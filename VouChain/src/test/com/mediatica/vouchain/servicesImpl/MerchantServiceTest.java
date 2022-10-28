package test.com.mediatica.vouchain.servicesImpl;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.List;

import org.bouncycastle.util.test.TestFailedException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.transaction.annotation.Transactional;

import com.mediatica.vouchain.dao.MerchantDaoImpl;
import com.mediatica.vouchain.dao.TransactionDaoImpl;
import com.mediatica.vouchain.dao.UserDaoImpl;
import com.mediatica.vouchain.dto.DTO;
import com.mediatica.vouchain.dto.MerchantDTO;
import com.mediatica.vouchain.entities.Merchant;
import com.mediatica.vouchain.entities.Transaction;
import com.mediatica.vouchain.entities.User;
import com.mediatica.vouchain.exceptions.EmailYetInTheSystemException;
import com.mediatica.vouchain.servicesImpl.MerchantServiceImpl;

@Transactional
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("file:WebContent/WEB-INF/vouchain-servlet.xml")
@WebAppConfiguration
public class MerchantServiceTest {
	
	@Autowired
	SessionFactory sessionFactory;	
	
	@Autowired
	MerchantServiceImpl merchantServiceImpl;
	
	@Autowired
	UserDaoImpl userDaoImpl;
	
	@Autowired
	MerchantDaoImpl merchantDaoImpl;

	@Autowired
	TransactionDaoImpl transactionDaoImpl;

	
	private static org.slf4j.Logger log = LoggerFactory.getLogger(MerchantServiceImpl.class);

//------------SIGUNP----------------------------------------

		@Test
		public void signUpTest() {			
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
		
		//TEST KO: try to insert User without email
		@Test
		public void signUpTest_insertUser_noMail_KO() {			

			//Fill dto without email
			MerchantDTO dto = fillMerchantDTO_NoMail();	
			MerchantDTO dtoResult = null;

			try {
				log.info("Statring signUp...");
				
				//try signup without email
				dtoResult = (MerchantDTO) merchantServiceImpl.signUp(dto);
				Session session = sessionFactory.getCurrentSession();
				session.getTransaction().commit();	
				
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
					org.hibernate.Transaction txn = session.beginTransaction();
					MerchantDTO dtoAfterModify = (MerchantDTO) merchantServiceImpl.modProfile(modifiedMerchant);
					txn.commit();


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


		//TEST KO: try to modify profile with null ID
		@Test
		public void modProfileTest_nullID_KO() {	

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

					//ID null
					modifiedMerchant.setUsrId(null);				

					try {
						//update merchant 
						MerchantDTO dtoAfterModify;
						org.hibernate.Transaction txn = session.beginTransaction();
						dtoAfterModify = (MerchantDTO) merchantServiceImpl.modProfile(modifiedMerchant);
						txn.commit();
						
					} catch (Exception e) {
						session.getTransaction().rollback();
						idUserInserted=Integer.valueOf(dtoBeforeModify.getUsrId());
						log.info("executing rollback...");
						rollBack(dtoBeforeModify, idUserInserted, session);
						log.info("rollback OK");					
						
						e.printStackTrace();
						fail();
					}

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
			}catch (Exception ex) {
				ex.printStackTrace();
				log.error("Generic Exception");
				fail();
			}

		}
		
		
//------------LOGIN--------------------------------------------

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

						//login
						DTO dtoFromLogin = merchantServiceImpl.login(dtoResult.getUsrEmail());

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
						
						//try login with no existing email
						DTO dtoFromLogin = merchantServiceImpl.login("notExistingMail@gmail.com");

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
						DTO profile = merchantServiceImpl.showProfile(dtoResult.getUsrId());

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
						
						//try showProfile with no existing user id
						DTO profile = merchantServiceImpl.showProfile("9999");

						//delete data just inserted in DB
						idUserInserted=Integer.valueOf(dtoResult.getUsrId());
						log.info("executing rollback...");
						rollBack(dtoResult, idUserInserted, session);
						log.info("rollback OK");


						//check showProfile output
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
						List<MerchantDTO> dtoList = merchantServiceImpl.getMerchantsList();

						//delete data just inserted in DB
						idUserInserted=Integer.valueOf(dtoResult.getUsrId());
						log.info("executing rollback...");
						rollBack(dtoResult, idUserInserted, session);
						log.info("rollback OK");

						//check get merchant list
						log.info("assert merchant list...");
						assertTrue(dtoList!=null && dtoList.size()>0);		
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
			

//------------UTILITIES-------------------------------------------


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
		
		private MerchantDTO fillMerchantDTO_NoMail() {
			MerchantDTO dto = new MerchantDTO();
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

}
