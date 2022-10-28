package test.com.mediatica.vouchain.dao;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.List;

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
import com.mediatica.vouchain.dto.CompanyDTO;
import com.mediatica.vouchain.dto.EmployeeDTO;
import com.mediatica.vouchain.entities.Employee;
import com.mediatica.vouchain.servicesInterface.CompanyServicesInterface;
import com.mediatica.vouchain.servicesInterface.EmployeeServicesInterface;

@Transactional
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("file:WebContent/WEB-INF/vouchain-servlet.xml")
@WebAppConfiguration
public class EmployeeDaoTest {
	
	private static org.slf4j.Logger log = LoggerFactory.getLogger(EmployeeDaoTest.class);
	
	@Autowired
	SessionFactory sessionFactory;	
	
	@Autowired
	EmployeeServicesInterface employeeServicesInterface;
	
	@Autowired
	CompanyServicesInterface companyServicesInterface;
	
	@Autowired
	EmployeeDaoImpl employeeDaoImpl;
	
	
	//-------------LIST EMPLOYEE BY COMPANY ID---------------------------
	
	@Test
	public void listEmployeesByCompanyIdTest() {
		
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
			
			List<Employee> employeeList = employeeDaoImpl.listEmployeesByCompanyId(idCompanyInserted);
			
			
			//delete data just inserted in DB		
			log.info("executing rollback...");
			rollBack(idEmployeeInserted, idCompanyInserted, session);
			log.info("rollback OK");
			
			assertTrue(employeeList!=null && employeeList.size()>0);
		}
		catch(Exception e) {
			fail();
			e.printStackTrace();
		}
		
	}
	
	//TEST KO: try to recover employee list with wrong company id
	@Test
	public void listEmployeesByCompanyIdTest_wrongCompanyID_KO() {
		
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
			
			//try to recover employee list with wrong company id
			List<Employee> employeeList = employeeDaoImpl.listEmployeesByCompanyId(9999);
			
			
			//delete data just inserted in DB		
			log.info("executing rollback...");
			rollBack(idEmployeeInserted, idCompanyInserted, session);
			log.info("rollback OK");
			
			assertTrue(employeeList!=null && employeeList.size()>0);
		}
		catch(Exception e) {
			fail();
			e.printStackTrace();
		}
		
	}
	
	
	
	
	//-------------FIND EMPLOYEE BY INVITATION CODE---------------------------
	
	
	
	@Test
	public void findByInvitationCodeTest() {
		
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
			
			log.info("retrieving just inserted employee...");
			Employee employee = new Employee();
			employee = employeeDaoImpl.findByPrimaryKey(employee, idEmployeeInserted);
			
			log.info("getting employee by invitation code");
			Employee employeeByInvitationCode = employeeDaoImpl.findByInvitationCode(employee.getEmpInvitationCode());			
			
			//delete data just inserted in DB		
			log.info("executing rollback...");
			rollBack(idEmployeeInserted, idCompanyInserted, session);
			log.info("rollback OK");
			
			assertTrue(employeeByInvitationCode!=null && employeeByInvitationCode.getUsrId()!=null);
		}
		catch(Exception e) {
			fail();
			e.printStackTrace();
		}
		
	}
	
	//TEST KO: try to recovery employee with a wrong invitation code
	@Test
	public void findByInvitationCodeTest_wrongInvitationCode() {
		
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
			
			log.info("retrieving just inserted employee...");
			Employee employee = new Employee();
			employee = employeeDaoImpl.findByPrimaryKey(employee, idEmployeeInserted);
			
			log.info("getting employee by invitation code");
			//try to recovery employee with a wrong invitation code
			Employee employeeByInvitationCode = employeeDaoImpl.findByInvitationCode("wrongInvitationCode");			
			
			//delete data just inserted in DB		
			log.info("executing rollback...");
			rollBack(idEmployeeInserted, idCompanyInserted, session);
			log.info("rollback OK");
			
			assertTrue(employeeByInvitationCode!=null && employeeByInvitationCode.getUsrId()!=null);
		}
		catch(Exception e) {
			fail();
			e.printStackTrace();
		}
		
	}
	
	
	
	
	//-------------UTILITIES---------------------------
	
	private CompanyDTO fillCompanyDTO() {
		CompanyDTO dto = new CompanyDTO();
		dto.setUsrEmail("liga@gmail.com");
		dto.setUsrPassword("Liga.654");
		dto.setCpyPartitaIva("000PARTITAIVA000");
		dto.setCpyCodiceFiscale("000CODICEFISCALE000");
		dto.setCpyPec("stefano.liga1990@gmail.com");
		return dto;
	}
	
	private EmployeeDTO fillEmployeeDTO() {
		EmployeeDTO dto = new EmployeeDTO();
		dto.setUsrEmail("stefanoliga12@libero.it");
		dto.setUsrPassword("Employee.654");
		dto.setEmpFirstName("Giorgio");
		dto.setEmpLastName("Bianchi");
		return dto;
	}
	
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
}
