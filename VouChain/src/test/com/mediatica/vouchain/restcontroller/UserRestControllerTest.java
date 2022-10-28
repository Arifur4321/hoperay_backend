package test.com.mediatica.vouchain.restcontroller;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.bouncycastle.util.test.TestFailedException;
import org.glassfish.jersey.client.ClientConfig;
import org.glassfish.jersey.client.authentication.HttpAuthenticationFeature;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mediatica.vouchain.config.Constants;
import com.mediatica.vouchain.dao.CompanyDaoImpl;
import com.mediatica.vouchain.dao.TransactionDaoImpl;
import com.mediatica.vouchain.dao.UserDaoImpl;
import com.mediatica.vouchain.dto.CompanyDTO;
import com.mediatica.vouchain.dto.DTO;
import com.mediatica.vouchain.dto.PasswordDTO;
import com.mediatica.vouchain.dto.SimpleResponseDTO;
import com.mediatica.vouchain.dto.UserSimpleDTO;
import com.mediatica.vouchain.entities.Company;
import com.mediatica.vouchain.entities.Transaction;
import com.mediatica.vouchain.entities.User;
import com.mediatica.vouchain.exceptions.EmailYetInTheSystemException;
import com.mediatica.vouchain.servicesImpl.UserServiceImpl;
import com.mediatica.vouchain.servicesInterface.CompanyServicesInterface;
import com.mediatica.vouchain.utilities.EncrptingUtils;

@Transactional
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("file:WebContent/WEB-INF/vouchain-servlet.xml")
@WebAppConfiguration
public class UserRestControllerTest {

	private static org.slf4j.Logger log = LoggerFactory.getLogger(UserRestControllerTest.class);

	@Autowired
	SessionFactory sessionFactory;

	@Autowired
	private EncrptingUtils encrptingUtils;

	@Autowired
	UserDaoImpl userDaoImpl;

	@Autowired
	UserServiceImpl userServiceImpl;

	@Autowired
	CompanyServicesInterface companyServicesInterface;

	@Autowired
	TransactionDaoImpl transactionDaoImpl;

	@Autowired
	CompanyDaoImpl companyDaoImpl;


	@Value("${generic_user_auth_username}")
	private String GENERIC_USER_USERNAME;

	@Value("${generic_user_auth_password}")
	private String GENERIC_USER_PASSWORD;

	private String CHANGE_PASSWORD_URI="http://localhost:8080/VouChain/api/users/changePassword";

	private String MODIFY_PASSWORD_URI="http://localhost:8080/VouChain/api/users/modifyPassword";

	private String REPLACE_PASSWORD_URI="http://localhost:8080/VouChain/api/users/replacePassword";


	
	//------------USER FORGOTTEN PASSWORD MAIL-----------------------
	@Test
	public void userForgottenPasswordMailTest() {			
		CompanyDTO dto = fillCompanyDTO();	
		CompanyDTO dtoResult = null;
		Integer idUserInserted = null;

		try {
			log.info("Starting signUp...");
			dtoResult = (CompanyDTO) companyServicesInterface.signUp(dto);
			Session session = sessionFactory.getCurrentSession();
			session.getTransaction().commit();	

			if(dtoResult!=null) {
				log.info("signUp OK");
				idUserInserted=Integer.valueOf(dtoResult.getUsrId());
			}

			log.info("get data from DB...");

			UserSimpleDTO userSimpleDTO = new UserSimpleDTO();
			userSimpleDTO.setUsrEmail(dto.getUsrEmail());
			SimpleResponseDTO response = invokeUserForgottenPasswordMail(userSimpleDTO, "company");


			//delete data just inserted in DB		
			log.info("executing rollback...");
			rollBack(dtoResult, idUserInserted, session);
			log.info("rollback OK");
			
			
			assertTrue(response.getStatus().equals(Constants.RESPONSE_STATUS.OK.toString()));


		} catch (EmailYetInTheSystemException e) {
			e.printStackTrace();
			log.error("Email inserted yet");
			fail();
		} catch (TestFailedException ex) {
			ex.printStackTrace();
			log.error("Test Exception");
			fail();
		} catch (Exception ex) {
			ex.printStackTrace();
			log.error("Generic Exception");
			fail();
		}

	}
	
	
	
	@Test
	public void userForgottenPasswordMailTest_invalidEmail_KO() {			
		CompanyDTO dto = fillCompanyDTO();	
		CompanyDTO dtoResult = null;
		Integer idUserInserted = null;

		try {
			log.info("Starting signUp...");
			dtoResult = (CompanyDTO) companyServicesInterface.signUp(dto);
			Session session = sessionFactory.getCurrentSession();
			session.getTransaction().commit();	

			if(dtoResult!=null) {
				log.info("signUp OK");
				idUserInserted=Integer.valueOf(dtoResult.getUsrId());
			}

			log.info("get data from DB...");

			UserSimpleDTO userSimpleDTO = new UserSimpleDTO();
			userSimpleDTO.setUsrEmail("INVALID_EMAIL");
			SimpleResponseDTO response = invokeUserForgottenPasswordMail(userSimpleDTO, "company");


			//delete data just inserted in DB		
			log.info("executing rollback...");
			rollBack(dtoResult, idUserInserted, session);
			log.info("rollback OK");
			
			
			assertTrue(response.getStatus().equals(Constants.RESPONSE_STATUS.OK.toString()));


		} catch (EmailYetInTheSystemException e) {
			e.printStackTrace();
			log.error("Email inserted yet");
			fail();
		} catch (TestFailedException ex) {
			ex.printStackTrace();
			log.error("Test Exception");
			fail();
		} catch (Exception ex) {
			ex.printStackTrace();
			log.error("Generic Exception");
			fail();
		}

	}
	

	//------------MODIFY PASSWORD -----------------------
	@Test
	public void modifyPasswordTest() {

		CompanyDTO dto = fillCompanyDTO();	
		CompanyDTO dtoResult = null;
		Integer idUserInserted = null;
		UserSimpleDTO inputDTO = new UserSimpleDTO();

		try {
			log.info("Statring signUp...");
			dtoResult = (CompanyDTO) companyServicesInterface.signUp(dto);
			Session session = sessionFactory.getCurrentSession();
			session.getTransaction().commit();			


			if(dtoResult!=null) {
				log.info("signUp OK");

				log.info("try to send mail with recovery link...");
				org.hibernate.Transaction txn = session.beginTransaction();
				userServiceImpl.sendRecoveryLink(dtoResult.getUsrEmail(), "COMPANY");
				txn.commit();
				log.info("sending mail with recovery link OK");


				//recover the User before modify the password
				idUserInserted=Integer.valueOf(dtoResult.getUsrId());
				User userBeforeModifyPassword = new User();
				userBeforeModifyPassword = userDaoImpl.findByPrimaryKey(userBeforeModifyPassword, idUserInserted);

				log.info("modify Password...");

				inputDTO.setUsrPassword("newPassword123!");
				SimpleResponseDTO response = invokeModifyPassword(inputDTO, userBeforeModifyPassword.getUsrRecoverEmailCode());			
				log.info("Password modified");



				//delete data just inserted in DB
				log.info("executing rollback...");
				rollBack(dtoResult, idUserInserted, session);
				log.info("rollback OK");

				log.info("assert password modify...");
				assertTrue(response.getStatus().equals(Constants.RESPONSE_STATUS.OK.toString()));
				log.info("password modify OK");				

			}

		} catch (EmailYetInTheSystemException e) {
			e.printStackTrace();
			log.error("Email inserted yet");
			fail();
		} catch (TestFailedException ex) {
			ex.printStackTrace();
			log.error("Test Exception");
			fail();
		} catch (Exception ex) {
			ex.printStackTrace();
			log.error("Generic Exception");
			fail();
		}
	}
	
	
	@Test
	public void modifyPasswordTest_invalidPassword_KO() {

		CompanyDTO dto = fillCompanyDTO();	
		CompanyDTO dtoResult = null;
		Integer idUserInserted = null;
		UserSimpleDTO inputDTO = new UserSimpleDTO();

		try {
			log.info("Statring signUp...");
			dtoResult = (CompanyDTO) companyServicesInterface.signUp(dto);
			Session session = sessionFactory.getCurrentSession();
			session.getTransaction().commit();			


			if(dtoResult!=null) {
				log.info("signUp OK");

				log.info("try to send mail with recovery link...");
				org.hibernate.Transaction txn = session.beginTransaction();
				userServiceImpl.sendRecoveryLink(dtoResult.getUsrEmail(), "COMPANY");
				txn.commit();
				log.info("sending mail with recovery link OK");


				//recover the User before modify the password
				idUserInserted=Integer.valueOf(dtoResult.getUsrId());
				User userBeforeModifyPassword = new User();
				userBeforeModifyPassword = userDaoImpl.findByPrimaryKey(userBeforeModifyPassword, idUserInserted);

				log.info("modify Password...");

				inputDTO.setUsrPassword("invalid");
				SimpleResponseDTO response = invokeModifyPassword(inputDTO, userBeforeModifyPassword.getUsrRecoverEmailCode());			
				log.info("Password modified");



				//delete data just inserted in DB
				log.info("executing rollback...");
				rollBack(dtoResult, idUserInserted, session);
				log.info("rollback OK");

				log.info("assert password modify...");
				assertTrue(response.getStatus().equals(Constants.RESPONSE_STATUS.OK.toString()));
				log.info("password modify OK");				

			}

		} catch (EmailYetInTheSystemException e) {
			e.printStackTrace();
			log.error("Email inserted yet");
			fail();
		} catch (TestFailedException ex) {
			ex.printStackTrace();
			log.error("Test Exception");
			fail();
		} catch (Exception ex) {
			ex.printStackTrace();
			log.error("Generic Exception");
			fail();
		}
	}
	
	
	//------------REPLACE PASSWORD -----------------------

	@Test
	public void replacePasswordTest() {			
		CompanyDTO dto = fillCompanyDTO();	
		CompanyDTO dtoResult = null;
		Integer idUserInserted = null;

		try {
			log.info("Starting signUp...");
			dtoResult = (CompanyDTO) companyServicesInterface.signUp(dto);
			Session session = sessionFactory.getCurrentSession();
			session.getTransaction().commit();	

			if(dtoResult!=null) {
				log.info("signUp OK");
				idUserInserted=Integer.valueOf(dtoResult.getUsrId());
			}

			log.info("get data from DB...");
			//recover the User
			User userBefoceReplace = new User();
			userBefoceReplace = userDaoImpl.findByPrimaryKey(userBefoceReplace, idUserInserted);

			PasswordDTO passwordDTO = fillPasswordDTO(dto, dtoResult);
			SimpleResponseDTO response = invokeReplacePassword(passwordDTO);


			//delete data just inserted in DB		
			log.info("executing rollback...");
			rollBack(dtoResult, idUserInserted, session);
			log.info("rollback OK");

			assertTrue(response.getStatus().equals(Constants.RESPONSE_STATUS.OK.toString()));

		} catch (EmailYetInTheSystemException e) {
			e.printStackTrace();
			log.error("Email inserted yet");
			fail();
		} catch (TestFailedException ex) {
			ex.printStackTrace();
			log.error("Test Exception");
			fail();
		} catch (Exception ex) {
			ex.printStackTrace();
			log.error("Generic Exception");
			fail();
		}

	}
	
	
	@Test
	public void replacePasswordTest_invalidPassword_KO() {			
		CompanyDTO dto = fillCompanyDTO();	
		CompanyDTO dtoResult = null;
		Integer idUserInserted = null;

		try {
			log.info("Starting signUp...");
			dtoResult = (CompanyDTO) companyServicesInterface.signUp(dto);
			Session session = sessionFactory.getCurrentSession();
			session.getTransaction().commit();	

			if(dtoResult!=null) {
				log.info("signUp OK");
				idUserInserted=Integer.valueOf(dtoResult.getUsrId());
			}

			log.info("get data from DB...");
			//recover the User
			User userBefoceReplace = new User();
			userBefoceReplace = userDaoImpl.findByPrimaryKey(userBefoceReplace, idUserInserted);

			PasswordDTO passwordDTO = fillPasswordDTO(dto, dtoResult);			
			passwordDTO.setOldPsw("anotherPassword");
			SimpleResponseDTO response = invokeReplacePassword(passwordDTO);


			//delete data just inserted in DB		
			log.info("executing rollback...");
			rollBack(dtoResult, idUserInserted, session);
			log.info("rollback OK");

			assertTrue(response.getStatus().equals(Constants.RESPONSE_STATUS.OK.toString()));

		} catch (EmailYetInTheSystemException e) {
			e.printStackTrace();
			log.error("Email inserted yet");
			fail();
		} catch (TestFailedException ex) {
			ex.printStackTrace();
			log.error("Test Exception");
			fail();
		} catch (Exception ex) {
			ex.printStackTrace();
			log.error("Generic Exception");
			fail();
		}

	}





	//------------INVOKE METHODS -----------------------


	private SimpleResponseDTO invokeUserForgottenPasswordMail(UserSimpleDTO request, String profile) {
		SimpleResponseDTO responseDTO=null;
		try {
			Client client = configureClient();		
			logRequest(request,"invokeUserForgottenPasswordMail");		
			Response response  = client
					.target(CHANGE_PASSWORD_URI)
					.path(profile)
					.request(MediaType.APPLICATION_JSON)
					.post(Entity.entity(request, MediaType.APPLICATION_JSON));
			response.bufferEntity();
			responseDTO  =  response.readEntity(SimpleResponseDTO.class);
		} catch (Exception e) {
			log.error("Error Invoking User Rest Controller",e);
			e.printStackTrace();
		}
		return responseDTO;		
	}

	private SimpleResponseDTO invokeModifyPassword(UserSimpleDTO request, String resetCode) {
		SimpleResponseDTO responseDTO=null;
		try {
			Client client = configureClient();		
			logRequest(request,"invokeUserForgottenPasswordMail");		
			Response response  = client
					.target(MODIFY_PASSWORD_URI)
					.path(resetCode)
					.request(MediaType.APPLICATION_JSON)
					.post(Entity.entity(request, MediaType.APPLICATION_JSON));
			response.bufferEntity();
			responseDTO  =  response.readEntity(SimpleResponseDTO.class);
		} catch (Exception e) {
			log.error("Error Invoking User Rest Controller",e);
			e.printStackTrace();
		}
		return responseDTO;		
	}


	private SimpleResponseDTO invokeReplacePassword(PasswordDTO request) {
		SimpleResponseDTO responseDTO=null;
		try {
			Client client = configureClient();		
			logRequest(request,"invokeUserForgottenPasswordMail");		
			Response response  = client
					.target(REPLACE_PASSWORD_URI)
					.request(MediaType.APPLICATION_JSON)
					.post(Entity.entity(request, MediaType.APPLICATION_JSON));
			response.bufferEntity();
			responseDTO  =  response.readEntity(SimpleResponseDTO.class);
		} catch (Exception e) {
			log.error("Error Invoking User Rest Controller",e);
			e.printStackTrace();
		}
		return responseDTO;		
	}



	//------------UTILITIES-------------------------------------------


	private void rollBack(DTO dtoResult, Integer idUserInserted, Session session) {
		//delete the Company
		org.hibernate.Transaction txn1 = session.beginTransaction();
		Query query1 = session.createQuery("delete from Company where usr_id = " + idUserInserted);
		query1.executeUpdate();				
		txn1.commit();

		//delete the Transaction
		org.hibernate.Transaction txn2 = session.beginTransaction();
		Query query2 = session.createQuery("delete from Transaction where usr_id_da = " + idUserInserted);
		query2.executeUpdate();				
		txn2.commit();

		//delete the User
		org.hibernate.Transaction txn3 = session.beginTransaction();
		Query query3 = session.createQuery("delete from User where usr_id = " + idUserInserted);
		query3.executeUpdate();				
		txn3.commit();
	}

	private CompanyDTO fillCompanyDTO() {
		CompanyDTO dto = new CompanyDTO();
		dto.setUsrEmail("liga@gmail.com");
		dto.setUsrPassword("Liga.654");
		dto.setCpyPartitaIva("000PARTITAIVA000");
		dto.setCpyCodiceFiscale("000CODICEFISCALE000");
		dto.setCpyPec("s.liga@aktsrl.com");
		return dto;
	}


	private PasswordDTO fillPasswordDTO(CompanyDTO dto, CompanyDTO dtoResult) {
		PasswordDTO passwordDTO = new PasswordDTO();
		passwordDTO.setNewPsw("NewPassword123!");
		passwordDTO.setOldPsw(dto.getUsrPassword());
		passwordDTO.setUserId(dtoResult.getUsrId());
		passwordDTO.setUsrProfile("company");
		return passwordDTO;

	}



	private Client configureClient() {

		ClientConfig configuration = new ClientConfig();		
		Client client = ClientBuilder.newClient(configuration);
		HttpAuthenticationFeature feature;
		try {
			feature = HttpAuthenticationFeature.basic(GENERIC_USER_USERNAME, encrptingUtils.decrypt(GENERIC_USER_PASSWORD.trim()));
			client.register(feature);
			return client;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return client;
	}

	private void logRequest(Object request,String method) {
		ObjectMapper mapper = new ObjectMapper();
		String jsonString=null;
		try {
			jsonString = mapper.writeValueAsString(request);
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		log.debug("********************* User method {}Json string request is {}",method,jsonString);
	}
}
