package test.com.mediatica.vouchain.dao;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.text.ParseException;
import java.util.Date;
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

import com.mediatica.vouchain.config.Constants;
import com.mediatica.vouchain.dao.UserDaoImpl;
import com.mediatica.vouchain.dto.CompanyDTO;
import com.mediatica.vouchain.dto.DTO;
import com.mediatica.vouchain.entities.User;
import com.mediatica.vouchain.exceptions.EmailYetInTheSystemException;
import com.mediatica.vouchain.servicesImpl.UserServiceImpl;
import com.mediatica.vouchain.servicesInterface.CompanyServicesInterface;

@Transactional
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("file:WebContent/WEB-INF/vouchain-servlet.xml")
@WebAppConfiguration
public class UserDaoTest {
	
	private static org.slf4j.Logger log = LoggerFactory.getLogger(UserDaoTest.class);
	
	@Autowired
	SessionFactory sessionFactory;	
	
	@Autowired
	CompanyServicesInterface companyServicesInterface;
	
	@Autowired
	UserServiceImpl userServiceImpl;
	
	@Autowired
	UserDaoImpl userDaoImpl;
	

	
	//--------------FIND USER BY EMAIL AND PASSWORD----------------
	@Test
	public void findUserByEmailPasswordTest() {	

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
				idUserInserted=Integer.valueOf(dtoResult.getUsrId());

				
				User userByID = new User();
				userByID = userDaoImpl.findByPrimaryKey(userByID, idUserInserted);				
				User user = userDaoImpl.findUserByEmailPassword(userByID.getUsrEmail(), userByID.getUsrPassword());

				//delete data just inserted in DB
				log.info("executing rollback...");
				rollBack(dtoResult, idUserInserted, session);
				log.info("rollback OK");


				//check showProfile outpu
				log.info("assert show profile...");
				assertTrue(user!=null && user.getUsrId()!=null);		
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
	
	//TEST KO: try to find user with wrong password
	@Test
	public void findUserByEmailPasswordTest_wrongPassword_KO() {	

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
				idUserInserted=Integer.valueOf(dtoResult.getUsrId());

				
				User userByID = new User();
				userByID = userDaoImpl.findByPrimaryKey(userByID, idUserInserted);			
				
				String wrongPassword="wrongPassword";
				
				User user = userDaoImpl.findUserByEmailPassword(userByID.getUsrEmail(), wrongPassword);

				//delete data just inserted in DB
				log.info("executing rollback...");
				rollBack(dtoResult, idUserInserted, session);
				log.info("rollback OK");


				//check showProfile outpu
				log.info("assert show profile...");
				assertTrue(user!=null && user.getUsrId()!=null);		
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
	
	
	//--------------FIND USER BY EMAIL----------------
	@Test
	public void findUserByEmailTest() {	

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
				idUserInserted=Integer.valueOf(dtoResult.getUsrId());
				
				User user = userDaoImpl.findUserByEmail(dtoResult.getUsrEmail());

				//delete data just inserted in DB
				log.info("executing rollback...");
				rollBack(dtoResult, idUserInserted, session);
				log.info("rollback OK");


				//check showProfile outpu
				log.info("assert show profile...");
				assertTrue(user!=null && user.getUsrId()!=null);		
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
	
	//TEST KO: try to find user by wrong email
	@Test
	public void findUserByEmailTest_wrongEmail_KO() {	

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
				idUserInserted=Integer.valueOf(dtoResult.getUsrId());
				
				String wrongEmail = "wrongEmail";
				User user = userDaoImpl.findUserByEmail(wrongEmail);

				//delete data just inserted in DB
				log.info("executing rollback...");
				rollBack(dtoResult, idUserInserted, session);
				log.info("rollback OK");


				//check showProfile outpu
				log.info("assert show profile...");
				assertTrue(user!=null && user.getUsrId()!=null);		
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
	
	//--------------FIND USER BY RECOVERY CODE----------------
	@Test
	public void findUserByRecoveryCodeTest() {	

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
				idUserInserted=Integer.valueOf(dtoResult.getUsrId());
				
				log.info("try to send mail with recovery link...");
				org.hibernate.Transaction txn = session.beginTransaction();
				userServiceImpl.sendRecoveryLink(dtoResult.getUsrEmail(), "COMPANY");
				txn.commit();
				log.info("sending mail with recovery link OK");
				
				User user = new User();
				user = userDaoImpl.findByPrimaryKey(user, idUserInserted);
				
				User userByRecoveryCode = userDaoImpl.findUserByRecoveryCode(user.getUsrRecoverEmailCode());

				//delete data just inserted in DB
				log.info("executing rollback...");
				rollBack(dtoResult, idUserInserted, session);
				log.info("rollback OK");


				//check showProfile outpu
				log.info("assert show profile...");
				assertTrue(userByRecoveryCode!=null && userByRecoveryCode.getUsrId()!=null);		
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
	
	//TEST KO: try to recover user by wrong recovery code
	@Test
	public void findUserByRecoveryCodeTest_wrongCode_KO() {	

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
				idUserInserted=Integer.valueOf(dtoResult.getUsrId());
				
				log.info("try to send mail with recovery link...");
				org.hibernate.Transaction txn = session.beginTransaction();
				userServiceImpl.sendRecoveryLink(dtoResult.getUsrEmail(), "COMPANY");
				txn.commit();
				log.info("sending mail with recovery link OK");
				
				String wrongCode="wrongCode";
				
				User userByRecoveryCode = userDaoImpl.findUserByRecoveryCode(wrongCode);

				//delete data just inserted in DB
				log.info("executing rollback...");
				rollBack(dtoResult, idUserInserted, session);
				log.info("rollback OK");


				//check showProfile outpu
				log.info("assert show profile...");
				assertTrue(userByRecoveryCode!=null && userByRecoveryCode.getUsrId()!=null);		
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
	
	
	@Test
	public void getUserByLastInvocationDateTest() {	
		try {
			User user = new User();
			log.info("filling user to insert...");
			user = fillUser(user);
			
			log.info("insert User...");
			Session session = sessionFactory.getCurrentSession();
			userDaoImpl.insert(user);			
			session.getTransaction().commit();	
			
			log.info("Getting User by invocation date...");
			List<User> userList = userDaoImpl.getUserByLastInvocationDate(Constants.FORMATTER_DD_MM_YYYY.parse("18-06-2018"));			
			
			//delete the User
			log.info("Deleting inserted data...");
			org.hibernate.Transaction txn3 = session.beginTransaction();
			Query query3 = session.createQuery("delete from User where usr_email = '" + user.getUsrEmail() + "'");
			query3.executeUpdate();				
			txn3.commit();
			
			assertTrue(userList!=null && userList.size()>0);
			
			
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}
	
	//TEST KO: try to get user passing date after last invocation date
	@Test
	public void getUserByLastInvocationDateTest_dateAfterLastInovcation_KO() {	
		try {
			User user = new User();
			log.info("filling user to insert...");
			user = fillUser(user);
			
			log.info("insert User...");
			Session session = sessionFactory.getCurrentSession();
			userDaoImpl.insert(user);			
			session.getTransaction().commit();	
			
			log.info("Getting User by invocation date...");
			List<User> userList = userDaoImpl.getUserByLastInvocationDate(Constants.FORMATTER_DD_MM_YYYY.parse("18-06-2999"));			
			
			//delete the User
			log.info("Deleting inserted data...");
			org.hibernate.Transaction txn3 = session.beginTransaction();
			Query query3 = session.createQuery("delete from User where usr_email = '" + user.getUsrEmail() + "'");
			query3.executeUpdate();				
			txn3.commit();
			
			assertTrue(userList!=null && userList.size()>0);
			
			
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}
	
	
	private User fillUser(User user) {
		user.setUsrEmail("mailForTest@gmail.com");
		user.setUsrPassword("passwordTest");
		user.setLastInvocationDate(new Date());
		return user;
	}

	//--------------UTILITITES-------------------
	private CompanyDTO fillCompanyDTO() {
		CompanyDTO dto = new CompanyDTO();
		dto.setUsrEmail("stefanoliga12@libero.it");
		dto.setUsrPassword("Liga.654");
		dto.setCpyPartitaIva("000PARTITAIVA000");
		dto.setCpyCodiceFiscale("000CODICEFISCALE000");
		dto.setCpyPec("stefanoliga12@libero.it");
		return dto;
	}
	

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

}
