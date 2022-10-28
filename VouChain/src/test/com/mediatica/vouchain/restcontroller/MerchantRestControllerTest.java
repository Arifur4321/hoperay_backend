package test.com.mediatica.vouchain.restcontroller;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.List;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.GenericType;
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
import com.mediatica.vouchain.dao.MerchantDaoImpl;
import com.mediatica.vouchain.dao.TransactionDaoImpl;
import com.mediatica.vouchain.dao.UserDaoImpl;
import com.mediatica.vouchain.dto.DTO;
import com.mediatica.vouchain.dto.DTOList;
import com.mediatica.vouchain.dto.EmployeeDTO;
import com.mediatica.vouchain.dto.MerchantDTO;
import com.mediatica.vouchain.entities.Merchant;
import com.mediatica.vouchain.entities.Transaction;
import com.mediatica.vouchain.entities.User;
import com.mediatica.vouchain.exceptions.EmailYetInTheSystemException;
import com.mediatica.vouchain.servicesImpl.MerchantServiceImpl;
import com.mediatica.vouchain.utilities.EncrptingUtils;

@Transactional
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("file:WebContent/WEB-INF/vouchain-servlet.xml")
@WebAppConfiguration
public class MerchantRestControllerTest {
	
	private static org.slf4j.Logger log = LoggerFactory.getLogger(MerchantRestControllerTest.class);
	
	@Autowired
	SessionFactory sessionFactory;
	
	@Autowired
	MerchantServiceImpl merchantServiceImpl;
	
	@Autowired
	MerchantDaoImpl merchantDaoImpl;

	@Autowired
	TransactionDaoImpl transactionDaoImpl;

	@Autowired
	UserDaoImpl userDaoImpl;
	
	@Autowired
	private EncrptingUtils encrptingUtils;
	
	@Value("${merchant_auth_username}")
	private String MERCHANT_USERNAME;
	
	@Value("${merchant_auth_password}")
	private String MERCHANT_PASSWORD;
	
	private String MERCHANT_LOGIN_URI="http://localhost:8080/VouChain/api/merchants/merchantLogin";
	
	private String SHOW_MERCHANT_PROFILE_URI="http://localhost:8080/VouChain/api/merchants/showMerchantProfile";

	private String MERCHANT_SIGNUP_URI="http://localhost:8080/VouChain/api/merchants/merchantSignUp";

	private String SHOW_MERCHANT_LIST_URI="http://localhost:8080/VouChain/api/merchants/showMerchantsList";

	private String MODIFY_MERCHANT_PROFILE_URI="http://localhost:8080/VouChain/api/merchants/modMerchantProfile";
	
	
	
	@Test
	public void loginTest() {	

		MerchantDTO dto = fillMerchantDTO();	
		MerchantDTO dtoResult = null;
		Integer idUserInserted = null;

		try {
			log.info("Statring signUp...");
			dtoResult = (MerchantDTO) merchantServiceImpl.signUp(dto);
			Session session = sessionFactory.getCurrentSession();
			session.getTransaction().commit();	

			if(dtoResult!=null) {
				log.info("signUp OK");
				
				dtoResult.setUsrPassword(dto.getUsrPassword());
				
				//login
				DTO dtoFromLogin = invokeMerchantLogin(dtoResult);

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
	
	//TEST KO: try to login with not existing mail
	@Test
	public void loginTest_notExistingMail_KO() {	

		MerchantDTO dto = fillMerchantDTO();	
		MerchantDTO dtoResult = null;
		Integer idUserInserted = null;

		try {
			log.info("Statring signUp...");
			dtoResult = (MerchantDTO) merchantServiceImpl.signUp(dto);
			Session session = sessionFactory.getCurrentSession();
			session.getTransaction().commit();	

			if(dtoResult!=null) {
				log.info("signUp OK");
				
				dtoResult.setUsrPassword(dto.getUsrPassword());
				dtoResult.setUsrEmail("NotExistingMail@gmail.com");

				
				//login
				DTO dtoFromLogin = invokeMerchantLogin(dtoResult);

				//delete data just inserted in DB
				idUserInserted=Integer.valueOf(dtoResult.getUsrId());
				log.info("executing rollback...");
				rollBack(dtoResult, idUserInserted, session);
				log.info("rollback OK");

				//check login
				log.info("assert login...");
				assertTrue(dtoFromLogin!=null && dtoFromLogin.getUsrId()!=null && dtoFromLogin.getStatus().equals(Constants.RESPONSE_STATUS.OK.toString()));		
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
	
	
	//------------SHOW PROFILE--------------------------------------------

	@Test
	public void showProfileTest() {	

		MerchantDTO dto = fillMerchantDTO();	
		MerchantDTO dtoResult = null;
		Integer idUserInserted = null;

		try {
			log.info("Statring signUp...");
			dtoResult = (MerchantDTO) merchantServiceImpl.signUp(dto);
			Session session = sessionFactory.getCurrentSession();
			session.getTransaction().commit();	

			if(dtoResult!=null) {
				log.info("signUp OK");
				
				//showProfile
				DTO profile = invokeShowMerchantProfile(dtoResult.getUsrId());

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
	
	//TEST KO: try to show profile with a not existing user id
	@Test
	public void showProfileTest_NotExistingID_KO() {

		MerchantDTO dto = fillMerchantDTO();	
		MerchantDTO dtoResult = null;
		Integer idUserInserted = null;

		try {
			log.info("Statring signUp...");
			dtoResult = (MerchantDTO) merchantServiceImpl.signUp(dto);
			Session session = sessionFactory.getCurrentSession();
			session.getTransaction().commit();	

			if(dtoResult!=null) {
				log.info("signUp OK");
				
				//showProfile
				DTO profile = invokeShowMerchantProfile("9999");

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
	
	
	
	
	//------------SIGUNP----------------------------------------

			@Test
			public void signUpTest() {			
				MerchantDTO dto = fillMerchantDTO();	
				MerchantDTO dtoResult = null;
				Integer idUserInserted = null;

				try {
					log.info("Starting signUp...");					
					
					dtoResult = invokeMerchantSignUp(dto);					
					
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
					//recover the Merchant
					Merchant merchant = new Merchant();
					merchant = merchantDaoImpl.findByPrimaryKey(merchant, idUserInserted);					
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
					
					//check merchant insert
					log.info("assert Merchant insert...");
					assertTrue(merchant.getUsrId()!=null);		
					log.info("Merchant insert OK");
					
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
			
			//TEST KO: try to sigup with not valid email
			@Test
			public void signUpTest_notValidEmail_KO() {			
				MerchantDTO dto = fillMerchantDTO();	
				MerchantDTO dtoResult = null;
				Integer idUserInserted = null;

				try {
					log.info("Starting signUp...");
					
					dto.setUsrEmail("NOT_VALID_EMAIL");
					dtoResult = invokeMerchantSignUp(dto);						
					
					Session session = sessionFactory.getCurrentSession();
					session.getTransaction().commit();	
					
					//check user insert
					log.info("assert User insert...");
					assertTrue(dtoResult!= null && dtoResult.getUsrId()!=null && dtoResult.getStatus().equals(Constants.RESPONSE_STATUS.OK.toString()));		
					log.info("User insert OK");

					
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
			
			
			
			//------------GET MERCHANT LIST--------------------------------------------


			@Test
			public void getMerchantListTest() {	

				MerchantDTO dto = fillMerchantDTO();	
				MerchantDTO dtoResult = null;
				Integer idUserInserted = null;

				try {
					log.info("Starting signUp...");
					dtoResult = (MerchantDTO) merchantServiceImpl.signUp(dto);
					Session session = sessionFactory.getCurrentSession();
					session.getTransaction().commit();	

					if(dtoResult!=null) {
						log.info("signUp OK");

						//get merchant list
						DTOList<MerchantDTO> dtoList = invokeShowMerchantList();

						//delete data just inserted in DB
						idUserInserted=Integer.valueOf(dtoResult.getUsrId());
						log.info("executing rollback...");
						rollBack(dtoResult, idUserInserted, session);
						log.info("rollback OK");

						//check get merchant list
						log.info("assert merchant list...");
						assertTrue(dtoList!=null && dtoList.getList()!=null && dtoList.getList().size()>0);		
						log.info("merchant list OK");

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

				MerchantDTO dtoBeforeModify = fillMerchantDTO();	
				Integer idUserInserted = null;

				try {
					log.info("Statring signUp...");
					dtoBeforeModify = (MerchantDTO) merchantServiceImpl.signUp(dtoBeforeModify);
					Session session = sessionFactory.getCurrentSession();
					session.getTransaction().commit();	

					if(dtoBeforeModify!=null) {
						log.info("signUp OK");

						//change merchant data
						MerchantDTO modifiedMerchant = fillMerchantForModProfile(dtoBeforeModify.getUsrId());

						//update merchant 
						MerchantDTO dtoAfterModify = invokeModMerchantProfile(modifiedMerchant);


						//delete data just inserted in DB
						idUserInserted=Integer.valueOf(dtoAfterModify.getUsrId());
						log.info("executing rollback...");
						rollBack(dtoAfterModify, idUserInserted, session);
						log.info("rollback OK");

						//check c update
						log.info("assert modProfile...");
						assertTrue(dtoAfterModify!=null && dtoAfterModify.getUsrId()!=null);
						assertTrue(!dtoBeforeModify.getMrcAddress().equals(dtoAfterModify.getMrcAddress()));		

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
			
			//TEST KO: try to modify profile with an invalid email
			@Test
			public void modProfileTest_invalidEmail_KO() {	

				MerchantDTO dtoBeforeModify = fillMerchantDTO();	
				Integer idUserInserted = null;

				try {
					log.info("Statring signUp...");
					dtoBeforeModify = (MerchantDTO) merchantServiceImpl.signUp(dtoBeforeModify);
					Session session = sessionFactory.getCurrentSession();
					session.getTransaction().commit();	

					if(dtoBeforeModify!=null) {
						log.info("signUp OK");

						//change merchant data
						MerchantDTO modifiedMerchant = fillMerchantForModProfile(dtoBeforeModify.getUsrId());

						//update merchant 
						modifiedMerchant.setUsrEmail("INVALID_EMAIL");
						MerchantDTO dtoAfterModify = invokeModMerchantProfile(modifiedMerchant);


						//delete data just inserted in DB
						idUserInserted=Integer.valueOf(modifiedMerchant.getUsrId());
						log.info("executing rollback...");
						rollBack(dtoAfterModify, idUserInserted, session);
						log.info("rollback OK");

						//check c update
						log.info("assert modProfile...");
						assertTrue(dtoAfterModify!=null && dtoAfterModify.getUsrId()!=null && dtoAfterModify.getStatus().equals(Constants.RESPONSE_STATUS.OK.toString()));
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

	private MerchantDTO invokeMerchantLogin(MerchantDTO request) {
		MerchantDTO responseDTO=null;
		try {
			Client client = configureClient();		
			logRequest(request,"invokeMerchantLogin");		
			Response response  = client
					.target(MERCHANT_LOGIN_URI)
					.request(MediaType.APPLICATION_JSON)
					.post(Entity.entity(request, MediaType.APPLICATION_JSON));
			response.bufferEntity();
			responseDTO  =  response.readEntity(MerchantDTO.class);
		} catch (Exception e) {
			log.error("Error Invoking Merchant Rest Controller",e);
			e.printStackTrace();
		}
		return responseDTO;		
	}
	
	private MerchantDTO invokeShowMerchantProfile(String request) {
		MerchantDTO responseDTO=null;
		try {
			Client client = configureClient();		
			logRequest(request,"invokeShowMerchantProfile");		
			Response response  = client
					.target(SHOW_MERCHANT_PROFILE_URI)
					.path(request)
					.request(MediaType.APPLICATION_JSON)
					.get();
			response.bufferEntity();
			responseDTO  =  response.readEntity(MerchantDTO.class);
		} catch (Exception e) {
			log.error("Error Invoking Merchant Rest Controller",e);
			e.printStackTrace();
		}
		return responseDTO;		
	}
	
	private MerchantDTO invokeMerchantSignUp(MerchantDTO request) {
		MerchantDTO responseDTO=null;
		try {
			Client client = configureClient();		
			logRequest(request,"invokeMerchantSignUp");		
			Response response  = client
					.target(MERCHANT_SIGNUP_URI)
					.request(MediaType.APPLICATION_JSON)
					.post(Entity.entity(request, MediaType.APPLICATION_JSON));
			response.bufferEntity();
			responseDTO  =  response.readEntity(MerchantDTO.class);
		} catch (Exception e) {
			log.error("Error Invoking Merchant Rest Controller",e);
			e.printStackTrace();
		}
		return responseDTO;		
	}
	
	private DTOList<MerchantDTO> invokeShowMerchantList() {
		DTOList<MerchantDTO> responseDTO=null;
		try {
			Client client = configureClient();		
			Response response  = client
					.target(SHOW_MERCHANT_LIST_URI)
					.request(MediaType.APPLICATION_JSON)
					.get();
			response.bufferEntity();
			responseDTO  =  response.readEntity(new GenericType<DTOList<MerchantDTO>>() {});
		} catch (Exception e) {
			log.error("Error Invoking Employee Rest Controller",e);
			e.printStackTrace();
		}
		return responseDTO;		
	}
	
	private MerchantDTO invokeModMerchantProfile(MerchantDTO request) {
		MerchantDTO responseDTO=null;
		try {
			Client client = configureClient();		
			logRequest(request,"invokeModMerchantProfile");		
			Response response  = client
					.target(MODIFY_MERCHANT_PROFILE_URI)
					.request(MediaType.APPLICATION_JSON)
					.post(Entity.entity(request, MediaType.APPLICATION_JSON));
			response.bufferEntity();
			responseDTO  =  response.readEntity(MerchantDTO.class);
		} catch (Exception e) {
			log.error("Error Invoking Merchant Rest Controller",e);
			e.printStackTrace();
		}
		return responseDTO;		
	}
	
	
	//------------------UTILITIES-----------------------
	
	private void rollBack(DTO dtoResult, Integer idUserInserted, Session session) {
		
		//delete the Transaction
		org.hibernate.Transaction txn2 = session.beginTransaction();
		Query query2 = session.createQuery("delete from Transaction where usr_id_da = " + idUserInserted + " or usr_id_a = " + idUserInserted);
		query2.executeUpdate();				
		txn2.commit();
		
		//delete the Merchant
		org.hibernate.Transaction txn1 = session.beginTransaction();
		Query query1 = session.createQuery("delete from Merchant where usr_id = " + idUserInserted);
		query1.executeUpdate();				
		txn1.commit();

		//delete the User
		org.hibernate.Transaction txn3 = session.beginTransaction();
		Query query3 = session.createQuery("delete from User where usr_id = " + idUserInserted);
		query3.executeUpdate();				
		txn3.commit();
	}
	
	private MerchantDTO fillMerchantDTO() {
		MerchantDTO dto = new MerchantDTO();
		dto.setUsrEmail("stefanoliga12@libero.it");
		dto.setUsrPassword("Merchant.654");
		dto.setMrcAddress("via del corso 12");
		dto.setMrcBank("ING direct");
		dto.setMrcCity("Roma");
		dto.setMrcCodiceFiscale("123CodFiscale123");
		dto.setMrcFirstNameRef("Ciro");
		dto.setMrcLastNameRef("Rossi");
		return dto;
	}
	
	private MerchantDTO fillMerchantForModProfile(String usrId) {
		MerchantDTO dto = new MerchantDTO();
		dto.setUsrId(usrId);
		dto.setUsrEmail("nuovaMail@aktsrl.com");
		dto.setUsrPassword("NuovaPassword.654");
		dto.setMrcAddress("via del campo 13");
		dto.setMrcBank("Unicredit");
		dto.setMrcCity("Palermo");
		dto.setMrcCodiceFiscale("456CodFiscale456");
		dto.setMrcFirstNameRef("Mario");
		dto.setMrcLastNameRef("Gialli");
		return dto;
	}

	private Client configureClient() {

		ClientConfig configuration = new ClientConfig();		
		Client client = ClientBuilder.newClient(configuration);
		HttpAuthenticationFeature feature;
		try {
			feature = HttpAuthenticationFeature.basic(MERCHANT_USERNAME, encrptingUtils.decrypt(MERCHANT_PASSWORD.trim()));
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
		log.debug("********************* Merchant method {}Json string request is {}",method,jsonString);
	}



}
