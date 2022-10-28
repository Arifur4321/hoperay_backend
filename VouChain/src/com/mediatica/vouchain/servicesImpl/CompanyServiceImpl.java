package com.mediatica.vouchain.servicesImpl;



import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.PublicKey;
import java.security.Security;
import java.security.cert.Certificate;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;

import org.bouncycastle.cert.X509CertificateHolder;
import org.bouncycastle.cert.jcajce.JcaX509CertificateConverter;
import org.bouncycastle.cms.CMSException;
import org.bouncycastle.cms.CMSProcessable;
import org.bouncycastle.cms.CMSSignedData;
import org.bouncycastle.cms.SignerInformation;
import org.bouncycastle.cms.SignerInformationStore;
import org.bouncycastle.cms.jcajce.JcaSimpleSignerInfoVerifierBuilder;
import org.bouncycastle.util.Store;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.mediatica.vouchain.config.Constants;
import com.mediatica.vouchain.config.enumerations.TransactionStatus;
import com.mediatica.vouchain.config.enumerations.TransactionType;
import com.mediatica.vouchain.dao.CompanyDaoImpl;
import com.mediatica.vouchain.dao.TransactionDaoImpl;
import com.mediatica.vouchain.dao.UserDaoImpl;
import com.mediatica.vouchain.dto.CompanyDTO;
import com.mediatica.vouchain.dto.DTO;
import com.mediatica.vouchain.dto.ResponseGrantPermissionDTO;
import com.mediatica.vouchain.dto.ResponseNewKeyPairDTO;
import com.mediatica.vouchain.entities.Company;
import com.mediatica.vouchain.entities.Transaction;
import com.mediatica.vouchain.entities.User;
import com.mediatica.vouchain.exceptions.ContractNotVaidException;
import com.mediatica.vouchain.exceptions.EmailYetInTheSystemException;
import com.mediatica.vouchain.export.PdfServicesImpl;
import com.mediatica.vouchain.rest.client.BlockChainRestClient;
import com.mediatica.vouchain.servicesInterface.CompanyServicesInterface;
import com.mediatica.vouchain.utilities.Utils;



/**
 * 
 * Thic class manage the services about the company entity
 * 
 * @author Pietro Napolitano
 *
 */
@Service
@Transactional
@PropertySource("file:${vouchain_home}/configurations/config.properties")
@Scope( proxyMode = ScopedProxyMode.TARGET_CLASS )
public class CompanyServiceImpl extends UserServiceAbstract implements CompanyServicesInterface{

	@Autowired
	UserDaoImpl userDao;
	
	@Autowired
	CompanyDaoImpl companyDao;
	
	@Autowired
	TransactionDaoImpl transactionDao;
	
	@Autowired
	BlockChainRestClient blockChainClient;
	
	@Autowired
	EmailServiceImpl emailServiceImpl;
	
	@Autowired
	PdfServicesImpl pdfService;
	
	@Value("${salt_complexity}")
	private int saltComplexity;
	
	@Value("${email_yet_system}")
	private String msgEmailYetInTheSystem;
	
	@Value("${validate_hash}")
	private String validateHash;
	
	@Value("${pem_file_path}")
	private String pemFilePath;
	
	
	private static org.slf4j.Logger log = LoggerFactory.getLogger(CompanyServiceImpl.class);
	

	@Override
	public CompanyDTO checkLoadSign(String usrId, MultipartFile contract) throws IOException,ContractNotVaidException,Exception {
		User usr = new User();
		CompanyDTO companyDTO = new CompanyDTO();
		if(usrId!=null) {
			usr=userDao.findByPrimaryKey(usr, Integer.parseInt(usrId));
			if(usr!=null) {
				boolean contractIsValid= validateContract(contract,usr);
				if(contractIsValid) {
					usr.getCompany().setCpyContract(contract.getBytes());
					usr.getCompany().setCpyContractChecked(true);
					userDao.update(usr);
					companyDTO = (CompanyDTO)companyDTO.wrap(usr);	
				}else {
					throw new ContractNotVaidException("The contract is not valid");
				}
				
			}
		}

		return companyDTO;
	}


	@Override
	public DTO signUp(DTO usr) throws EmailYetInTheSystemException {
			User user = null;
			CompanyDTO company = (CompanyDTO)usr;
			CompanyDTO toReturn = new CompanyDTO();
			
			Calendar cal = Calendar.getInstance();
			String timeString = new SimpleDateFormat("EEE yyyy.MM.dd HH:mm:ss.SSS z").format(cal.getTime());
			log.info("INIZIO SERVICE SIGN UP COMPANY "+timeString);
			
			log.info("Checking if email is in yet the System");
			user = userDao.findUserByEmail(company.getUsrEmail());
			if(user!=null) {
				throw new EmailYetInTheSystemException(msgEmailYetInTheSystem);
			}
			
			user = (User) company.unwrap(company);
			
			//////password managing
			log.info("Crypting password for userMail: {}",user.getUsrEmail());
			String salt = BCrypt.gensalt(saltComplexity);
			user.setUsrSalt(salt);
			String pw_hash = BCrypt.hashpw(company.getUsrPassword(), salt);
			user.setUsrPassword(pw_hash);
			//////
			
			user.setUsrActive(true);
			user.getCompany().setCpyContractChecked(false);
			Company companyEntity = user.getCompany();
			user.setCompany(null);
			
			///////Inserting in database
			log.info("inserting in the database the user: {}",user.getUsrEmail());
			userDao.insert(user);
			log.debug("user inserted insering company ");
			companyEntity.setUser(user);
			companyEntity.setUsrId(user.getUsrId()); 
			companyDao.insert(companyEntity);
			log.debug("company inserted");
			user.setCompany(companyEntity);
			Transaction trx = new Transaction();
			trx.setTrcDate(new Date());
			trx.setTrcState(TransactionStatus.PENDING.getValue());
			trx.setTrcType(TransactionType.NEW_USER.getDescription());
			trx.setUsrIdDa(user); 
			
			cal = Calendar.getInstance();
			timeString = new SimpleDateFormat("EEE yyyy.MM.dd HH:mm:ss.SSS z").format(cal.getTime());
			log.info("PRIMA DI BLOCKCHAIN SIGN UP COMPANY"+timeString + "ID: "+ user.getUsrId());
			////// calling blockchain
			try {
				log.info("Calling BlockChain Services in companySignUp: "+new Date());
				
				//creating new address in blockchain (multichain)
				ResponseNewKeyPairDTO keyPairResult = blockChainClient.invokeNewKeyPair();
				

				if(keyPairResult.getError()==null || keyPairResult.getError().isEmpty()) {
					user.setUsrBchAddress(keyPairResult.getResult().getAddress());
					ResponseGrantPermissionDTO grantResult = blockChainClient.invokeGrantPermission(keyPairResult.getResult().getAddress(), Constants.ENTITIES_TYPE.COMPANY.toString());
					if(grantResult.getError()==null || grantResult.getError().isEmpty()) {
						user.setUsrPrivateKey(keyPairResult.getResult().getPrivateKey());
						String trcTxId =  grantResult.getResult();
						trx.setTrcTxId(trcTxId);
						cal = Calendar.getInstance();
						timeString = new SimpleDateFormat("EEE yyyy.MM.dd HH:mm:ss.SSS z").format(cal.getTime());
						log.info("DOPO DI BLOCKCHAIN"+timeString + "ID: "+ user.getUsrId());
						//////updating user and transaction with the information retrieved from the blockchain
						log.debug("updating in the database the user: {}",user.getUsrEmail());
						userDao.update(user);
						log.debug("updating in the database the transaction: {}",trx.getTrcId());
						trx.setTrcState(TransactionStatus.CONFIRMED.getValue());
						transactionDao.insert(trx);
						cal = Calendar.getInstance();
						timeString = new SimpleDateFormat("EEE yyyy.MM.dd HH:mm:ss.SSS z").format(cal.getTime());
						log.info("DOPO UPDATE DB SIGN UP COMPANY"+timeString + "ID: "+ user.getUsrId());
		
					}else {
						throw new Exception(grantResult.getError());
					}
				}else {
					throw new Exception(keyPairResult.getError());
				}
				//////
			}catch(Exception e) {
				log.error("Error invoking blockchain services",e);
				log.warn("Even if there is an error during blockchain comunication Let's continue ");
			}
			//////////////////////
			

			/////managing mail
			emailServiceImpl.manageCompanyConfirmationMail(user, trx);
			
			cal = Calendar.getInstance();
			timeString = new SimpleDateFormat("EEE yyyy.MM.dd HH:mm:ss.SSS z").format(cal.getTime());
			log.info("DOPO INVIO EMAIL SIGN UP COMPANY"+timeString + "ID: "+ user.getUsrId());
			
			toReturn = (CompanyDTO)toReturn.wrap(user);
			return toReturn;
	}







	@Override
	public DTO modProfile(DTO usrDTO) throws Exception {
		CompanyDTO companyDTO =(CompanyDTO)usrDTO;
		CompanyDTO result = new CompanyDTO();
		String usrId = companyDTO.getUsrId();
		log.info("Mofiying company information about company with Id: {}",usrId);
		User usr = new User(); 
		if(usrId!=null) {
			usr = userDao.findByPrimaryKey(usr, Integer.parseInt(usrId));
			if(usr!=null) {
				usr.getCompany().setCpyCodiceFiscale(companyDTO.getCpyCodiceFiscale());
				usr.getCompany().setCpyPartitaIva(companyDTO.getCpyPartitaIva());
				usr.getCompany().setCpyPec(companyDTO.getCpyPec());
				usr.getCompany().setCpyCuu(companyDTO.getCpyCuu());
				usr.getCompany().setCpyFirstNameRef(companyDTO.getCpyFirstNameRef());
				usr.getCompany().setCpyLastNameRef(companyDTO.getCpyLastNameRef());
				usr.getCompany().setCpyPhoneNoRef(companyDTO.getCpyPhoneNoRef());
				
				usr.getCompany().setCpyRagioneSociale(companyDTO.getCpyRagioneSociale());
				usr.getCompany().setCpyAddress(companyDTO.getCpyAddress());
				usr.getCompany().setCpyCity(companyDTO.getCpyCity());
				usr.getCompany().setCpyProv(companyDTO.getCpyProv());
				usr.getCompany().setCpyZip(companyDTO.getCpyZip());
				
				
				userDao.update(usr);
				result=(CompanyDTO) result.wrap(usr);
		}

		}else {
			throw new Exception("no_user_found");
		}
		return  result;
	}


	
	///// PRIVATE METHODS
	
	private boolean validateContract(MultipartFile contract,User usr) throws IOException,Exception {
		byte[] originalHash = usr.getCompany().getCpyHashedContract();

		boolean hasTheSameHash=true;
		byte[] fileInByte= contract.getBytes();
		String filePath= Constants.VOUCHAIN_HOME+Constants.TEMP_SUBDIR +"/"+usr.getUsrId()+"_"+contract.getOriginalFilename();
		log.debug("Writing contract file: "+filePath);
		File tempFile = Utils.writeByte(fileInByte, filePath);
		
		if(validateHash!=null&&!validateHash.isEmpty()&&validateHash.toLowerCase().trim().equals("true")) {
			log.info("Validating hash");
			File extractedContractFile = extractDecryptedContractFile(tempFile,String.valueOf(usr.getUsrId()));	
			hasTheSameHash = Utils.validateHash(extractedContractFile,originalHash);
		}
		boolean isContractSignedValid = false;
		if(hasTheSameHash) {
			isContractSignedValid = validateSignedContract(tempFile);
		}
		if(!isContractSignedValid || !hasTheSameHash) {
			if(!hasTheSameHash) {
				throw new ContractNotVaidException(Constants.HASH_VIOLATION);
			}else{
				throw new ContractNotVaidException(Constants.SIGN_VIOLATION);
			}
			
		}
		return (hasTheSameHash && isContractSignedValid);
		
	}




	private boolean validateSignedContract(File contract) throws IOException, InterruptedException {

		log.debug("Validating contract file: {}",contract.getName());
		boolean isValid=validate(contract);
		log.debug("Deleting contract file: {}",contract);
		try {
			boolean isDeleted= contract.delete();
			log.debug("Deleted contract file {}:{}",contract,isDeleted);
		}catch(Exception e ) {
			log.warn("Unable to delete: {} let's continue anyway",contract);
		}
	
		return isValid;
	}

	
	private boolean validate(File contractFile) {
	   String caPath =Constants.VOUCHAIN_HOME+pemFilePath;
	   log.info("validating contractFile: {}",contractFile.getName());
	     boolean isValid = false;
	   try {
		   Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
		   byte[] buffer = new byte[(int) contractFile.length()];
		   DataInputStream in = new DataInputStream(new FileInputStream(contractFile));
		   in.readFully(buffer);
		   in.close();

		   //Corresponding class of signed_data is CMSSignedData
		   CMSSignedData signature = new CMSSignedData(buffer);
		   Store cs = signature.getCertificates();
		   SignerInformationStore signers = signature.getSignerInfos();
		   Collection c = signers.getSigners();
		   Iterator it = c.iterator();

		   //the following array will contain the content of xml document
		   byte[] data = null;

		   while (it.hasNext() && !isValid) {
		        SignerInformation signer = (SignerInformation) it.next();
		        Collection certCollection = cs.getMatches(signer.getSID());
		        Iterator certIt = certCollection.iterator();
		        X509CertificateHolder cert = (X509CertificateHolder) certIt.next();


		        
		        
		        CMSProcessable sc = signature.getSignedContent();
		        data = (byte[]) sc.getContent();
		        
			     // Create a X509 certificate
			     CertificateFactory certificatefactory = CertificateFactory.getInstance("X.509","BC");

			     // Open the certificate file
			     BufferedInputStream buffinputstream = new BufferedInputStream(new FileInputStream(caPath));


			     while (!isValid) {
				     Certificate publicCerificate = certificatefactory.generateCertificate(buffinputstream);
				     //get CA public key
				     PublicKey pk = publicCerificate.getPublicKey();

				     X509Certificate myCA = new JcaX509CertificateConverter().setProvider("BC").getCertificate(cert);
				     try {
					     myCA.verify(pk);
					     log.info("****** the certificate is validated *********");
					     boolean isOk = signer.verify(new JcaSimpleSignerInfoVerifierBuilder().setProvider("BC").build(cert));
					     log.info("****** the sign on the document is validated ********* {}",isOk);
			  		     isValid = true;
				     } catch(Exception e) {
				     	log.warn("not valid for this certficate let's continue");
				     }
			     }
		    }
	   }catch(Exception e) {
		   	log.error("***** The contract is not valid *******");
	   }
	   return isValid;
	}
	
	
	private File extractDecryptedContractFile(File cryptedFile,String userId) throws Exception {

	    log.debug("decripting file: {}",cryptedFile.getAbsolutePath());
	    byte[] content = org.apache.commons.io.FileUtils.readFileToByteArray(cryptedFile);
	    File file = null;
	    byte[] contentUnsigned = removeP7MCodes(cryptedFile.getName(), content);
	    if (contentUnsigned != null) {
	    	String tempFilePath= Constants.VOUCHAIN_HOME+File.separator+cryptedFile.getName().substring(0,cryptedFile.getName().indexOf(Constants.P7M_SUFFIX));
	        file = new File(tempFilePath);
	        FileOutputStream fos = null;
	            fos = new FileOutputStream(file);
	            fos.write(contentUnsigned);
	    }
	    return file;
	    	
	}

	private byte[] removeP7MCodes(final String fileName, final byte[] p7bytes) throws IOException, CMSException {

	        if (p7bytes == null)
	            return p7bytes;

	        if (!fileName.toUpperCase().endsWith(".P7M")) {
	            return p7bytes;
	        }


	        CMSSignedData cms = new CMSSignedData(p7bytes); 

	        if (cms.getSignedContent() == null) {
	            log.error("Unable to find signed Content during decoding from P7M for file: {}" , fileName);               
	            return null;
	        }

	        ByteArrayOutputStream out = new ByteArrayOutputStream();
	        cms.getSignedContent().write(out);

	        return out.toByteArray();

	}
	
//	private Mail prepareConfirmationMail(User user,File attachment) {
//		Mail mail  = new Mail();
//		mail.setMailFrom(mailSenderService.getMailFrom());
//		mail.setMailSubject(mailSenderService.getMailCompanyConfirmationSubject());
//		mail.setMailTo(user.getCompany().getCpyPec());
//        
//		Map < String, Object > model = new HashMap < String, Object > ();
//        model.put("usrEmail",user.getUsrEmail()!=null?user.getUsrEmail():"");
//        model.put("cpyCodiceFiscale", user.getCompany().getCpyCodiceFiscale()!=null?user.getCompany().getCpyCodiceFiscale():"");
//        model.put("cpyPartitaIva", user.getCompany().getCpyPartitaIva()!=null?user.getCompany().getCpyPartitaIva():"");
//        model.put("cpyRagioneSociale", user.getCompany().getCpyRagioneSociale()!=null?user.getCompany().getCpyRagioneSociale():"");
//
//        model.put("cpyPec", user.getCompany().getCpyPec()!=null?user.getCompany().getCpyPec():"");
//        model.put("cpyAddress", user.getCompany().getCpyAddress()!=null?user.getCompany().getCpyAddress():"");
//        model.put("cpyCity", user.getCompany().getCpyCity()!=null?user.getCompany().getCpyCity() :"");
//        model.put("cpyProv", user.getCompany().getCpyProv()!=null?user.getCompany().getCpyProv() :"");
//
//        model.put("cpyZip", user.getCompany().getCpyZip()!=null?user.getCompany().getCpyZip() :"");
//        model.put("cpyFirstName", user.getCompany().getCpyFirstNameRef()!=null?user.getCompany().getCpyFirstNameRef() :"");
//        model.put("cpyLastName", user.getCompany().getCpyLastNameRef()!=null?user.getCompany().getCpyLastNameRef() :"");
//        model.put("cpyNoPhoneRef", user.getCompany().getCpyPhoneNoRef()!=null?user.getCompany().getCpyPhoneNoRef() :"");
//
//
//        model.put("cpyCuu", user.getCompany().getCpyCuu()!=null?user.getCompany().getCpyCuu():"");
//
//        
//        
//        
//		mail.setModel(model);
//		if(attachment!=null) {
//			log.debug("attaching file: "+attachment.getName());
//			List<Object> attachments = new ArrayList<Object>();
//			attachments.add(attachment);
//			mail.setAttachments(attachments);
//		}
//		return mail;
//	}


}
