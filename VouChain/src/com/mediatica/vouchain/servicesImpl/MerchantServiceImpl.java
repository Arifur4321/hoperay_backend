package com.mediatica.vouchain.servicesImpl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mediatica.vouchain.config.Constants;
import com.mediatica.vouchain.config.enumerations.TransactionStatus;
import com.mediatica.vouchain.config.enumerations.TransactionType;
import com.mediatica.vouchain.dao.MerchantDaoImpl;
import com.mediatica.vouchain.dao.TransactionDaoImpl;
import com.mediatica.vouchain.dao.UserDaoImpl;
import com.mediatica.vouchain.dto.DTO;
import com.mediatica.vouchain.dto.LocalizationDTO;
import com.mediatica.vouchain.dto.MerchantDTO;
import com.mediatica.vouchain.dto.ResponseGrantPermissionDTO;
import com.mediatica.vouchain.dto.ResponseNewKeyPairDTO;
import com.mediatica.vouchain.entities.Merchant;
import com.mediatica.vouchain.entities.Transaction;
import com.mediatica.vouchain.entities.User;
import com.mediatica.vouchain.exceptions.EmailYetInTheSystemException;
import com.mediatica.vouchain.export.PdfServicesImpl;
import com.mediatica.vouchain.mail.Mail;
import com.mediatica.vouchain.mail.MailSenderServiceImpl;
import com.mediatica.vouchain.rest.client.BlockChainRestClient;
import com.mediatica.vouchain.servicesInterface.MerchantServicesInterface;

/**
 * 
 * @author Pietro Napolitano
 *
 */
@Service
@Transactional
@PropertySource("file:${vouchain_home}/configurations/config.properties")
@Scope( proxyMode = ScopedProxyMode.TARGET_CLASS )
public class MerchantServiceImpl extends UserServiceAbstract implements MerchantServicesInterface{

	@Autowired
	UserDaoImpl userDao;
	
	@Autowired
	MerchantDaoImpl merchantDao;
	
	@Autowired
	TransactionDaoImpl transactionDao;
	
	@Autowired
	BlockChainRestClient blockChainClient;
	
	@Autowired
	PdfServicesImpl pdfService;
	
	@Autowired
	EmailServiceImpl emailServiceImpl;
	
	@Autowired
	GeographicServiceImpl geographicServiceImpl;
	
	@Value("${salt_complexity}")
	private int saltComplexity;
	
	@Value("${email_yet_system}")
	private String msgEmailYetInTheSystem;
	
	private static org.slf4j.Logger log = LoggerFactory.getLogger(MerchantServiceImpl.class);

	
	
	@Override
	public DTO signUp(DTO usr) throws EmailYetInTheSystemException {
		User user = null;
		MerchantDTO merchantDTO = (MerchantDTO)usr;
		MerchantDTO toReturn = new MerchantDTO();
		
		log.info("Checking if email is in yet the System");
		user = userDao.findUserByEmail(merchantDTO.getUsrEmail());
		if(user!=null) {
			throw new EmailYetInTheSystemException(msgEmailYetInTheSystem);
		}
		
		user = (User) merchantDTO.unwrap(merchantDTO);
		
		//////password managing
		log.info("Crypting password for userMail: {}",user.getUsrEmail());
		String salt = BCrypt.gensalt(saltComplexity);
		user.setUsrSalt(salt);
		String pw_hash = BCrypt.hashpw(merchantDTO.getUsrPassword(), salt);
		user.setUsrPassword(pw_hash);
		//////
		
		user.setUsrActive(true);
		user.getMerchant().setMrcChecked(1);
		user.setUsrNotificationEnable("true");
		
		
		Merchant merchantEntity = user.getMerchant();
		user.setMerchant(null);
		
		///////Inserting in database
		log.info("inserting in the database the user: {}",user.getUsrEmail());
		userDao.insert(user);
		log.debug("user inserted insering Merchant ");
		merchantEntity.setUser(user);
		merchantEntity.setUsrId(user.getUsrId()); 
		merchantDao.insert(merchantEntity);
		log.debug("Merchant inserted");
		user.setMerchant(merchantEntity);
		Transaction trx = new Transaction();
		trx.setTrcDate(new Date());
		trx.setTrcState(TransactionStatus.PENDING.getValue());
		trx.setTrcType(TransactionType.NEW_USER.getDescription());
		trx.setUsrIdDa(user); 

		////// calling bloackchain
		try {
			log.info("Calling BlockChain Services in MerchantSignUp: {}",new Date());
			ResponseNewKeyPairDTO keyPairResult = blockChainClient.invokeNewKeyPair();
			if(keyPairResult.getError()==null || keyPairResult.getError().isEmpty()) {
				user.setUsrBchAddress(keyPairResult.getResult().getAddress());
				ResponseGrantPermissionDTO grantResult = blockChainClient.invokeGrantPermission(keyPairResult.getResult().getAddress(), Constants.ENTITIES_TYPE.MERCHANT.toString());
				if(grantResult.getError()==null || grantResult.getError().isEmpty()) {
					user.setUsrPrivateKey(keyPairResult.getResult().getPrivateKey());
					String trcTxId =  grantResult.getResult();
					trx.setTrcTxId(trcTxId);
					
					LocalizationDTO localizationDTO = new LocalizationDTO();
					localizationDTO = geographicServiceImpl.getMapsLongLat(merchantDTO.getMrcAddressOffice(), merchantDTO.getMrcProvOffice(), merchantDTO.getMrcCityOffice());
					
					if(localizationDTO.getLongitude()!=null && localizationDTO.getLatitude()!=null) {
						user.getMerchant().setMrcLatitude(new BigDecimal(localizationDTO.getLatitude()));
						user.getMerchant().setMrcLongitude(new BigDecimal(localizationDTO.getLongitude()));
					}
					
					user.getMerchant().setMrcImageProfile(merchantDTO.getMrcImageProfile());
					
					
					//////updating user and transaction with the information retrieved from the blockchain
					log.debug("updating in the database the user: {}",user.getUsrEmail());
					userDao.update(user);
					log.debug("updating in the database the transaction: {}",trx.getTrcId());
					trx.setTrcState(TransactionStatus.CONFIRMED.getValue());
					transactionDao.insert(trx);
				}else {
					throw new Exception(grantResult.getError());
				}
				//////
			}else {
				throw new Exception(keyPairResult.getError());
			}
		}catch(Exception e) {
			log.error("Error invoking blockchain services",e);
			log.warn("Even if there is an error during blockchain comunication Let's continue ");
		}
		//////////////////////
		

		/////managing mail
		emailServiceImpl.manageMerchantConfirmationMail(user, trx);
		
		toReturn = (MerchantDTO)toReturn.wrap(user);
		return toReturn;
	}





	@Override
	public DTO modProfile(DTO usrDTO) throws Exception {
		MerchantDTO merchantDTO =(MerchantDTO)usrDTO;
		MerchantDTO result = new MerchantDTO();
		String usrId = merchantDTO.getUsrId();
		log.info("Modifying company information about company with Id: {}",usrId);
		User usr = new User(); 
		usr = userDao.findByPrimaryKey(usr, Integer.parseInt(usrId));
		if(usr!=null && usr.getMerchant()!=null) {

			//////password managing
			if(merchantDTO.getUsrPassword()!=null &&!merchantDTO.getUsrPassword().isEmpty()) {
				log.info("Crypting password for userMail: {}",merchantDTO.getUsrEmail());
				String salt = null;
				if(usr.getUsrSalt()!=null && !usr.getUsrSalt().isEmpty()) {
					salt =usr.getUsrSalt();
				}else {
					salt = BCrypt.gensalt(saltComplexity);
					usr. setUsrSalt(salt);
				}
				String pw_hash = BCrypt.hashpw(merchantDTO.getUsrPassword(), salt);
				usr.setUsrPassword(pw_hash);
			}
			//////
			usr.getMerchant().setMrcAddress(merchantDTO.getMrcAddress());
			usr.getMerchant().setMrcAddressOffice(merchantDTO.getMrcAddressOffice());
			usr.getMerchant().setMrcBank(merchantDTO.getMrcBank());
			usr.getMerchant().setMrcCity(merchantDTO.getMrcCity());
			usr.getMerchant().setMrcCityOffice(merchantDTO.getMrcCityOffice());
			usr.getMerchant().setMrcCodiceFiscale(merchantDTO.getMrcCodiceFiscale());
			usr.getMerchant().setMrcFirstNameRef(merchantDTO.getMrcFirstNameRef());
			usr.getMerchant().setMrcFirstNameReq(merchantDTO.getMrcFirstNameReq());
			usr.getMerchant().setMrcIban(merchantDTO.getMrcIban());
			usr.getMerchant().setMrcLastNameRef(merchantDTO.getMrcLastNameRef());
			usr.getMerchant().setMrcLastNameReq(merchantDTO.getMrcLastNameReq());
			usr.getMerchant().setMrcOfficeName(merchantDTO.getMrcOfficeName());
			usr.getMerchant().setMrcPartitaIva(merchantDTO.getMrcPartitaIva());
			usr.getMerchant().setMrcPhoneNo(merchantDTO.getMrcPhoneNo());
			usr.getMerchant().setMrcPhoneNoOffice(merchantDTO.getMrcPhoneNoOffice());
			usr.getMerchant().setMrcProv(merchantDTO.getMrcProv());
			usr.getMerchant().setMrcProvOffice(merchantDTO.getMrcProvOffice());
			usr.getMerchant().setMrcRagioneSociale(merchantDTO.getMrcRagioneSociale());
			usr.getMerchant().setMrcRoleReq(merchantDTO.getMrcRoleReq());
			usr.getMerchant().setMrcZip(merchantDTO.getMrcZip());
			
			if(merchantDTO.getMrcImageProfile()!=null) {
				usr.getMerchant().setMrcImageProfile(merchantDTO.getMrcImageProfile());
			}
			if(merchantDTO.getMrcNotificationEnabled()!=null) {
				usr.setUsrNotificationEnable(merchantDTO.getMrcNotificationEnabled());
			}
			usr.setUsrAccessType(merchantDTO.getAccessType());
			if(merchantDTO.getPinCode()!=null) {
				usr.setUsrPin(merchantDTO.getPinCode());
			}
			else {
				usr.setUsrPin(null);
			}
			
			
			LocalizationDTO localizationDTO = new LocalizationDTO();
			localizationDTO = geographicServiceImpl.getMapsLongLat(merchantDTO.getMrcAddressOffice(), merchantDTO.getMrcProvOffice(), merchantDTO.getMrcCityOffice());
			
			if(localizationDTO.getLongitude()!=null && localizationDTO.getLatitude()!=null) {
				usr.getMerchant().setMrcLatitude(new BigDecimal(localizationDTO.getLatitude()));
				usr.getMerchant().setMrcLongitude(new BigDecimal(localizationDTO.getLongitude()));
			}
			
			userDao.update(usr);
			result=(MerchantDTO) result.wrap(usr);
			result.setMrcLinkMap(localizationDTO.getLink());
			
		}else {
			throw new Exception("no_user_found");
		}
		return  result;
	}

	@Override
	public List<MerchantDTO> getMerchantsList() {
		List<Merchant> listEntities =  merchantDao.listAll();
		List<MerchantDTO> result=null;
		if(listEntities!=null) {
			result=new ArrayList<MerchantDTO>();
			for(Merchant item:listEntities) {
				MerchantDTO dto = new MerchantDTO();
				dto=(MerchantDTO)dto.wrap(item.getUser());
				result.add(dto);
			}
		}
		return result;
	}





	@Override
	public Merchant getFromId(Integer id) {
		User user = userDao.findUserById(id);
		return user.getMerchant();
	}





	@Override
	public HashMap<MerchantDTO, String> searchMerchantGIS(LocalizationDTO localizationDTO) {
		HashMap<Merchant, String> listEntities =  merchantDao.cercaMerchant(localizationDTO);
		HashMap<MerchantDTO, String> result=null;
		if(listEntities!=null) {
			result=new HashMap<MerchantDTO, String>();
			for(Merchant item:listEntities.keySet()) {
				MerchantDTO dto = new MerchantDTO();
				dto=(MerchantDTO)dto.wrap(item.getUser());
				String distanza = listEntities.get(item);
				result.put(dto, distanza);
			}
		}
		return result;
	}

	
	
}
