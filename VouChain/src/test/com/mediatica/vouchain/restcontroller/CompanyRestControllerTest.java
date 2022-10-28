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
import com.mediatica.vouchain.entities.Company;
import com.mediatica.vouchain.entities.Transaction;
import com.mediatica.vouchain.entities.User;
import com.mediatica.vouchain.exceptions.EmailYetInTheSystemException;
import com.mediatica.vouchain.servicesInterface.CompanyServicesInterface;
import com.mediatica.vouchain.utilities.EncrptingUtils;

@Transactional
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("file:WebContent/WEB-INF/vouchain-servlet.xml")
@WebAppConfiguration
public class CompanyRestControllerTest {
	
	private static org.slf4j.Logger log = LoggerFactory.getLogger(CompanyRestControllerTest.class);
	
	@Autowired
    private EncrptingUtils encrptingUtils;
	
	@Autowired
	CompanyServicesInterface companyServicesInterface;
	
	@Autowired
	UserDaoImpl userDaoImpl;

	@Autowired
	CompanyDaoImpl companyDaoImpl;

	@Autowired
	TransactionDaoImpl transactionDaoImpl;
	
	@Autowired
	SessionFactory sessionFactory;	
	
	@Value("${company_auth_username}")
	private String COMPANY_USERNAME;
	
	@Value("${company_auth_password}")
	private String COMPANY_PASSWORD;
	
	private String COMPANY_LOGIN_URI="http://localhost:8080/VouChain/api/companies/companyLogin";
	
	private String COMPANY_SIGNUP_URI="http://localhost:8080/VouChain/api/companies/companySignUp";
	
	private String SHOW_COMPANY_PROFILE_URI="http://localhost:8080/VouChain/api/companies/showCompanyProfile";
	
	private String MODIFIY_COMPANY_PROFILE_URI="http://localhost:8080/VouChain/api/companies/modCompanyProfile";

	
	


	//---------COMPANY LOGIN--------------------------
	@Test
	public void companyLoginTest() {
		CompanyDTO dto = fillCompanyDTO();	
		CompanyDTO dtoResult = null;
		Integer idUserInserted = null;
		
		try {
			log.info("Statring signUp...");
			dtoResult = (CompanyDTO) companyServicesInterface.signUp(dto);
			Session session = sessionFactory.getCurrentSession();
			session.getTransaction().commit();	

			if(dtoResult!=null) {
				log.info("signUp OK");

				//login
				DTO dtoFromLogin = invokeCompanyLogin(dto);

				//delete data just inserted in DB
				idUserInserted=Integer.valueOf(dtoResult.getUsrId());
				log.info("executing rollback...");
				rollBack(dtoResult, idUserInserted, session);
				log.info("rollback OK");

				//check login
				log.info("assert login...");
				assertTrue(dtoFromLogin!=null && dtoFromLogin.getUsrId()!=null);		
				log.info("login OK");

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
	
	//TEST KO: try to login with a not existing email
	@Test
	public void loginTest_notExistingMail_KO() {	

		CompanyDTO dto = fillCompanyDTO();	
		CompanyDTO dtoResult = null;
		Integer idUserInserted = null;

		try {
			log.info("Statring signUp...");
			dtoResult = (CompanyDTO) companyServicesInterface.signUp(dto);
			Session session = sessionFactory.getCurrentSession();
			session.getTransaction().commit();	

			if(dtoResult!=null) {
				log.info("signUp OK");
				
				//try login with no existing email
				dto.setUsrEmail("notExistingEmail@gmail.com");
				DTO dtoFromLogin = invokeCompanyLogin(dto);
				
				//delete data just inserted in DB
				idUserInserted=Integer.valueOf(dtoResult.getUsrId());
				log.info("executing rollback...");
				rollBack(dtoResult, idUserInserted, session);
				log.info("rollback OK");

				//check login
				log.info("assert login...");
				assertTrue(dtoFromLogin!=null && dtoFromLogin.getUsrId()!=null);		
				log.info("login OK");
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
	
	
	//------------SIGUNP----------------------------------------

	@Test
	public void signUpTest() {			
		CompanyDTO dto = fillCompanyDTO();	
		CompanyDTO dtoResult = null;
		Integer idUserInserted = null;

		try {
			log.info("Starting signUp...");
			
			dtoResult = invokeCompanySignUp(dto);
			
			//dtoResult = (CompanyDTO) companyServicesInterface.signUp(dto);
			Session session = sessionFactory.getCurrentSession();
			session.getTransaction().commit();	

			if(dtoResult!=null) {
				log.info("signUp OK");
				idUserInserted=Integer.valueOf(dtoResult.getUsrId());
			}

			log.info("get data from DB...");
			//recover the User
			User user = new User();
			user = userDaoImpl.findByPrimaryKey(user, idUserInserted);				
			//recover the Company
			Company company = new Company();
			company = companyDaoImpl.findByPrimaryKey(company, idUserInserted);					
			//recover the transaction
			Transaction transaction = new Transaction();
			transaction = transactionDaoImpl.getLastTransaction(idUserInserted);	

			//delete data just inserted in DB		
			log.info("executing rollback...");
			rollBack(dtoResult, idUserInserted, session);
			log.info("rollback OK");
			
			//check user insert
			log.info("assert User insert...");
			assertTrue(user.getUsrId()!=null);		
			log.info("User insert OK");

			//check company insert
			log.info("assert Company insert...");
			assertTrue(company.getUsrId()!=null);
			log.info("Company insert OK");

			//check blockchain call (fields: usr_bch_address, usr_private_key)
			log.info("assert calling Blockchain...");
			assertTrue(user.getUsrBchAddress()!=null && user.getUsrPrivateKey()!=null);	
			log.info("calling Blockchain OK");

			if(transaction!=null) {
				//check transaction insert  
				log.info("assert Transaction insert...");
				assertTrue(transaction.getTrcId()!=null);	
				log.info("Transaction insert OK");

				//check trc_tx_id
				log.info("assert trc_tx_id...");
				assertTrue(transaction.getTrcTxId()!=null);	
				log.info("trc_tx_id OK");

				//check email
				log.info("assert sending email...");
				assertTrue(transaction.getTrcMailSent());
				log.info("sending email OK");
			}


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
	
	//TEST KO: try to insert User without email
	@Test
	public void signUpTest_noMail_KO() {			

		//Fill dto without email
		CompanyDTO dto = fillCompanyDTO();	
		CompanyDTO dtoResult = null;
		Integer idUserInserted = null;

		try {
			log.info("Statring signUp...");
			
			//try signup without email
			dto.setUsrEmail(null);
			dtoResult = invokeCompanySignUp(dto);
			Session session = sessionFactory.getCurrentSession();
			session.getTransaction().commit();	
			
			assertTrue(dtoResult!=null && dtoResult.getUsrId()!=null);
			
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
	
	
	//------------SHOW PROFILE--------------------------------------------

		@Test
		public void showProfileTest() {	

			CompanyDTO dto = fillCompanyDTO();	
			CompanyDTO dtoResult = null;
			Integer idUserInserted = null;

			try {
				log.info("Statring signUp...");
				dtoResult = (CompanyDTO) companyServicesInterface.signUp(dto);
				Session session = sessionFactory.getCurrentSession();
				session.getTransaction().commit();	

				if(dtoResult!=null) {
					log.info("signUp OK");
					
					//showProfile
					DTO profile = invokeShowCompanyProfile(dtoResult.getUsrId());

					//delete data just inserted in DB
					idUserInserted=Integer.valueOf(dtoResult.getUsrId());
					log.info("executing rollback...");
					rollBack(dtoResult, idUserInserted, session);
					log.info("rollback OK");


					//check showProfile outpu
					log.info("assert show profile...");
					assertTrue(profile!=null && profile.getUsrId()!=null);		
					log.info("show profile OK");
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
		
		
		//TEST KO: try to show proile with a not existing user id
		@Test
		public void showProfileTest_NotExistingID_KO() {	

			CompanyDTO dto = fillCompanyDTO();	
			CompanyDTO dtoResult = null;
			Integer idUserInserted = null;

			try {
				log.info("Statring signUp...");
				dtoResult = (CompanyDTO) companyServicesInterface.signUp(dto);
				Session session = sessionFactory.getCurrentSession();
				session.getTransaction().commit();	

				if(dtoResult!=null) {
					log.info("signUp OK");
					
					//try showProfile with no existing user id
					DTO profile = invokeShowCompanyProfile("9999");

					//delete data just inserted in DB
					idUserInserted=Integer.valueOf(dtoResult.getUsrId());
					log.info("executing rollback...");
					rollBack(dtoResult, idUserInserted, session);
					log.info("rollback OK");


					//check showProfile outpu
					log.info("assert show profile...");
					assertTrue(profile!=null && profile.getUsrId()!=null && profile.getStatus().equals(Constants.RESPONSE_STATUS.OK.toString()));		
					log.info("show profile OK");
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
		
		//------------MOD PROFILE--------------------------------------------

		@Test
		public void modProfileTest() {	

			CompanyDTO dtoBeforeModify = fillCompanyDTO();	
			Integer idUserInserted = null;

			try {
				log.info("Statring signUp...");
				dtoBeforeModify = (CompanyDTO) companyServicesInterface.signUp(dtoBeforeModify);
				Session session = sessionFactory.getCurrentSession();
				session.getTransaction().commit();	

				if(dtoBeforeModify!=null) {
					log.info("signUp OK");

					//change company data
					CompanyDTO modifiedCompany = fillCompanyForModProfile(dtoBeforeModify.getUsrId());

					//update company 
					CompanyDTO dtoAfterModify = invokeModCompanyProfile(modifiedCompany);
					
					//delete data just inserted in DB
					idUserInserted=Integer.valueOf(dtoAfterModify.getUsrId());
					log.info("executing rollback...");
					rollBack(dtoAfterModify, idUserInserted, session);
					log.info("rollback OK");

					//check company update
					log.info("assert modProfile...");
					assertTrue(dtoAfterModify!=null && dtoAfterModify.getUsrId()!=null);
					assertTrue(!dtoBeforeModify.getCpyCodiceFiscale().equals(dtoAfterModify.getCpyCodiceFiscale()));		

					log.info("modProfile OK");

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
		
		
		//TEST KO: try to modify profile without PEC
		@Test
		public void modProfileTest_nullPEC_KO() {	


			CompanyDTO dtoBeforeModify = fillCompanyDTO();	
			Integer idUserInserted = null;

			try {
				log.info("Statring signUp...");
				dtoBeforeModify = (CompanyDTO) companyServicesInterface.signUp(dtoBeforeModify);
				Session session = sessionFactory.getCurrentSession();
				session.getTransaction().commit();	

				if(dtoBeforeModify!=null) {
					log.info("signUp OK");

					//change company data
					CompanyDTO modifiedCompany = fillCompanyForModProfile(dtoBeforeModify.getUsrId());

					//update company 
					modifiedCompany.setCpyPec(null);				
					CompanyDTO dtoAfterModify = invokeModCompanyProfile(modifiedCompany);
					
					//delete data just inserted in DB
					idUserInserted=Integer.valueOf(modifiedCompany.getUsrId());
					log.info("executing rollback...");
					rollBack(dtoAfterModify, idUserInserted, session);
					log.info("rollback OK");

					//check company update
					log.info("assert modProfile...");
					assertTrue(dtoAfterModify!=null && dtoAfterModify.getUsrId()!=null && dtoAfterModify.getStatus().equals(Constants.RESPONSE_STATUS.OK.toString()));
					assertTrue(!dtoBeforeModify.getCpyCodiceFiscale().equals(dtoAfterModify.getCpyCodiceFiscale()));		

					log.info("modProfile OK");

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
	
	
	//-----------------INVOKE METHODS--------------------
	
	private CompanyDTO invokeCompanyLogin(CompanyDTO request) {
		CompanyDTO responseDTO=null;
		try {
			Client client = configureClient();		
			logRequest(request,"invokeCompanyLogin");		
			Response response  = client
			      .target(COMPANY_LOGIN_URI)
			      .request(MediaType.APPLICATION_JSON)
			      .post(Entity.entity(request, MediaType.APPLICATION_JSON));
			response.bufferEntity();
			responseDTO  =  response.readEntity(CompanyDTO.class);
		} catch (Exception e) {
			log.error("Error Invoking Company Rest Controller",e);
			e.printStackTrace();
		}
    	return responseDTO;		
	}
	
	private CompanyDTO invokeCompanySignUp(CompanyDTO request) {
		CompanyDTO responseDTO=null;
		try {
			Client client = configureClient();		
			logRequest(request,"invokeCompanySignUp");		
			Response response  = client
			      .target(COMPANY_SIGNUP_URI)
			      .request(MediaType.APPLICATION_JSON)
			      .post(Entity.entity(request, MediaType.APPLICATION_JSON));
			response.bufferEntity();
			responseDTO  =  response.readEntity(CompanyDTO.class);
		} catch (Exception e) {
			log.error("Error Invoking Company Rest Controller",e);
			e.printStackTrace();
		}
    	return responseDTO;		
	}
	
	private CompanyDTO invokeShowCompanyProfile(String request) {
		CompanyDTO responseDTO=null;
		try {
			Client client = configureClient();		
			logRequest(request,"invokeCompanySignUp");		
			Response response  = client
			      .target(SHOW_COMPANY_PROFILE_URI)
			      .path(request)
			      .request(MediaType.APPLICATION_JSON)
			      .get();
			response.bufferEntity();
			responseDTO  =  response.readEntity(CompanyDTO.class);
		} catch (Exception e) {
			log.error("Error Invoking Company Rest Controller",e);
			e.printStackTrace();
		}
    	return responseDTO;		
	}
	
	private CompanyDTO invokeModCompanyProfile(CompanyDTO request) {
		CompanyDTO responseDTO=null;
		try {
			Client client = configureClient();		
			logRequest(request,"invokeCompanyLogin");		
			Response response  = client
			      .target(MODIFIY_COMPANY_PROFILE_URI)
			      .request(MediaType.APPLICATION_JSON)
			      .post(Entity.entity(request, MediaType.APPLICATION_JSON));
			response.bufferEntity();
			responseDTO  =  response.readEntity(CompanyDTO.class);
		} catch (Exception e) {
			log.error("Error Invoking Company Rest Controller",e);
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
		dto.setUsrEmail("stefanoliga12@libero.it");
		dto.setUsrPassword("Liga.654");
		dto.setCpyPartitaIva("000PARTITAIVA000");
		dto.setCpyCodiceFiscale("000CODICEFISCALE000");
		dto.setCpyPec("stefanoliga12@libero.it");
		return dto;
	}
	
	private CompanyDTO fillCompanyForModProfile(String usrID) {
		CompanyDTO dto = new CompanyDTO();

		dto.setUsrId(usrID);
		dto.setUsrEmail("nuovaMail@gmail.com");
		dto.setUsrPassword("nuovaPassword");

		dto.setCpyCodiceFiscale("111CODICEFISCALE111");
		dto.setCpyPartitaIva("111PARTITAIVA111");
		dto.setCpyPec("nuovaPEC@gmail.com");
		dto.setCpyCuu("abcdef");
		dto.setCpyFirstNameRef("Mario");
		dto.setCpyLastNameRef("Rossi");
		dto.setCpyPhoneNoRef("348999595");

		dto.setCpyRagioneSociale("ragioneSociale");
		dto.setCpyAddress("via del campo");
		dto.setCpyCity("Roma");
		dto.setCpyProv("RM");
		dto.setCpyZip("00158");

		return dto;

	}
	
	private Client configureClient() {
		
		ClientConfig configuration = new ClientConfig();		
		Client client = ClientBuilder.newClient(configuration);
    	HttpAuthenticationFeature feature;
		try {
			feature = HttpAuthenticationFeature.basic(COMPANY_USERNAME, encrptingUtils.decrypt(COMPANY_PASSWORD.trim()));
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
	    log.debug("********************* Company method {}Json string request is {}",method,jsonString);
	}
}
