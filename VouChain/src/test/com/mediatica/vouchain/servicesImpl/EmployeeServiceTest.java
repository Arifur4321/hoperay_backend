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

import com.mediatica.vouchain.dao.EmployeeDaoImpl;
import com.mediatica.vouchain.dao.TransactionDaoImpl;
import com.mediatica.vouchain.dao.UserDaoImpl;
import com.mediatica.vouchain.dto.CompanyDTO;
import com.mediatica.vouchain.dto.DTO;
import com.mediatica.vouchain.dto.EmployeeDTO;
import com.mediatica.vouchain.entities.Employee;
import com.mediatica.vouchain.entities.Transaction;
import com.mediatica.vouchain.entities.User;
import com.mediatica.vouchain.exceptions.EmailYetInTheSystemException;
import com.mediatica.vouchain.servicesImpl.EmployeeServiceImpl;
import com.mediatica.vouchain.servicesInterface.CompanyServicesInterface;
import com.mediatica.vouchain.servicesInterface.EmployeeServicesInterface;

@Transactional
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("file:WebContent/WEB-INF/vouchain-servlet.xml")
@WebAppConfiguration
public class EmployeeServiceTest {
	
	private static org.slf4j.Logger log = LoggerFactory.getLogger(EmployeeServiceImpl.class);
	
	@Autowired
	SessionFactory sessionFactory;	
	
	@Autowired
	EmployeeServicesInterface employeeServicesInterface;
	
	@Autowired
	CompanyServicesInterface companyServicesInterface;
	
	@Autowired
	UserDaoImpl userDaoImpl;
	
	@Autowired
	EmployeeDaoImpl employeeDaoImpl;
	
	@Autowired
	TransactionDaoImpl transactionDaoImpl;
	
//------------SIGUNP----------------------------------------

	@Test
	public void signUpTest() {		
		CompanyDTO companyDTO = fillCompanyDTO();
		CompanyDTO companyDTOResult = null;
		EmployeeDTO employeeDTO = fillEmployeeDTO();	
		EmployeeDTO employeeDTOResult = null;
		Integer idEmployeeInserted = null;
		Integer idCompanyInserted = null;


		try {
			
			Session session = sessionFactory.getCurrentSession();

			log.info("Statring company signUp...");			
			companyDTOResult = (CompanyDTO) companyServicesInterface.signUp(companyDTO);
			session.getTransaction().commit();
			
			if(companyDTOResult!=null) {
				log.info("signUp OK");
				idCompanyInserted=Integer.valueOf(companyDTOResult.getUsrId());
			}
			
			employeeDTO.setCpyUsrId(companyDTOResult.getUsrId());

			log.info("Starting employee signUp...");
			org.hibernate.Transaction txn2 = session.beginTransaction();
			employeeDTOResult = (EmployeeDTO) employeeServicesInterface.signUp(employeeDTO);
			txn2.commit();	

			if(employeeDTOResult!=null) {
				log.info("signUp OK");
				idEmployeeInserted=Integer.valueOf(employeeDTOResult.getUsrId());
			}
			
			log.info("get data from DB...");
			
			//recover the User
			User user = new User();
			user = userDaoImpl.findByPrimaryKey(user, idEmployeeInserted);	
			
			//recover the Employee
			Employee employee = new Employee();
			employee = employeeDaoImpl.findByPrimaryKey(employee, idEmployeeInserted);			
			
			//delete data just inserted in DB		
			log.info("executing rollback...");
			rollBack(idEmployeeInserted, idCompanyInserted, session);
			log.info("rollback OK");
			
			//check user insert
			log.info("assert User insert...");
			assertTrue(user.getUsrId()!=null);		
			log.info("User insert OK");
			
			//check employee insert
			log.info("assert Employee insert...");
			assertTrue(employee.getUsrId()!=null);		
			log.info("Employee insert OK");
			
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
	
	//TEST KO: try to insert Employee without first name
	@Test
	public void signUpTest_insertEmployee_noFirstName_KO() {			
		
		CompanyDTO companyDTO = fillCompanyDTO();
		CompanyDTO companyDTOResult = null;
		EmployeeDTO employeeDTO = fillEmployeeDTO_noFirstName();	
		EmployeeDTO employeeDTOResult = null;
		Integer idEmployeeInserted = null;
		Integer idCompanyInserted = null;
		

		try {
			Session session = sessionFactory.getCurrentSession();

			log.info("Statring company signUp...");			
			companyDTOResult = (CompanyDTO) companyServicesInterface.signUp(companyDTO);
			session.getTransaction().commit();
			
			if(companyDTOResult!=null) {
				log.info("signUp OK");
				idCompanyInserted=Integer.valueOf(companyDTOResult.getUsrId());
			}
			
			employeeDTO.setCpyUsrId(companyDTOResult.getUsrId());
			
			org.hibernate.Transaction txn2 = session.beginTransaction();

			try {
				log.info("Starting employee signUp...");
				employeeDTOResult = (EmployeeDTO) employeeServicesInterface.signUp(employeeDTOResult);
				txn2.commit();	

			} catch (Exception e) {
				txn2.commit();	
				//delete data just inserted in DB		
				log.info("executing rollback...");
				rollBack(idEmployeeInserted, idCompanyInserted, session);
				log.info("rollback OK");
				e.printStackTrace();
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
	
//------------SHOW PROFILE--------------------------------------------

	@Test
	public void showProfileTest() {	
		
		CompanyDTO companyDTO = fillCompanyDTO();
		CompanyDTO companyDTOResult = null;
		EmployeeDTO employeeDTO = fillEmployeeDTO();	
		EmployeeDTO employeeDTOResult = null;
		Integer idEmployeeInserted = null;
		Integer idCompanyInserted = null;

		try {
			Session session = sessionFactory.getCurrentSession();

			log.info("Statring company signUp...");			
			companyDTOResult = (CompanyDTO) companyServicesInterface.signUp(companyDTO);
			session.getTransaction().commit();
			
			if(companyDTOResult!=null) {
				log.info("signUp OK");
				idCompanyInserted=Integer.valueOf(companyDTOResult.getUsrId());
			}
			
			employeeDTO.setCpyUsrId(companyDTOResult.getUsrId());

			log.info("Starting employee signUp...");
			org.hibernate.Transaction txn2 = session.beginTransaction();
			employeeDTOResult = (EmployeeDTO) employeeServicesInterface.signUp(employeeDTO);
			txn2.commit();	

			if(employeeDTOResult!=null) {
				log.info("signUp OK");
				
				//showProfile
				DTO profile = employeeServicesInterface.showProfile(employeeDTOResult.getUsrId());

				//delete data just inserted in DB
				idEmployeeInserted=Integer.valueOf(employeeDTOResult.getUsrId());
				log.info("executing rollback...");
				rollBack(idEmployeeInserted, idCompanyInserted, session);
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
	
	//TEST KO: try to show proile with a not existing user id
	@Test
	public void showProfileTest_NotExistingID_KO() {	
		
		CompanyDTO companyDTO = fillCompanyDTO();
		CompanyDTO companyDTOResult = null;
		EmployeeDTO employeeDTO = fillEmployeeDTO();	
		EmployeeDTO employeeDTOResult = null;
		Integer idEmployeeInserted = null;
		Integer idCompanyInserted = null;

		try {
			Session session = sessionFactory.getCurrentSession();

			log.info("Statring company signUp...");			
			companyDTOResult = (CompanyDTO) companyServicesInterface.signUp(companyDTO);
			session.getTransaction().commit();
			
			if(companyDTOResult!=null) {
				log.info("signUp OK");
				idCompanyInserted=Integer.valueOf(companyDTOResult.getUsrId());
			}
			
			employeeDTO.setCpyUsrId(companyDTOResult.getUsrId());

			log.info("Starting employee signUp...");
			org.hibernate.Transaction txn2 = session.beginTransaction();
			employeeDTOResult = (EmployeeDTO) employeeServicesInterface.signUp(employeeDTO);
			txn2.commit();	

			if(employeeDTOResult!=null) {
				log.info("signUp OK");
				
				//try showProfile with no existing user id
				DTO profile = employeeServicesInterface.showProfile("9999");

				//delete data just inserted in DB
				idEmployeeInserted=Integer.valueOf(employeeDTOResult.getUsrId());
				log.info("executing rollback...");
				rollBack(idEmployeeInserted, idCompanyInserted, session);
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
	
//------------MOD PROFILE--------------------------------------------

	@Test
	public void modProfileTest() {	
		
		CompanyDTO companyDTO = fillCompanyDTO();
		CompanyDTO companyDTOResult = null;
		EmployeeDTO dtoBeforeModify = fillEmployeeDTO();	
		Integer idEmployeeInserted = null;
		Integer idCompanyInserted = null;

		try {
			Session session = sessionFactory.getCurrentSession();

			log.info("Statring company signUp...");			
			companyDTOResult = (CompanyDTO) companyServicesInterface.signUp(companyDTO);
			session.getTransaction().commit();
			
			if(companyDTOResult!=null) {
				log.info("signUp OK");
				idCompanyInserted=Integer.valueOf(companyDTOResult.getUsrId());
			}
			
			dtoBeforeModify.setCpyUsrId(companyDTOResult.getUsrId());

			log.info("Starting employee signUp...");
			org.hibernate.Transaction txn2 = session.beginTransaction();
			dtoBeforeModify = (EmployeeDTO) employeeServicesInterface.signUp(dtoBeforeModify);
			txn2.commit();	

			if(dtoBeforeModify!=null) {
				log.info("signUp OK");

				//change employee data
				EmployeeDTO modifiedEmployee = fillEmployeeForModProfile(dtoBeforeModify.getUsrId());

				//update employee 
				org.hibernate.Transaction txn = session.beginTransaction();
				EmployeeDTO dtoAfterModify = (EmployeeDTO) employeeServicesInterface.modProfile(modifiedEmployee);
				txn.commit();


				//delete data just inserted in DB
				idEmployeeInserted=Integer.valueOf(dtoAfterModify.getUsrId());
				log.info("executing rollback...");
				rollBack(idEmployeeInserted, idCompanyInserted, session);
				log.info("rollback OK");

				//check employee update
				log.info("assert modProfile...");
				assertTrue(dtoAfterModify!=null && dtoAfterModify.getUsrId()!=null);
				assertTrue(!dtoBeforeModify.getEmpFirstName().equals(dtoAfterModify.getEmpFirstName()));		

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
	
	//TEST KO: try to modify profile without First Name
	@Test
	public void modProfileTest_nullFirstName_KO() {	

		CompanyDTO companyDTO = fillCompanyDTO();
		CompanyDTO companyDTOResult = null;
		EmployeeDTO dtoBeforeModify = fillEmployeeDTO();	
		Integer idEmployeeInserted = null;
		Integer idCompanyInserted = null;

		try {
			Session session = sessionFactory.getCurrentSession();

			log.info("Statring company signUp...");			
			companyDTOResult = (CompanyDTO) companyServicesInterface.signUp(companyDTO);
			session.getTransaction().commit();
			
			if(companyDTOResult!=null) {
				log.info("signUp OK");
				idCompanyInserted=Integer.valueOf(companyDTOResult.getUsrId());
			}
			
			dtoBeforeModify.setCpyUsrId(companyDTOResult.getUsrId());

			log.info("Starting employee signUp...");
			org.hibernate.Transaction txn2 = session.beginTransaction();
			dtoBeforeModify = (EmployeeDTO) employeeServicesInterface.signUp(dtoBeforeModify);
			txn2.commit();	

			if(dtoBeforeModify!=null) {
				log.info("signUp OK");

				//change employee data
				EmployeeDTO modifiedEmployee = fillEmployeeForModProfile(dtoBeforeModify.getUsrId());

				//Email null
				modifiedEmployee.setEmpFirstName(null);;				

				try {
					//update employee 
					EmployeeDTO dtoAfterModify;
					org.hibernate.Transaction txn = session.beginTransaction();
					dtoAfterModify = (EmployeeDTO) employeeServicesInterface.modProfile(modifiedEmployee);
					txn.commit();
					
				} catch (Exception e) {
					session.getTransaction().rollback();
					idEmployeeInserted=Integer.valueOf(dtoBeforeModify.getUsrId());
					log.info("executing rollback...");
					rollBack(idEmployeeInserted, idCompanyInserted, session);
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
		
		CompanyDTO companyDTO = fillCompanyDTO();
		CompanyDTO companyDTOResult = null;
		EmployeeDTO employeeDTO = fillEmployeeDTO();	
		Integer idEmployeeInserted = null;
		Integer idCompanyInserted = null;

		try {
			Session session = sessionFactory.getCurrentSession();

			log.info("Statring company signUp...");			
			companyDTOResult = (CompanyDTO) companyServicesInterface.signUp(companyDTO);
			session.getTransaction().commit();
			
			if(companyDTOResult!=null) {
				log.info("signUp OK");
				idCompanyInserted=Integer.valueOf(companyDTOResult.getUsrId());
			}
			
			employeeDTO.setCpyUsrId(companyDTOResult.getUsrId());

			log.info("Starting employee signUp...");
			org.hibernate.Transaction txn2 = session.beginTransaction();
			employeeDTO = (EmployeeDTO) employeeServicesInterface.signUp(employeeDTO);
			txn2.commit();	

			if(employeeDTO!=null) {
				log.info("signUp OK");

				//login
				DTO dtoFromLogin = employeeServicesInterface.login(employeeDTO.getUsrEmail());

				//delete data just inserted in DB
				idEmployeeInserted=Integer.valueOf(employeeDTO.getUsrId());
				log.info("executing rollback...");
				rollBack(idEmployeeInserted, idCompanyInserted,  session);
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
		CompanyDTO companyDTO = fillCompanyDTO();
		CompanyDTO companyDTOResult = null;
		EmployeeDTO employeeDTO = fillEmployeeDTO();	
		Integer idEmployeeInserted = null;
		Integer idCompanyInserted = null;

		try {
			Session session = sessionFactory.getCurrentSession();

			log.info("Statring company signUp...");			
			companyDTOResult = (CompanyDTO) companyServicesInterface.signUp(companyDTO);
			session.getTransaction().commit();
			
			if(companyDTOResult!=null) {
				log.info("signUp OK");
				idCompanyInserted=Integer.valueOf(companyDTOResult.getUsrId());
			}
			
			employeeDTO.setCpyUsrId(companyDTOResult.getUsrId());

			log.info("Starting employee signUp...");
			org.hibernate.Transaction txn2 = session.beginTransaction();
			employeeDTO = (EmployeeDTO) employeeServicesInterface.signUp(employeeDTO);
			txn2.commit();	

			if(employeeDTO!=null) {
				log.info("signUp OK");

				//login
				DTO dtoFromLogin = employeeServicesInterface.login("notExistingMail@gmail.com");

				//delete data just inserted in DB
				idEmployeeInserted=Integer.valueOf(employeeDTO.getUsrId());
				log.info("executing rollback...");
				rollBack(idEmployeeInserted, idCompanyInserted, session);
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
	
//------------GET EMPLOYEE LIST--------------------------------------------


	@Test
	public void getEmployeeListTest() {	

		CompanyDTO companyDTO = fillCompanyDTO();
		CompanyDTO companyDTOResult = null;
		EmployeeDTO employeeDTO = fillEmployeeDTO();	
		Integer idEmployeeInserted = null;
		Integer idCompanyInserted = null;

		try {
			Session session = sessionFactory.getCurrentSession();

			log.info("Statring company signUp...");			
			companyDTOResult = (CompanyDTO) companyServicesInterface.signUp(companyDTO);
			session.getTransaction().commit();
			
			if(companyDTOResult!=null) {
				log.info("signUp OK");
				idCompanyInserted=Integer.valueOf(companyDTOResult.getUsrId());
			}
			
			employeeDTO.setCpyUsrId(companyDTOResult.getUsrId());

			log.info("Starting employee signUp...");
			org.hibernate.Transaction txn2 = session.beginTransaction();
			employeeDTO = (EmployeeDTO) employeeServicesInterface.signUp(employeeDTO);
			txn2.commit();	

			if(employeeDTO!=null) {
				log.info("signUp OK");

				//get employee list by company id
				List<EmployeeDTO> dtoList = employeeServicesInterface.getEmployeeList(employeeDTO.getCpyUsrId());

				//delete data just inserted in DB
				idEmployeeInserted=Integer.valueOf(employeeDTO.getUsrId());
				log.info("executing rollback...");
				rollBack(idEmployeeInserted, idCompanyInserted, session);
				log.info("rollback OK");

				//check get employee list
				log.info("assert employee list...");
				assertTrue(dtoList!=null && dtoList.size()>0);		
				log.info("employee list OK");

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
	
	//TEST KO: try get employee list with a null company ID
	@Test
	public void getEmployeeListTest_nullCompanyID_KO() {	

		CompanyDTO companyDTO = fillCompanyDTO();
		CompanyDTO companyDTOResult = null;
		EmployeeDTO employeeDTO = fillEmployeeDTO();	
		Integer idEmployeeInserted = null;
		Integer idCompanyInserted = null;

		try {
			Session session = sessionFactory.getCurrentSession();

			log.info("Statring company signUp...");			
			companyDTOResult = (CompanyDTO) companyServicesInterface.signUp(companyDTO);
			session.getTransaction().commit();
			
			if(companyDTOResult!=null) {
				log.info("signUp OK");
				idCompanyInserted=Integer.valueOf(companyDTOResult.getUsrId());
			}
			
			employeeDTO.setCpyUsrId(companyDTOResult.getUsrId());

			log.info("Starting employee signUp...");
			org.hibernate.Transaction txn2 = session.beginTransaction();
			employeeDTO = (EmployeeDTO) employeeServicesInterface.signUp(employeeDTO);
			txn2.commit();	

			if(employeeDTO!=null) {
				log.info("signUp OK");

				try {
					//try to get employee list by null company id
					List<EmployeeDTO> dtoList = employeeServicesInterface.getEmployeeList(null);

				} catch (Exception e) {
					//delete data just inserted in DB
					idEmployeeInserted=Integer.valueOf(employeeDTO.getUsrId());
					log.info("executing rollback...");
					rollBack(idEmployeeInserted, idCompanyInserted, session);
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
	
//------------CHECK INVITATION CODE--------------------------------------------
	
	@Test
	public void checkInvitationCodeTest() {	

		CompanyDTO companyDTO = fillCompanyDTO();
		CompanyDTO companyDTOResult = null;
		EmployeeDTO employeeDTO = fillEmployeeDTO();	
		Integer idEmployeeInserted = null;
		Integer idCompanyInserted = null;

		try {
			Session session = sessionFactory.getCurrentSession();

			log.info("Statring company signUp...");			
			companyDTOResult = (CompanyDTO) companyServicesInterface.signUp(companyDTO);
			session.getTransaction().commit();
			
			if(companyDTOResult!=null) {
				log.info("signUp OK");
				idCompanyInserted=Integer.valueOf(companyDTOResult.getUsrId());
			}
			
			employeeDTO.setCpyUsrId(companyDTOResult.getUsrId());

			log.info("Starting employee signUp...");
			org.hibernate.Transaction txn2 = session.beginTransaction();
			employeeDTO = (EmployeeDTO) employeeServicesInterface.signUp(employeeDTO);
			txn2.commit();	

			if(employeeDTO!=null) {
				log.info("signUp OK");

				//check invitation code
				DTO dtoFromCheckInvitationCode = employeeServicesInterface.checkInvitationCode(employeeDTO.getEmpInvitationCode());

				//delete data just inserted in DB
				idEmployeeInserted=Integer.valueOf(employeeDTO.getUsrId());
				log.info("executing rollback...");
				rollBack(idEmployeeInserted, idCompanyInserted, session);
				log.info("rollback OK");

				//check login
				log.info("assert checking invitation code...");
				assertTrue(dtoFromCheckInvitationCode!=null);		
				log.info("checking invitation code OK");

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
	
	//TEST KO: try check a wrong invitation code
	@Test
	public void checkInvitationCodeTest_wrongCode_KO() {	

		CompanyDTO companyDTO = fillCompanyDTO();
		CompanyDTO companyDTOResult = null;
		EmployeeDTO employeeDTO = fillEmployeeDTO();	
		Integer idEmployeeInserted = null;
		Integer idCompanyInserted = null;

		try {
			Session session = sessionFactory.getCurrentSession();

			log.info("Statring company signUp...");			
			companyDTOResult = (CompanyDTO) companyServicesInterface.signUp(companyDTO);
			session.getTransaction().commit();
			
			if(companyDTOResult!=null) {
				log.info("signUp OK");
				idCompanyInserted=Integer.valueOf(companyDTOResult.getUsrId());
			}
			
			employeeDTO.setCpyUsrId(companyDTOResult.getUsrId());

			log.info("Starting employee signUp...");
			org.hibernate.Transaction txn2 = session.beginTransaction();
			employeeDTO = (EmployeeDTO) employeeServicesInterface.signUp(employeeDTO);
			txn2.commit();	

			if(employeeDTO!=null) {
				log.info("signUp OK");

				//check invitation code
				DTO dtoFromCheckInvitationCode = employeeServicesInterface.checkInvitationCode("WRONG_INVITATION_CODE");

				//delete data just inserted in DB
				idEmployeeInserted=Integer.valueOf(employeeDTO.getUsrId());
				log.info("executing rollback...");
				rollBack(idEmployeeInserted, idCompanyInserted, session);
				log.info("rollback OK");

				//check login
				log.info("assert checking invitation code...");
				assertTrue(dtoFromCheckInvitationCode!=null);		
				log.info("checking invitation code OK");

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
	
//------------CONFIRM-------------------------------------------
	
	@Test
	public void confirmTest() {	

		CompanyDTO companyDTO = fillCompanyDTO();
		CompanyDTO companyDTOResult = null;
		EmployeeDTO employeeDTO = fillEmployeeDTO();	
		Integer idEmployeeInserted = null;
		Integer idCompanyInserted = null;

		try {
			Session session = sessionFactory.getCurrentSession();

			log.info("Statring company signUp...");			
			companyDTOResult = (CompanyDTO) companyServicesInterface.signUp(companyDTO);
			session.getTransaction().commit();
			
			if(companyDTOResult!=null) {
				log.info("signUp OK");
				idCompanyInserted=Integer.valueOf(companyDTOResult.getUsrId());
			}
			
			employeeDTO.setCpyUsrId(companyDTOResult.getUsrId());

			log.info("Starting employee signUp...");
			org.hibernate.Transaction txn2 = session.beginTransaction();
			employeeDTO = (EmployeeDTO) employeeServicesInterface.signUp(employeeDTO);
			txn2.commit();	
			
			if(employeeDTO!=null) {
				log.info("signUp OK");
				idEmployeeInserted=Integer.valueOf(employeeDTO.getUsrId());

				
				//confirm
				org.hibernate.Transaction txn = session.beginTransaction();
				DTO dtoFromConfirm = employeeServicesInterface.confirm(employeeDTO);
				txn.commit();
				
				log.info("get data from DB...");
				//recover the User
				User user = new User();
				user = userDaoImpl.findByPrimaryKey(user, idEmployeeInserted);				
				//recover the transaction
				Transaction transaction = new Transaction();
				transaction = transactionDaoImpl.getLastTransaction(idEmployeeInserted);	

				//delete data just inserted in DB
				idEmployeeInserted=Integer.valueOf(employeeDTO.getUsrId());
				log.info("executing rollback...");
				rollBack(idEmployeeInserted, idCompanyInserted, session);
				log.info("rollback OK");


				//check confirm output
				log.info("assert confirm...");
				assertTrue(dtoFromConfirm!=null);		
				log.info("confirm OK");
				
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
	
	//TEST KO: try confirm an employee with wrong ID
	@Test
	public void confirmTest_notExistingID_KO() {	

		CompanyDTO companyDTO = fillCompanyDTO();
		CompanyDTO companyDTOResult = null;
		EmployeeDTO employeeDTO = fillEmployeeDTO();	
		Integer idEmployeeInserted = null;
		Integer idCompanyInserted = null;

		try {
			Session session = sessionFactory.getCurrentSession();

			log.info("Statring company signUp...");			
			companyDTOResult = (CompanyDTO) companyServicesInterface.signUp(companyDTO);
			session.getTransaction().commit();
			
			if(companyDTOResult!=null) {
				log.info("signUp OK");
				idCompanyInserted=Integer.valueOf(companyDTOResult.getUsrId());
			}
			
			employeeDTO.setCpyUsrId(companyDTOResult.getUsrId());

			log.info("Starting employee signUp...");
			org.hibernate.Transaction txn2 = session.beginTransaction();
			employeeDTO = (EmployeeDTO) employeeServicesInterface.signUp(employeeDTO);
			txn2.commit();		
			
			if(employeeDTO!=null) {
				log.info("signUp OK");
				idEmployeeInserted=Integer.valueOf(employeeDTO.getUsrId());

				//set not existing ID
				employeeDTO.setUsrId("9999");
				
				//confirm
				org.hibernate.Transaction txn = session.beginTransaction();
				DTO dtoFromConfirm;
				try {
					dtoFromConfirm = employeeServicesInterface.confirm(employeeDTO);
				} catch (Exception e) {
					txn.commit();
					//rollback
					log.info("executing rollback...");
					rollBack(idEmployeeInserted, idCompanyInserted, session);
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
	

	private void rollBack(Integer idEmployeeInserted, Integer idCompanyInserted, Session session) {

		//delete the Transaction
		org.hibernate.Transaction txn = session.beginTransaction();
		Query query = session.createQuery("delete from Transaction where usr_id_da = " + idCompanyInserted + " or usr_id_a = " + idCompanyInserted);
		query.executeUpdate();				
		txn.commit();
		
		//delete the Transaction
		org.hibernate.Transaction txn1 = session.beginTransaction();
		Query query1 = session.createQuery("delete from Transaction where usr_id_da = " + idEmployeeInserted +  " or usr_id_a = " + idEmployeeInserted);
		query1.executeUpdate();				
		txn1.commit();
		
		//delete the Employee
		org.hibernate.Transaction txn2 = session.beginTransaction();
		Query query2 = session.createQuery("delete from Employee where usr_id = " + idEmployeeInserted);
		query2.executeUpdate();				
		txn2.commit();

		//delete the Employee User
		org.hibernate.Transaction txn3 = session.beginTransaction();
		Query query3 = session.createQuery("delete from User where usr_id = " + idEmployeeInserted);
		query3.executeUpdate();				
		txn3.commit();
		
		//delete the Company
		org.hibernate.Transaction txn4 = session.beginTransaction();
		Query query4 = session.createQuery("delete from Company where usr_id = " + idCompanyInserted);
		query4.executeUpdate();				
		txn4.commit();

		//delete the Company User
		org.hibernate.Transaction txn5 = session.beginTransaction();
		Query query5 = session.createQuery("delete from User where usr_id = " + idCompanyInserted);
		query5.executeUpdate();				
		txn5.commit();
	}
	
	private EmployeeDTO fillEmployeeDTO() {
		EmployeeDTO dto = new EmployeeDTO();
		dto.setUsrEmail("stefanoliga12@libero.it");
		dto.setUsrPassword("Employee.654");
		dto.setEmpFirstName("Giorgio");
		dto.setEmpLastName("Bianchi");
		return dto;
	}
	
	private EmployeeDTO fillEmployeeDTO_noFirstName() {
		EmployeeDTO dto = new EmployeeDTO();
		dto.setUsrEmail("employee@gmail.com");
		dto.setUsrPassword("Employee.654");
		dto.setEmpLastName("Bianchi");
		return dto;
	}
	
	private EmployeeDTO fillEmployeeForModProfile(String usrID) {
		EmployeeDTO dto = new EmployeeDTO();
		dto.setUsrId(usrID);
		dto.setUsrEmail("newEmployeeMail@gmail.com");
		dto.setUsrPassword("NewPassword.654");
		dto.setEmpFirstName("Maurizio");
		dto.setEmpLastName("Verdi");
		return dto;

	}

	private CompanyDTO fillCompanyDTO() {
		CompanyDTO dto = new CompanyDTO();
		dto.setUsrEmail("liga@gmail.com");
		dto.setUsrPassword("Liga.654");
		dto.setCpyPartitaIva("000PARTITAIVA000");
		dto.setCpyCodiceFiscale("000CODICEFISCALE000");
		dto.setCpyPec("stefano.liga1990@gmail.com");
		return dto;
	}
	
}
