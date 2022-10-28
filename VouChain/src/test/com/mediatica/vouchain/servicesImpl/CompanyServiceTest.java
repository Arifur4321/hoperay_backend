package test.com.mediatica.vouchain.servicesImpl;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.apache.log4j.Logger;
import org.bouncycastle.util.test.TestFailedException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.mediatica.vouchain.dao.CompanyDaoImpl;
import com.mediatica.vouchain.dao.EmployeeDaoImpl;
import com.mediatica.vouchain.dao.TransactionDaoImpl;
import com.mediatica.vouchain.dao.UserDaoImpl;
import com.mediatica.vouchain.dto.CompanyDTO;
import com.mediatica.vouchain.dto.DTO;
import com.mediatica.vouchain.entities.Company;
import com.mediatica.vouchain.entities.Transaction;
import com.mediatica.vouchain.entities.User;
import com.mediatica.vouchain.exceptions.ContractNotVaidException;
import com.mediatica.vouchain.exceptions.EmailYetInTheSystemException;
import com.mediatica.vouchain.servicesImpl.CompanyServiceImpl;
import com.mediatica.vouchain.servicesInterface.CompanyServicesInterface;

@Transactional
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("file:WebContent/WEB-INF/vouchain-servlet.xml")
@WebAppConfiguration
public class CompanyServiceTest {	

	@Autowired
	SessionFactory sessionFactory;	

	@Autowired
	CompanyServicesInterface companyServicesInterface;

	@Autowired
	UserDaoImpl userDaoImpl;

	@Autowired
	CompanyDaoImpl companyDaoImpl;

	@Autowired
	EmployeeDaoImpl employeeDaoImpl;

	@Autowired
	TransactionDaoImpl transactionDaoImpl;

	private static org.slf4j.Logger log = LoggerFactory.getLogger(CompanyServiceImpl.class);
	
	

//------------CHECK LOAD SIGN----------------------------------------
	

	@Test
	public void checkLoadSignTest() {
		
		CompanyDTO dto = fillCompanyDTO();	
		CompanyDTO dtoResult = null;
		Integer idUserInserted = null;

		try {
			//signUp
			log.info("Starting signUp...");
			dtoResult = (CompanyDTO) companyServicesInterface.signUp(dto);
			Session session = sessionFactory.getCurrentSession();
			session.getTransaction().commit();	
			
			//get user ID
			if(dtoResult!=null) {
				log.info("signUp OK");
				idUserInserted=Integer.valueOf(dtoResult.getUsrId());
			}
		
			//recover multipart file
			boolean alteredContract = false;
			MultipartFile multipartFile = getMultipartFile(alteredContract);
			
			//check load sign
			org.hibernate.Transaction txn = session.beginTransaction();
			CompanyDTO checkLoadSignResult = companyServicesInterface.checkLoadSign(dtoResult.getUsrId(), multipartFile);
			txn.commit();
			
			//rollback		
			log.info("executing rollback...");
			rollBack(dtoResult, idUserInserted, session);
			log.info("rollback OK");
			
			assertTrue(checkLoadSignResult!=null);	


		} catch (EmailYetInTheSystemException e) {
			e.printStackTrace();
			log.error("Email inserted yet");
			fail();
		} catch (ContractNotVaidException e) {			
			e.printStackTrace();
			log.error("Contract not valid");
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
	
	//TEST KO: try to check load sign with an altered contract
	@Test
	public void checkLoadSignTest_alteredContract_KO() {
		
		CompanyDTO dto = fillCompanyDTO();	
		CompanyDTO dtoResult = null;
		Integer idUserInserted = null;

		try {
			//signUp
			log.info("Starting signUp...");
			dtoResult = (CompanyDTO) companyServicesInterface.signUp(dto);
			Session session = sessionFactory.getCurrentSession();
			session.getTransaction().commit();	
			
			//get user ID
			if(dtoResult!=null) {
				log.info("signUp OK");
				idUserInserted=Integer.valueOf(dtoResult.getUsrId());
			}		
			
			//recover multipart file
			boolean alteredContract = true;
			MultipartFile multipartFile = getMultipartFile(alteredContract);
			
			org.hibernate.Transaction txn = session.beginTransaction();
			try {
				//try to check load sign with altered contract
				CompanyDTO checkLoadSignResult = companyServicesInterface.checkLoadSign(dtoResult.getUsrId(), multipartFile);
				//rollback
				txn.commit();			
				log.info("executing rollback...");
				rollBack(dtoResult, idUserInserted, session);
				log.info("rollback OK");
			} 
			catch (ContractNotVaidException e) {			
				//rollback
				txn.commit();			
				log.info("executing rollback...");
				rollBack(dtoResult, idUserInserted, session);
				log.info("rollback OK");

				e.printStackTrace();
				log.error("Contract not valid");
				fail();
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
			dtoResult = (CompanyDTO) companyServicesInterface.signUp(dto);
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
		CompanyDTO dto = fillCompanyDTO_NoMail();	
		CompanyDTO dtoResult = null;
		Integer idUserInserted = null;

		try {
			log.info("Statring signUp...");
			
			//try signup without email
			dtoResult = (CompanyDTO) companyServicesInterface.signUp(dto);
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

	//TEST KO: try to insert User without PEC
	@Test
	public void signUpTest_insertCompany_noPEC_KO() {			

		//Fill dto without PEC
		CompanyDTO dto = fillCompanyDTO_NoPEC();	
		CompanyDTO dtoResult = null;
		Integer idUserInserted = null;

		try {
			log.info("Statring signUp...");
			
			//try signup without PEC
			dtoResult = (CompanyDTO) companyServicesInterface.signUp(dto);
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
				DTO profile = companyServicesInterface.showProfile(dtoResult.getUsrId());

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
				DTO profile = companyServicesInterface.showProfile("9999");

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
	
//------------LOGIN--------------------------------------------


	@Test
	public void loginTest() {	

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
				DTO dtoFromLogin = companyServicesInterface.login(dtoResult.getUsrEmail());

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
				DTO dtoFromLogin = companyServicesInterface.login("notExistingMail@gmail.com");

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
				org.hibernate.Transaction txn = session.beginTransaction();
				CompanyDTO dtoAfterModify = (CompanyDTO) companyServicesInterface.modProfile(modifiedCompany);
				txn.commit();


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

				//PEC null
				modifiedCompany.setCpyPec(null);				

				try {
					//update company 
					CompanyDTO dtoAfterModify;
					org.hibernate.Transaction txn = session.beginTransaction();
					dtoAfterModify = (CompanyDTO) companyServicesInterface.modProfile(modifiedCompany);
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

	private CompanyDTO fillCompanyDTO() {
		CompanyDTO dto = new CompanyDTO();
		dto.setUsrEmail("stefanoliga12@libero.it");
		dto.setUsrPassword("Liga.654");
		dto.setCpyPartitaIva("000PARTITAIVA000");
		dto.setCpyCodiceFiscale("000CODICEFISCALE000");
		dto.setCpyPec("stefanoliga12@libero.it");
		return dto;
	}

	private CompanyDTO fillCompanyDTO_NoMail() {
		CompanyDTO dto = new CompanyDTO();
		dto.setUsrPassword("Liga.654");
		dto.setCpyPartitaIva("000PARTITAIVA000");
		dto.setCpyCodiceFiscale("000CODICEFISCALE000");
		dto.setCpyPec("s.liga@aktsrl.com");
		return dto;
	}

	private CompanyDTO fillCompanyDTO_NoPEC() {
		CompanyDTO dto = new CompanyDTO();
		dto.setUsrEmail("liga@gmail.com");
		dto.setUsrPassword("Liga.654");
		dto.setCpyPartitaIva("000PARTITAIVA000");
		dto.setCpyCodiceFiscale("000CODICEFISCALE000");
		return dto;
	}
	

	private MultipartFile getMultipartFile(boolean alteredContract) throws IOException {
		Path path = Paths.get("");
		String name = "";
		String originalFileName = "";		
		
		if(alteredContract) {
			path = Paths.get("C:/vouchain/test/test_altered_cert.pdf.p7m");
			name = "test_altered_cert.pdf.p7m";
			originalFileName = "test_altered_cert.pdf.p7m";
		}
		else {
			path = Paths.get("C:/vouchain/test/test.pdf.p7m");
			name = "test.pdf.p7m";
			originalFileName = "test.pdf.p7m";
		}

		String contentType = "application/pdf";
		byte[] content = null;
		content = Files.readAllBytes(path);
		MultipartFile result = new MockMultipartFile(name, originalFileName, contentType, content);
		return result;
	}

}
