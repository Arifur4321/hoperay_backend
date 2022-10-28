package com.mediatica.vouchain.servicesImpl;

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
import com.mediatica.vouchain.dao.EmployeeDaoImpl;
import com.mediatica.vouchain.dao.TransactionDaoImpl;
import com.mediatica.vouchain.dao.UserDaoImpl;
import com.mediatica.vouchain.dto.DTO;
import com.mediatica.vouchain.dto.EmployeeDTO;
import com.mediatica.vouchain.dto.ResponseGrantPermissionDTO;
import com.mediatica.vouchain.dto.ResponseNewKeyPairDTO;
import com.mediatica.vouchain.entities.Employee;
import com.mediatica.vouchain.entities.Transaction;
import com.mediatica.vouchain.entities.User;
import com.mediatica.vouchain.exceptions.EmailYetInTheSystemException;
import com.mediatica.vouchain.export.PdfServicesImpl;
import com.mediatica.vouchain.mail.Mail;
import com.mediatica.vouchain.mail.MailSenderServiceImpl;
import com.mediatica.vouchain.rest.client.BlockChainRestClient;
import com.mediatica.vouchain.servicesInterface.EmployeeServicesInterface;
import com.mediatica.vouchain.utilities.Utils;

@Service
@Transactional
@PropertySource("file:${vouchain_home}/configurations/config.properties")
@Scope( proxyMode = ScopedProxyMode.TARGET_CLASS )
public class EmployeeServiceImpl extends UserServiceAbstract implements EmployeeServicesInterface{

	@Autowired
	UserDaoImpl userDao;
	
	@Autowired
	EmployeeDaoImpl employeeDao;
	
	@Autowired
	TransactionDaoImpl transactionDao;
	
	@Autowired
	BlockChainRestClient blockChainClient;
	
	@Autowired
	PdfServicesImpl pdfService;	
	
	@Autowired
	EmailServiceImpl emailServiceImpl;
	
	@Value("${salt_complexity}")
	private int saltComplexity;
	
	@Value("${email_yet_system}")
	private String msgEmailYetInTheSystem;
	
	@Value("${validate_hash}")
	private String validateHash;
	
	@Value("${pem_file_path}")
	private String pemFilePath;
	
	private static org.slf4j.Logger log = LoggerFactory.getLogger(EmployeeServiceImpl.class);

	
	@Override
	public DTO signUp(DTO usr) throws EmailYetInTheSystemException {
		User user = null;
		EmployeeDTO employeeItem = (EmployeeDTO)usr;
		EmployeeDTO toReturn = new EmployeeDTO();
		
		log.info("Checking if email is in yet the System");
		user = userDao.findUserByEmail(employeeItem.getUsrEmail());
		if(user!=null) {
			throw new EmailYetInTheSystemException(msgEmailYetInTheSystem);
		}
		
		user = (User) employeeItem.unwrap(employeeItem);
		user.getEmployee().setEmpCheckedLogin(false);
		
		String invitationCode = Utils.generateAlphaNumericString();
		user.getEmployee().setEmpInvitationCode(invitationCode);
		user.setUsrActive(true);

		
		
		Employee employeeItemEntity = user.getEmployee();
		user.setEmployee(null);
		
		///////Inserting in database
		log.info("inserting in the database the user: {}",user.getUsrEmail());
		userDao.insert(user);
		log.debug("user inserted insering employeeItem ");
		employeeItemEntity.setUser(user);
		employeeItemEntity.setUsrId(user.getUsrId()); 
		employeeDao.insert(employeeItemEntity);
		log.debug("employeeItem inserted");
		user.setEmployee(employeeItemEntity);

		toReturn = (EmployeeDTO)toReturn.wrap(user);
		return toReturn;
	}



	@Override
	public DTO modProfile(DTO usrDTO) throws Exception {
		EmployeeDTO employeeDTO =(EmployeeDTO)usrDTO;
		EmployeeDTO result = new EmployeeDTO();
		String usrId = employeeDTO.getUsrId();
		log.info("Modifying company information about company with Id: {}",usrId);
		User usr = new User(); 
		usr = userDao.findByPrimaryKey(usr, Integer.parseInt(usrId));
		if(usr!=null && usr.getEmployee()!=null) {

			//////password managing
			if(employeeDTO.getUsrPassword()!=null &&!employeeDTO.getUsrPassword().isEmpty()) {
				log.info("Crypting password for userMail: {}",employeeDTO.getUsrEmail());
				String salt = null;
				if(usr.getUsrSalt()!=null && !usr.getUsrSalt().isEmpty()) {
					salt =usr.getUsrSalt();
				}else {
					salt = BCrypt.gensalt(saltComplexity);
					usr. setUsrSalt(salt);
				}
				String pw_hash = BCrypt.hashpw(employeeDTO.getUsrPassword(), salt);
				usr.setUsrPassword(pw_hash);
			}
			//////
			usr.getEmployee().setEmpFirstName(employeeDTO.getEmpFirstName());
			usr.getEmployee().setEmpLastName(employeeDTO.getEmpLastName());
			usr.getEmployee().setEmpMatricola(employeeDTO.getEmpMatricola());
			usr.setUsrAccessType(employeeDTO.getAccessType());
			if(employeeDTO.getEmpNotificationEnabled()!=null) {
				usr.setUsrNotificationEnable(employeeDTO.getEmpNotificationEnabled());
			}
			if(employeeDTO.getPinCode()!=null) {
				usr.setUsrPin(employeeDTO.getPinCode());
			}
			else {
				usr.setUsrPin(null);
			}
			
			
			userDao.update(usr);
			result=(EmployeeDTO) result.wrap(usr);
		}else {
			throw new Exception("no_user_found");
		}
		return  result;
	}

	@Override
	public List<EmployeeDTO> insertEmployeesList(List<EmployeeDTO> employeeList) throws EmailYetInTheSystemException {
		List<EmployeeDTO> response = new ArrayList<EmployeeDTO>();
		for(EmployeeDTO employDTO:employeeList) {
			EmployeeDTO emp = (EmployeeDTO) signUp(employDTO);
			response.add(emp);
		}
		
		return response;
	}

	@Override
	public void sendMail(List<EmployeeDTO> list) {
		emailServiceImpl.manageSignUpEmployeeMail(list);
		
	}


	@Override
	public List<EmployeeDTO> getEmployeeList(String companyId) {
		List<EmployeeDTO> response = null;
		List<Employee> list=employeeDao.listEmployeesByCompanyId(Integer.parseInt(companyId));
		if(list!=null) {
			response = new ArrayList<EmployeeDTO>();
			for(Employee emp:list) {
				EmployeeDTO empDTO = new EmployeeDTO();
				empDTO = (EmployeeDTO) empDTO.wrap(emp.getUser());
				response.add(empDTO);
			}
		}
		return response;
	}

	@Override
	public EmployeeDTO checkInvitationCode(String invitationCode) {
		EmployeeDTO response = null;
		Employee emp = new Employee();
		emp = employeeDao.findByInvitationCode(invitationCode);
		if(emp!=null && emp.getUser()!=null) {
			response=new EmployeeDTO();
			response = (EmployeeDTO) response.wrap(emp.getUser());
		}
		return response;
	}

	@Override
	public EmployeeDTO confirm(EmployeeDTO employeeDTO) throws Exception{
		EmployeeDTO result = new EmployeeDTO();
		String usrId = employeeDTO.getUsrId();
		log.info("Mofiying company information about company with Id: {}",usrId);
		User usr = new User(); 
		usr = userDao.findByPrimaryKey(usr, Integer.parseInt(usrId));
		if(usr!=null && usr.getEmployee()!=null) {

			//////password managing
			if(employeeDTO.getUsrPassword()!=null &&!employeeDTO.getUsrPassword().isEmpty()) {
				log.info("Crypting password for userMail: {}",employeeDTO.getUsrEmail());
				String salt = null;
				if(usr.getUsrSalt()!=null && !usr.getUsrSalt().isEmpty()) {
					salt =usr.getUsrSalt();
				}else {
					salt = BCrypt.gensalt(saltComplexity);
					usr. setUsrSalt(salt);
				}
				String pw_hash = BCrypt.hashpw(employeeDTO.getUsrPassword(), salt);
				usr.setUsrPassword(pw_hash);
			}
			//////
			usr.getEmployee().setEmpFirstName(employeeDTO.getEmpFirstName());
			usr.getEmployee().setEmpLastName(employeeDTO.getEmpLastName());
			usr.getEmployee().setEmpMatricola(employeeDTO.getEmpMatricola());
			usr.getEmployee().setEmpCheckedLogin(true);
			userDao.update(usr);
			
			Transaction trx = new Transaction();
			trx.setTrcDate(new Date());
			trx.setTrcState(TransactionStatus.PENDING.getValue());
			trx.setTrcType(TransactionType.NEW_USER.getDescription());
			trx.setUsrIdDa(usr); 
			
			////// calling bloackchain
			try {
				log.info("Calling BlockChain Services in companySignUp: {}",new Date());
	
				ResponseNewKeyPairDTO keyPairResult = blockChainClient.invokeNewKeyPair();
				if(keyPairResult.getError()==null || keyPairResult.getError().isEmpty()) {
					usr.setUsrBchAddress(keyPairResult.getResult().getAddress());
					
					ResponseGrantPermissionDTO grantResult = blockChainClient.invokeGrantPermission(keyPairResult.getResult().getAddress(), Constants.ENTITIES_TYPE.EMPLOYEE.toString());
					if(grantResult.getError()==null || grantResult.getError().isEmpty()) {
						
						usr.setUsrPrivateKey(keyPairResult.getResult().getPrivateKey());
						String trcTxId =  grantResult.getResult();
						trx.setTrcTxId(trcTxId);
						
						//////updating user and transaction with the information retrieved from the blockchain
						log.debug("updating in the database the user: {}",usr.getUsrEmail());
						userDao.update(usr);
						log.debug("updating in the database the transaction: {}",trx.getTrcId());
						trx.setTrcState(TransactionStatus.CONFIRMED.getValue());
						transactionDao.insert(trx);
					//////
					}else {
						throw new Exception(grantResult.getError());
					}
				}else {
					throw new Exception(keyPairResult.getError());
				}
			}catch(Exception e) {
				log.error("Error invoking blockchain services",e);
				log.warn("Even if there is an error during blockchain comunication Let's continue ");
			}
			//////////////////////
			
			/////managing mail
			emailServiceImpl.manageEmployeeConfirmationMail(usr, trx);
			
			
			result=(EmployeeDTO) result.wrap(usr);
		}else {
			throw new Exception("no_user_found");
		}
		return  result;
	}









	
//	private Mail prepareConfirmationMail(User user, Object object) {
//		Mail mail  = new Mail();
//		mail.setMailFrom(mailSenderService.getMailFrom());
//		mail.setMailSubject(mailSenderService.getMailEmployeeConfirmationSubject());
//		mail.setMailTo(user.getUsrEmail());
//        
//		Map < String, Object > model = new HashMap < String, Object > ();
//        model.put("usrEmail",user.getUsrEmail()!=null?user.getUsrEmail():"");
//        model.put("empFirstName", user.getEmployee().getEmpFirstName()!=null?user.getEmployee().getEmpFirstName():"");
//        model.put("empLastName", user.getEmployee().getEmpLastName()!=null?user.getEmployee().getEmpLastName():"");
//        model.put("empMatricola", user.getEmployee().getEmpMatricola()!=null?user.getEmployee().getEmpMatricola():"");
//		mail.setModel(model);
//		return mail;
//	}
	

	


}
