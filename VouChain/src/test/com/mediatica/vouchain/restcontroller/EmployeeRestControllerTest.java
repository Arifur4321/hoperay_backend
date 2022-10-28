package test.com.mediatica.vouchain.restcontroller;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
import com.mediatica.vouchain.dao.EmployeeDaoImpl;
import com.mediatica.vouchain.dao.TransactionDaoImpl;
import com.mediatica.vouchain.dao.UserDaoImpl;
import com.mediatica.vouchain.dto.CompanyDTO;
import com.mediatica.vouchain.dto.DTO;
import com.mediatica.vouchain.dto.DTOList;
import com.mediatica.vouchain.dto.EmployeeDTO;
import com.mediatica.vouchain.entities.Employee;
import com.mediatica.vouchain.entities.Transaction;
import com.mediatica.vouchain.entities.User;
import com.mediatica.vouchain.exceptions.EmailYetInTheSystemException;
import com.mediatica.vouchain.servicesInterface.CompanyServicesInterface;
import com.mediatica.vouchain.servicesInterface.EmployeeServicesInterface;
import com.mediatica.vouchain.utilities.EncrptingUtils;

@Transactional
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("file:WebContent/WEB-INF/vouchain-servlet.xml")
@WebAppConfiguration
public class EmployeeRestControllerTest {

	private static org.slf4j.Logger log = LoggerFactory.getLogger(EmployeeRestControllerTest.class);

	@Autowired
	private EncrptingUtils encrptingUtils;

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

	@Value("${employee_auth_username}")
	private String EMPLOYEE_USERNAME;

	@Value("${employee_auth_password}")
	private String EMPLOYEE_PASSWORD;

	private String EMPLOYEE_LOGIN_URI="http://localhost:8080/VouChain/api/employees/employeeLogin";

	private String SHOW_EMPLOYEE_PROFILE_URI="http://localhost:8080/VouChain/api/employees/showEmployeeProfile";

	private String EMPLOYEE_SIGNUP_URI="http://localhost:8080/VouChain/api/employees/employSignUp";

	private String SHOW_EMPLOYEE_LIST_URI="http://localhost:8080/VouChain/api/employees/showEmployeesList";

	private String CHECK_INVITATION_CODE_URI="http://localhost:8080/VouChain/api/employees/checkInvitationCode";

	private String MODIFY_EMPLOYEE_PROFILE_URI="http://localhost:8080/VouChain/api/employees/modEmployeeProfile";

	private String EMPLOYEE_CONFIRMATION_URI="http://localhost:8080/VouChain/api/employees/employeeConfirmation";



	//------------LOGIN--------------------------------------------


	@Test
	public void employeeLoginTest() {		

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
				
				log.info("get data from DB...");
				//recover the User
				User user = new User();
				user = userDaoImpl.findByPrimaryKey(user, idEmployeeInserted);	
				
				employeeDTO.setUsrPassword(user.getUsrPassword());
				
				//confirm					
				EmployeeDTO dtoFromConfirm = invokeEmployeeConfirmation(employeeDTO);			


				//----------TEST------------
				DTO dtoFromLogin = invokeEmployeeLogin(employeeDTO);
				//--------------------------
				

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
	

	//TEST KO: try to login with wrong email
	@Test
	public void employeeLoginTest_wronEmail_KO() {		

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
				
				log.info("get data from DB...");
				//recover the User
				User user = new User();
				user = userDaoImpl.findByPrimaryKey(user, idEmployeeInserted);	
				
				employeeDTO.setUsrPassword(user.getUsrPassword());
				
				//confirm					
				EmployeeDTO dtoFromConfirm = invokeEmployeeConfirmation(employeeDTO);			


				//----------TEST------------
				employeeDTO.setUsrEmail("wrongEmail@gmail.com");
				DTO dtoFromLogin = invokeEmployeeLogin(employeeDTO);
				//--------------------------
				

				//delete data just inserted in DB
				idEmployeeInserted=Integer.valueOf(employeeDTO.getUsrId());
				log.info("executing rollback...");
				rollBack(idEmployeeInserted, idCompanyInserted, session);
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

				//----------TEST------------
				//showProfile
				DTO profile = invokeShowEmployeeProfile(employeeDTOResult.getUsrId());
				//--------------------------
				
				
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

				
				//----------TEST------------
				//try to show proile with a not existing user id
				DTO profile = invokeShowEmployeeProfile("9999");
				//--------------------------
				
				//delete data just inserted in DB
				idEmployeeInserted=Integer.valueOf(employeeDTOResult.getUsrId());
				log.info("executing rollback...");
				rollBack(idEmployeeInserted, idCompanyInserted, session);
				log.info("rollback OK");


				//check showProfile output
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
		CompanyDTO companyDTO = fillCompanyDTO();
		CompanyDTO companyDTOResult = null;
		EmployeeDTO employeeDTO = fillEmployeeDTO();	
		DTOList<EmployeeDTO> employeeDTOResult = null;
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
			List<EmployeeDTO> employeeList = new ArrayList<EmployeeDTO>();
			employeeList.add(employeeDTO);
			
			
			//----------TEST------------
			employeeDTOResult = invokeEmployeeSignup(employeeList);
			//--------------------------
			
			
			if(employeeDTOResult!=null) {
				log.info("signUp OK");
				idEmployeeInserted=Integer.valueOf(employeeDTOResult.getList().get(0).getUsrId());
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
		EmployeeDTO employeeDTO = fillEmployeeDTO();	
		DTOList<EmployeeDTO> employeeDTOResult = null;
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
			employeeDTO.setEmpFirstName(null);

			log.info("Starting employee signUp...");
			List<EmployeeDTO> employeeList = new ArrayList<EmployeeDTO>();
			employeeList.add(employeeDTO);
			
			//----------TEST------------
			employeeDTOResult = invokeEmployeeSignup(employeeList);
			//--------------------------
			
			if(employeeDTOResult!=null && employeeDTOResult.getStatus().equals(Constants.RESPONSE_STATUS.OK.toString())) {
				log.info("signUp OK");
				idEmployeeInserted=Integer.valueOf(employeeDTOResult.getList().get(0).getUsrId());
			}

			//delete data just inserted in DB		
			log.info("executing rollback...");
			rollBack(idEmployeeInserted, idCompanyInserted, session);
			log.info("rollback OK");
			
			//check employee insert
			log.info("assert Employee insert...");
			assertTrue(employeeDTOResult!=null && employeeDTOResult.getStatus().equals(Constants.RESPONSE_STATUS.OK.toString()));		
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

					//----------TEST------------
					//get employee list by company id
					DTOList<EmployeeDTO> dtoList = invokeShowEmployeesList(employeeDTO.getCpyUsrId());
					//--------------------------

					//delete data just inserted in DB
					idEmployeeInserted=Integer.valueOf(employeeDTO.getUsrId());
					log.info("executing rollback...");
					rollBack(idEmployeeInserted, idCompanyInserted, session);
					log.info("rollback OK");

					//check get employee list
					log.info("assert employee list...");
					assertTrue(dtoList!=null && dtoList.getList()!=null && dtoList.getList().size()>0);		
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
		
		//TEST KO: try to get employee list with not existing company ID
		@Test
		public void getEmployeeListTest_notExistingCompanyId() {	

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

					
					//----------TEST------------
					//get employee list by not existing company id
					DTOList<EmployeeDTO> dtoList = invokeShowEmployeesList("9999");
					//--------------------------
					

					//delete data just inserted in DB
					idEmployeeInserted=Integer.valueOf(employeeDTO.getUsrId());
					log.info("executing rollback...");
					rollBack(idEmployeeInserted, idCompanyInserted, session);
					log.info("rollback OK");

					//check get employee list
					log.info("assert employee list...");
					assertTrue(dtoList!=null && dtoList.getList()!=null && dtoList.getList().size()>0);		
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
					//----------TEST------------
					EmployeeDTO dtoFromCheckInvitationCode = invokeCheckInvitationCode(employeeDTO.getEmpInvitationCode());
					//--------------------------
					
					//delete data just inserted in DB
					idEmployeeInserted=Integer.valueOf(employeeDTO.getUsrId());
					log.info("executing rollback...");
					rollBack(idEmployeeInserted, idCompanyInserted, session);
					log.info("rollback OK");

					//check login
					log.info("assert checking invitation code...");
					assertTrue(dtoFromCheckInvitationCode!=null && dtoFromCheckInvitationCode.getUsrId()!=null);		
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

					//check invitation code with a wrong invitatrion code
					//----------TEST------------
					EmployeeDTO dtoFromCheckInvitationCode = invokeCheckInvitationCode("WRONG_INVITATION_CODE");
					//--------------------------
					
					//delete data just inserted in DB
					idEmployeeInserted=Integer.valueOf(employeeDTO.getUsrId());
					log.info("executing rollback...");
					rollBack(idEmployeeInserted, idCompanyInserted, session);
					log.info("rollback OK");

					//check login
					log.info("assert checking invitation code...");
					assertTrue(dtoFromCheckInvitationCode!=null && dtoFromCheckInvitationCode.getUsrId()!=null);		
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
					//----------TEST------------
					EmployeeDTO dtoAfterModify = invokeModEmployeeProfile(modifiedEmployee);
					//--------------------------

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
		
		

		//TEST KO: try to modify profile with an invalid email
		@Test
		public void modProfileTest_invalidEmail_KO() {	
			
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
					//----------TEST------------
					modifiedEmployee.setUsrEmail("INVALID_EMAIL");
					EmployeeDTO dtoAfterModify = invokeModEmployeeProfile(modifiedEmployee);
					//--------------------------

					//delete data just inserted in DB
					idEmployeeInserted=Integer.valueOf(modifiedEmployee.getUsrId());
					log.info("executing rollback...");
					rollBack(idEmployeeInserted, idCompanyInserted, session);
					log.info("rollback OK");

					//check employee update
					log.info("assert modProfile...");
					assertTrue(dtoAfterModify!=null && dtoAfterModify.getUsrId()!=null && dtoAfterModify.getStatus().equals(Constants.RESPONSE_STATUS.OK.toString()));
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
					
					log.info("get data from DB...");
					//recover the User
					User user = new User();
					user = userDaoImpl.findByPrimaryKey(user, idEmployeeInserted);	
					
					employeeDTO.setUsrPassword(user.getUsrPassword());
					
					//confirm					
					//----------TEST------------
					EmployeeDTO dtoFromConfirm = invokeEmployeeConfirmation(employeeDTO);				
					//--------------------------
					
			
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
					assertTrue(dtoFromConfirm!=null && dtoFromConfirm.getStatus().equals(Constants.RESPONSE_STATUS.OK.toString()));	
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
		
		//TEST KO: try to confirm profile with an invalid email
		@Test
		public void confirmTest_invalidEmail_KO() {	

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
					
					log.info("get data from DB...");
					//recover the User
					User user = new User();
					user = userDaoImpl.findByPrimaryKey(user, idEmployeeInserted);	
					
					employeeDTO.setUsrPassword(user.getUsrPassword());
					employeeDTO.setUsrPassword("INVALID_EMAIL");

					
					//confirm	
					//----------TEST------------
					EmployeeDTO dtoFromConfirm = invokeEmployeeConfirmation(employeeDTO);					
					//--------------------------
			
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
					assertTrue(dtoFromConfirm!=null && dtoFromConfirm.getStatus().equals(Constants.RESPONSE_STATUS.OK.toString()));	
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
	//-----------------INVOKE METHODS--------------------

	private EmployeeDTO invokeEmployeeLogin(EmployeeDTO request) {
		EmployeeDTO responseDTO=null;
		try {
			Client client = configureClient();		
			logRequest(request,"invokeEmployeeLogin");		
			Response response  = client
					.target(EMPLOYEE_LOGIN_URI)
					.request(MediaType.APPLICATION_JSON)
					.post(Entity.entity(request, MediaType.APPLICATION_JSON));
			response.bufferEntity();
			responseDTO  =  response.readEntity(EmployeeDTO.class);
		} catch (Exception e) {
			log.error("Error Invoking Employee Rest Controller",e);
			e.printStackTrace();
		}
		return responseDTO;		
	}

	private EmployeeDTO invokeShowEmployeeProfile(String request) {
		EmployeeDTO responseDTO=null;
		try {
			Client client = configureClient();		
			logRequest(request,"invokeShowEmployeeProfile");		
			Response response  = client
					.target(SHOW_EMPLOYEE_PROFILE_URI)
					.path(request)
					.request(MediaType.APPLICATION_JSON)
					.get();
			response.bufferEntity();
			responseDTO  =  response.readEntity(EmployeeDTO.class);
		} catch (Exception e) {
			log.error("Error Invoking Employee Rest Controller",e);
			e.printStackTrace();
		}
		return responseDTO;		
	}

	private DTOList<EmployeeDTO> invokeEmployeeSignup(List<EmployeeDTO> employeeList) {
		DTOList<EmployeeDTO> responseDTO=null;
		try {
			Client client = configureClient();		
			logRequest(employeeList,"invokeEmployeeSignup");		
			Response response  = client
					.target(EMPLOYEE_SIGNUP_URI)
					.request(MediaType.APPLICATION_JSON)
					.post(Entity.entity(employeeList, MediaType.APPLICATION_JSON));
			response.bufferEntity();			
			responseDTO  =  response.readEntity(new GenericType<DTOList<EmployeeDTO>>() {});
			
		} catch (Exception e) {
			log.error("Error Invoking Employee Rest Controller",e);
			e.printStackTrace();
		}
		return responseDTO;		
	}

	private DTOList<EmployeeDTO> invokeShowEmployeesList(String request) {
		DTOList<EmployeeDTO> responseDTO=null;
		try {
			Client client = configureClient();		
			logRequest(request,"invokeShowEmployeesList");		
			Response response  = client
					.target(SHOW_EMPLOYEE_LIST_URI)
					.path(request)
					.request(MediaType.APPLICATION_JSON)
					.get();
			response.bufferEntity();
			responseDTO  =  response.readEntity(new GenericType<DTOList<EmployeeDTO>>() {});
		} catch (Exception e) {
			log.error("Error Invoking Employee Rest Controller",e);
			e.printStackTrace();
		}
		return responseDTO;		
	}

	private EmployeeDTO invokeCheckInvitationCode(String request) {
		EmployeeDTO responseDTO=null;
		try {
			Client client = configureClient();		
			logRequest(request,"invokeCheckInvitationCode");		
			Response response  = client
					.target(CHECK_INVITATION_CODE_URI)
					.path(request)
					.request(MediaType.APPLICATION_JSON)
					.get();
			response.bufferEntity();
			responseDTO  =  response.readEntity(EmployeeDTO.class);
		} catch (Exception e) {
			log.error("Error Invoking Employee Rest Controller",e);
			e.printStackTrace();
		}
		return responseDTO;		
	}

	private EmployeeDTO invokeModEmployeeProfile(EmployeeDTO request) {
		EmployeeDTO responseDTO=null;
		try {
			Client client = configureClient();		
			logRequest(request,"invokeModEmployeeProfile");		
			Response response  = client
					.target(MODIFY_EMPLOYEE_PROFILE_URI)
					.request(MediaType.APPLICATION_JSON)
					.post(Entity.entity(request, MediaType.APPLICATION_JSON));
			response.bufferEntity();
			responseDTO  =  response.readEntity(EmployeeDTO.class);
		} catch (Exception e) {
			log.error("Error Invoking Employee Rest Controller",e);
			e.printStackTrace();
		}
		return responseDTO;		
	}

	private EmployeeDTO invokeEmployeeConfirmation(EmployeeDTO request) {
		EmployeeDTO responseDTO=null;
		try {
			Client client = configureClient();		
			logRequest(request,"invokeEmployeeConfirmation");		
			Response response  = client
					.target(EMPLOYEE_CONFIRMATION_URI)
					.request(MediaType.APPLICATION_JSON)
					.post(Entity.entity(request, MediaType.APPLICATION_JSON));
			response.bufferEntity();
			responseDTO  =  response.readEntity(EmployeeDTO.class);
		} catch (Exception e) {
			log.error("Error Invoking Employee Rest Controller",e);
			e.printStackTrace();
		}
		return responseDTO;		
	}


	//-----UTILITIES-----------------


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
		dto.setUsrEmail("vouchainsystem@gmail.com");
		dto.setUsrPassword("Employee.654");
		dto.setEmpFirstName("Giorgio ‡‡‡‡‡");
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

	private CompanyDTO fillCompanyDTO() {
		CompanyDTO dto = new CompanyDTO();
		dto.setUsrEmail("liga@gmail.com");
		dto.setUsrPassword("Liga.654");
		dto.setCpyPartitaIva("000PARTITAIVA000");
		dto.setCpyCodiceFiscale("000CODICEFISCALE000");
		dto.setCpyPec("stefano.liga1990@xxxxx.com");
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

	private Client configureClient() {

		ClientConfig configuration = new ClientConfig();		
		Client client = ClientBuilder.newClient(configuration);
		HttpAuthenticationFeature feature;
		try {
			feature = HttpAuthenticationFeature.basic(EMPLOYEE_USERNAME, encrptingUtils.decrypt(EMPLOYEE_PASSWORD.trim()));
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
		log.debug("********************* Employee method {}Json string request is {}",method,jsonString);
	}



}
