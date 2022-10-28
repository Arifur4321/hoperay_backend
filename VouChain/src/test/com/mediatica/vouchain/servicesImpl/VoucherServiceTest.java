package test.com.mediatica.vouchain.servicesImpl;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

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

import com.mediatica.vouchain.dto.CompanyDTO;
import com.mediatica.vouchain.dto.VoucherDTO;
import com.mediatica.vouchain.entities.Voucher;
import com.mediatica.vouchain.exceptions.EmailYetInTheSystemException;
import com.mediatica.vouchain.servicesImpl.VoucherServiceImpl;
import com.mediatica.vouchain.servicesInterface.CompanyServicesInterface;

@Transactional
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("file:WebContent/WEB-INF/vouchain-servlet.xml")
@WebAppConfiguration
public class VoucherServiceTest {

	@Autowired
	SessionFactory sessionFactory;	
	
	@Autowired
	VoucherServiceImpl voucherServiceImpl;
		
	@Autowired
	CompanyServicesInterface companyServicesInterface;


	
	private static org.slf4j.Logger log = LoggerFactory.getLogger(VoucherServiceImpl.class);
	

	
	//------------ADD NEW VOUCHER TYPE----------------------------------------

	@Test
	public void addNewVoucherTypeTest() {	
		
		CompanyDTO dto = fillCompanyDTO();	
		CompanyDTO dtoResult = null;
		Integer idUserInserted = null;
		
		try {
			
			log.info("Statring signUp...");
			dtoResult = (CompanyDTO) companyServicesInterface.signUp(dto);
			Session session = sessionFactory.getCurrentSession();
			session.getTransaction().commit();	
			
			log.info("Filling voucher...");
			VoucherDTO voucherDTO = fillVoucherDTO();	
			voucherDTO.setCompanyId(dtoResult.getUsrId());
			
			log.info("adding new voucher...");
			org.hibernate.Transaction txn1 = session.beginTransaction();
			voucherServiceImpl.addNewVoucherType(voucherDTO);
			txn1.commit();	
			log.info("voucher added");
			
			log.info("get data from DB...");			
			//recover the Voucher			
			org.hibernate.Transaction txn2 = session.beginTransaction();
			Query query1 = session.createQuery("from Voucher where vch_name = '" + voucherDTO.getVchName() + "'");
			Voucher voucherResult= (Voucher) query1.getSingleResult();				
			txn2.commit();
						
			idUserInserted=Integer.valueOf(dtoResult.getUsrId());

			//delete data just inserted in DB		
			log.info("executing rollback...");
			rollBackForAddNewVoucher(voucherDTO.getVchName(), session, Integer.valueOf(dtoResult.getUsrId()), idUserInserted);
			log.info("rollback OK");
			
			assertTrue(voucherResult!=null);
			
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
	


	//Test KO: try to add voucher with no company ID
	@Test
	public void addNewVoucherTypeTest_nullName_KO() {	
		
		CompanyDTO dto = fillCompanyDTO();	
		CompanyDTO dtoResult = null;
		Integer idUserInserted = null;
		
		try {
			
			log.info("Statring signUp...");
			dtoResult = (CompanyDTO) companyServicesInterface.signUp(dto);
			Session session = sessionFactory.getCurrentSession();
			session.getTransaction().commit();	
			
			log.info("Filling voucher...");
			VoucherDTO voucherDTO = fillVoucherDTO();

			//set company ID null
			voucherDTO.setCompanyId(null);			
			
			idUserInserted=Integer.valueOf(dtoResult.getUsrId());

			
			log.info("adding new voucher...");
			org.hibernate.Transaction txn1 = session.beginTransaction();
			try {
				voucherServiceImpl.addNewVoucherType(voucherDTO);
				txn1.commit();	

			} catch (Exception e) {
				txn1.rollback();	
				
				//delete data just inserted in DB		
				log.info("executing rollback...");
				rollBackForAddNewVoucher(voucherDTO.getVchName(), session, Integer.valueOf(dtoResult.getUsrId()), idUserInserted);
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
	



//------------UTILITIES----------------------------------------

	
	private VoucherDTO fillVoucherDTO() {
		VoucherDTO voucherDTO = new VoucherDTO();
		voucherDTO.setVchName("voucherNameTest");
		voucherDTO.setVchCreationDate("2019-12-20");
		voucherDTO.setVchEndDate("2020-12-20");
		voucherDTO.setVchValue("100");
		return voucherDTO;
	}

	
	
	private void rollBackForAddNewVoucher(String vchName, Session session, Integer userID, Integer idUserInserted) {
		
		//delete transaction detail
		deleteTransactionDetailByVchName(vchName, session);
		
		//delete voucher
		deleteVoucherByVchName(vchName, session);
		
		//delete the Transaction	
		deleteTransactionByUsrId(userID.toString(), session);
		
		//delete the Company		
		deleteCompanyByID(idUserInserted.toString(), session);
		
		//delete the User		
		deleteUserByID(idUserInserted.toString(), session);
		
	}



	private void deleteCompanyByID(String idCompanyInserted, Session session) {
		org.hibernate.Transaction txn8 = session.beginTransaction();
		Query query8 = session.createQuery("delete from Company where usr_id = " + idCompanyInserted);
		query8.executeUpdate();				
		txn8.commit();
	}



	private void deleteUserByID(String idEmployeeInserted, Session session) {
		org.hibernate.Transaction txn7 = session.beginTransaction();
		Query query7 = session.createQuery("delete from User where usr_id = " + idEmployeeInserted);
		query7.executeUpdate();				
		txn7.commit();
	}



	private void deleteTransactionByUsrId(String idCompanyInserted, Session session) {
		org.hibernate.Transaction txn5 = session.beginTransaction();
		Query query5 = session.createQuery("delete from Transaction where usr_id_da = " + idCompanyInserted + " or usr_id_a = " + idCompanyInserted);
		query5.executeUpdate();				
		txn5.commit();
	}



	private void deleteVoucherByVchName(String vchName1, Session session) {
		org.hibernate.Transaction txn2 = session.beginTransaction();
		Query query2 = session.createQuery("delete from Voucher where vch_name = '" + vchName1 + "'");
		query2.executeUpdate();				
		txn2.commit();
	}



	private void deleteTransactionDetailByVchName(String vchName1, Session session) {
		org.hibernate.Transaction txn1 = session.beginTransaction();
		Query query1 = session.createQuery("delete from TransactionDetail where vch_name = '" + vchName1 + "'");
		query1.executeUpdate();				
		txn1.commit();
	}	
	
	
	
	private CompanyDTO fillCompanyDTO() {
		CompanyDTO dto = new CompanyDTO();
		dto.setUsrEmail("stefano.liga1990@gmail.com");
		dto.setUsrPassword("Liga.654");
		dto.setCpyPartitaIva("000PARTITAIVA000");
		dto.setCpyCodiceFiscale("000CODICEFISCALE000");
		dto.setCpyPec("stefano.liga1990@gmail.com");
		dto.setCpyRagioneSociale("ragione Sociale Test");
		return dto;
	}


}
