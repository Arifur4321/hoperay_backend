package test.com.mediatica.vouchain.dao;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.ArrayList;
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
import com.mediatica.vouchain.config.enumerations.TransactionType;
import com.mediatica.vouchain.dao.TransactionDaoImpl;
import com.mediatica.vouchain.dao.TransactionDetailDaoImpl;
import com.mediatica.vouchain.dao.VoucherDaoImpl;
import com.mediatica.vouchain.dto.CompanyDTO;
import com.mediatica.vouchain.dto.DTO;
import com.mediatica.vouchain.dto.TransactionDTO;
import com.mediatica.vouchain.dto.VoucherDTO;
import com.mediatica.vouchain.entities.Transaction;
import com.mediatica.vouchain.entities.TransactionDetail;
import com.mediatica.vouchain.entities.TransactionDetailPK;
import com.mediatica.vouchain.entities.User;
import com.mediatica.vouchain.entities.Voucher;
import com.mediatica.vouchain.exceptions.EmailYetInTheSystemException;
import com.mediatica.vouchain.servicesInterface.CompanyServicesInterface;
import com.mediatica.vouchain.servicesInterface.TransactionServiceInterface;

@Transactional
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("file:WebContent/WEB-INF/vouchain-servlet.xml")
@WebAppConfiguration
public class TransactionDetailDaoTest {
	
	private static org.slf4j.Logger log = LoggerFactory.getLogger(TransactionDetailDaoTest.class);
	
	@Autowired
	SessionFactory sessionFactory;
	
	@Autowired
	TransactionServiceInterface transactionServiceInterface;	

	@Autowired
	CompanyServicesInterface companyServicesInterface;
	
	@Autowired
	TransactionDetailDaoImpl transactionDetailDaoImpl;
	
	@Autowired
	TransactionDaoImpl transactionDao;
	
	@Autowired
	VoucherDaoImpl voucherDao;
	
	@Autowired
	TransactionDetailDaoImpl transactionDetailDao;
	
	String transactionID = "";

	
	
	@Test
	public void findByTrcIdTest() {

		CompanyDTO dto = fillCompanyDTO();	
		CompanyDTO dtoResult = null;
		Integer idUserInserted = null;

		try {
			//insert a company
			log.info("Statring signUp...");
			dtoResult = (CompanyDTO) companyServicesInterface.signUp(dto);
			Session session = sessionFactory.getCurrentSession();
			session.getTransaction().commit();	
			
			if(dtoResult!=null) {
				log.info("signUp OK");
				idUserInserted=Integer.valueOf(dtoResult.getUsrId());
				
				
				//purchase 2 voucher for the inserted company
				log.info("Purchasing vouchers...");
				List<VoucherDTO> voucherList = fillVoucherList();
				purchaseVoucherList(voucherList, dtoResult.getUsrId(), session);
				log.info("Purchasing OK");
				
				String profile = "company";
				String startDate = "2000-10-20";
				String endDate = "2020-11-21";
								
				List<TransactionDetail> transactionDetailList = transactionDetailDaoImpl.findByTrcId(transactionID);

				
				//rollback		
				log.info("executing rollback...");
				rollBack(dtoResult, idUserInserted, session, voucherList);
				log.info("rollback OK");				
				
				assertTrue(transactionDetailList!=null && transactionDetailList.size()>0);

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
	
	//TEST KO: try to find transaction detail by wrong trc id
	@Test
	public void findByTrcIdTest_wrongTrcId_KO() {

		CompanyDTO dto = fillCompanyDTO();	
		CompanyDTO dtoResult = null;
		Integer idUserInserted = null;

		try {
			//insert a company
			log.info("Statring signUp...");
			dtoResult = (CompanyDTO) companyServicesInterface.signUp(dto);
			Session session = sessionFactory.getCurrentSession();
			session.getTransaction().commit();	
			
			if(dtoResult!=null) {
				log.info("signUp OK");
				idUserInserted=Integer.valueOf(dtoResult.getUsrId());
				
				
				//purchase 2 voucher for the inserted company
				log.info("Purchasing vouchers...");
				List<VoucherDTO> voucherList = fillVoucherList();
				purchaseVoucherList(voucherList, dtoResult.getUsrId(), session);
				log.info("Purchasing OK");
				
				String profile = "company";
				String startDate = "2000-10-20";
				String endDate = "2020-11-21";
				
				//try to find transaction detail by wrong trc id
				List<TransactionDetail> transactionDetailList = transactionDetailDaoImpl.findByTrcId("9999");

				
				//rollback		
				log.info("executing rollback...");
				rollBack(dtoResult, idUserInserted, session, voucherList);
				log.info("rollback OK");				
				
				assertTrue(transactionDetailList!=null && transactionDetailList.size()>0);

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
	
	
	private CompanyDTO fillCompanyDTO() {
		CompanyDTO dto = new CompanyDTO();
		dto.setUsrEmail("liga@gmail.com");
		dto.setUsrPassword("Liga.654");
		dto.setCpyPartitaIva("000PARTITAIVA000");
		dto.setCpyCodiceFiscale("000CODICEFISCALE000");
		dto.setCpyPec("s.liga@aktsrl.com");
		return dto;
	}
	
	private List<VoucherDTO> fillVoucherList() {
		
		List<VoucherDTO> voucherListDTO = new ArrayList<VoucherDTO>();
		
		VoucherDTO dto1 = new VoucherDTO();
		dto1.setVchName("vchNameTest_1");
		dto1.setVchQuantity("10");
		dto1.setVchValue("100");
		
		VoucherDTO dto2 = new VoucherDTO();
		dto2.setVchName("vchNameTest_2");
		dto2.setVchQuantity("20");
		dto2.setVchValue("150");

		
		voucherListDTO.add(dto1);
		voucherListDTO.add(dto2);
		
		return voucherListDTO;
	}
	
	public List<VoucherDTO> purchaseVoucherList(List<VoucherDTO> voucherList, String usrId, Session session) throws ParseException {
		List<VoucherDTO> resp = new ArrayList<VoucherDTO>();
		resp=voucherList;
		for(VoucherDTO item:voucherList) {
			
			Voucher voucherToInsert = new Voucher();
			voucherToInsert.setVchName(item.getVchName());
			voucherToInsert.setVchCreationDate(new Date());
			voucherToInsert.setVchValue(new BigDecimal(item.getVchValue()));	
			
	    	org.hibernate.Transaction txn1 = session.beginTransaction();
			voucherDao.insert(voucherToInsert);
			txn1.commit();

			
	    	Transaction transaction = new Transaction();
	    	transaction.setTrcDate(new Date());
	    	transaction.setTrcType(TransactionType.COMPANY_BUY_VOUCHER.getDescription());
	    	transaction.setTrcPayed(Constants.TRANSACTION_NOT_PAYED);
	    	transaction.setTrcState(true);
	    	User company = new User();
	    	company.setUsrId(Integer.parseInt(usrId));
	    	transaction.setUsrIdA(company);

	    	org.hibernate.Transaction txn2 = session.beginTransaction();
	    	transactionDao.insert(transaction);
			txn2.commit();

	    	
	    	TransactionDetail trxDetail = new TransactionDetail();
	    	Voucher voucher = voucherDao.findByName(item.getVchName());
	    	
	    	trxDetail.setVoucher(voucher); 
	    	trxDetail.setTransaction(transaction);
	    	trxDetail.setTrdQuantity(Long.parseLong(item.getVchQuantity()));
	    	TransactionDetailPK pk= new TransactionDetailPK();
	    	pk.setTrcId(transaction.getTrcId());
	    	pk.setVchName(item.getVchName());
	    	trxDetail.setTransactionDetailPK(pk);
	    	
	    	org.hibernate.Transaction txn3 = session.beginTransaction();
	    	transactionDetailDao.insert(trxDetail);
			txn3.commit();
			
			if(transaction!=null && transaction.getTrcId()!=null) {
				transactionID = transaction.getTrcId().toString();
			}

	    }
		return resp;
	}
	
	
	private void rollBack(DTO dtoResult, Integer idUserInserted, Session session, List<VoucherDTO> voucherList) {

		
		//delete from TransactionDetail and from Voucher
		if(voucherList!=null && voucherList.size()>0) {
			for(VoucherDTO voucher : voucherList) {
				org.hibernate.Transaction txn1 = session.beginTransaction();
				Query query1 = session.createQuery("delete from TransactionDetail where vch_name = '" + voucher.getVchName() + "'");
				query1.executeUpdate();				
				txn1.commit();
				
				org.hibernate.Transaction txn2 = session.beginTransaction();
				Query query2 = session.createQuery("delete from Voucher where vch_name = '" + voucher.getVchName() + "'");
				query2.executeUpdate();				
				txn2.commit();
			}
		}
	

		//delete the Transaction for signUp (NWU)
		org.hibernate.Transaction txn2 = session.beginTransaction();
		Query query2 = session.createQuery("delete from Transaction where usr_id_da = " + idUserInserted);
		query2.executeUpdate();				
		txn2.commit();
		
		//delete the Transaction for purchase (ACV)
		org.hibernate.Transaction txn3 = session.beginTransaction();
		Query query3 = session.createQuery("delete from Transaction where usr_id_da = " + idUserInserted + " or usr_id_a = " + idUserInserted);
		query3.executeUpdate();				
		txn3.commit();
		
		
		//delete the Company
		org.hibernate.Transaction txn4 = session.beginTransaction();
		Query query4 = session.createQuery("delete from Company where usr_id = " + idUserInserted);
		query4.executeUpdate();				
		txn4.commit();
		
		//delete the Merchant
		org.hibernate.Transaction txn5 = session.beginTransaction();
		Query query5 = session.createQuery("delete from Merchant where usr_id = " + idUserInserted);
		query5.executeUpdate();				
		txn5.commit();


		//delete the User
		org.hibernate.Transaction txn6 = session.beginTransaction();
		Query query6 = session.createQuery("delete from User where usr_id = " + idUserInserted);
		query6.executeUpdate();				
		txn6.commit();
	}

}
