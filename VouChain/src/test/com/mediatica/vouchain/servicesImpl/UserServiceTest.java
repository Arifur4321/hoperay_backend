package test.com.mediatica.vouchain.servicesImpl;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.apache.log4j.Logger;
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
public class UserServiceTest {

	@Autowired
	SessionFactory sessionFactory;	

	@Autowired
	UserDaoImpl userDaoImpl;

	@Autowired
	UserServiceImpl userServiceImpl;

	@Autowired
	CompanyServicesInterface companyServicesInterface;

	private static org.slf4j.Logger log = LoggerFactory.getLogger(UserServiceImpl.class);
	

//------------SEND RECOVERY LINK----------------------------------------
	
	@Test
	public void sendRecoveryLinkTest() {	

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
				
				log.info("try to send mail with recovery link...");
				org.hibernate.Transaction txn = session.beginTransaction();
				userServiceImpl.sendRecoveryLink(dtoResult.getUsrEmail(), "COMPANY");
				txn.commit();
				log.info("sending mail with recovery OK...");

				
				//recover the User
				idUserInserted=Integer.valueOf(dtoResult.getUsrId());
				User user = new User();
				user = userDaoImpl.findByPrimaryKey(user, idUserInserted);	
				
				//delete data just inserted in DB
				log.info("executing rollback...");
				rollBack(dtoResult, idUserInserted, session);
				log.info("rollback OK");			


				//check recovery code
				log.info("assert recovery code...");
				assertTrue(user!=null && user.getUsrRecoverEmailCode()!=null);
				log.info("recovery code OK");
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
	
	//Test KO: try to send recovey link with no existing mail
	@Test
	public void sendRecoveryLinkTest_noMailFound_KO() {	

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
				org.hibernate.Transaction txn = session.beginTransaction();
				try {
					log.info("try to send mail with recovery link...");
					userServiceImpl.sendRecoveryLink("noExistingMail@mail.com", "COMPANY");
					txn.commit();

				} catch (Exception e) {
					txn.commit();
					//delete data just inserted in DB
					idUserInserted=Integer.valueOf(dtoResult.getUsrId());
					log.info("executing rollback...");
					rollBack(dtoResult, idUserInserted, session);
					log.info("rollback OK");
					
					e.printStackTrace();
					fail();
				}
				txn.commit();
			}
			
			//delete data just inserted in DB
			idUserInserted=Integer.valueOf(dtoResult.getUsrId());
			log.info("executing rollback...");
			rollBack(dtoResult, idUserInserted, session);
			log.info("rollback OK");


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
	
	
//------------SEND RECOVERY LINK----------------------------------------	

	@Test
	public void modifyPasswordTest() {
		
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
				
				log.info("try to send mail with recovery link...");
				org.hibernate.Transaction txn = session.beginTransaction();
				userServiceImpl.sendRecoveryLink(dtoResult.getUsrEmail(), "COMPANY");
				txn.commit();
				log.info("sending mail with recovery link OK");

				
				//recover the User before modify the password
				idUserInserted=Integer.valueOf(dtoResult.getUsrId());
				User userBeforeModifyPassword = new User();
				userBeforeModifyPassword = userDaoImpl.findByPrimaryKey(userBeforeModifyPassword, idUserInserted);
				String passwordBeforeModify = userBeforeModifyPassword.getUsrPassword();
			
				log.info("modify Password...");
				org.hibernate.Transaction txn2 = session.beginTransaction();
				userServiceImpl.modifyPassword("newPassword", userBeforeModifyPassword.getUsrRecoverEmailCode());
				txn2.commit();
				log.info("Password modified");

				
				//recover the User after modify the password
				User userAfterModifyPassword = new User();
				userAfterModifyPassword = userDaoImpl.findByPrimaryKey(userAfterModifyPassword, idUserInserted);
				String passwordAfterModify = userAfterModifyPassword.getUsrPassword();

				
				//delete data just inserted in DB
				log.info("executing rollback...");
				rollBack(dtoResult, idUserInserted, session);
				log.info("rollback OK");
				
				log.info("assert password modify...");
				assertTrue(!passwordBeforeModify.equals(passwordAfterModify));
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
	
	//Test KO: try to modify password with wrong recovery code
	@Test
	public void modifyPasswordTest_wrongRecoveryCode_KO() {
		
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
				org.hibernate.Transaction txn = session.beginTransaction();
				userServiceImpl.sendRecoveryLink(dtoResult.getUsrEmail(), "COMPANY");
				txn.commit();
				
				//recover the User before modify the password
				idUserInserted=Integer.valueOf(dtoResult.getUsrId());
				User userBeforeModifyPassword = new User();
				userBeforeModifyPassword = userDaoImpl.findByPrimaryKey(userBeforeModifyPassword, idUserInserted);
			
				log.info("modify Password...");
				org.hibernate.Transaction txn2 = session.beginTransaction();
				try {
					//try to modify password with wrong recovery code"
					userServiceImpl.modifyPassword("newPassword", "WRONG_RECOVERY_CODE");
					txn2.commit();

				} catch (Exception e) {
					txn2.commit();
					//delete data just inserted in DB
					log.info("executing rollback...");
					rollBack(dtoResult, idUserInserted, session);
					log.info("rollback OK");
					e.printStackTrace();
					fail();
				}

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


}
