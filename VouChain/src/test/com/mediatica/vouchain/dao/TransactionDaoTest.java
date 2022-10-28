package test.com.mediatica.vouchain.dao;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
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
import com.mediatica.vouchain.dao.UserDaoImpl;
import com.mediatica.vouchain.dao.VoucherDaoImpl;
import com.mediatica.vouchain.dto.CompanyDTO;
import com.mediatica.vouchain.dto.DTO;
import com.mediatica.vouchain.dto.MerchantDTO;
import com.mediatica.vouchain.dto.TransactionDTO;
import com.mediatica.vouchain.dto.VoucherDTO;
import com.mediatica.vouchain.entities.Transaction;
import com.mediatica.vouchain.entities.TransactionDetail;
import com.mediatica.vouchain.entities.TransactionDetailPK;
import com.mediatica.vouchain.entities.User;
import com.mediatica.vouchain.entities.Voucher;
import com.mediatica.vouchain.exceptions.EmailYetInTheSystemException;
import com.mediatica.vouchain.servicesImpl.MerchantServiceImpl;
import com.mediatica.vouchain.servicesInterface.CompanyServicesInterface;
import com.mediatica.vouchain.servicesInterface.TransactionServiceInterface;

@Transactional
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("file:WebContent/WEB-INF/vouchain-servlet.xml")
@WebAppConfiguration
public class TransactionDaoTest {
	
	private static org.slf4j.Logger log = LoggerFactory.getLogger(TransactionDaoTest.class);
	
	@Autowired
	SessionFactory sessionFactory;	
	
	@Autowired
	TransactionDaoImpl transactionDaoImpl;
	
	@Autowired
	TransactionServiceInterface transactionServiceInterface;	

	@Autowired
	CompanyServicesInterface companyServicesInterface;
	
	
	@Autowired
	MerchantServiceImpl merchantServiceImpl;
	
	@Autowired
	TransactionDaoImpl transactionDao;
	
	@Autowired
	VoucherDaoImpl voucherDao;
	
	@Autowired
	UserDaoImpl userDao;
	
	@Autowired
	TransactionDetailDaoImpl transactionDetailDao;
	
	@Autowired
	VoucherDaoImpl voucherDaoImpl;
	
	
	
	
	//---------------GE LAST TRANSACTION-------------------
	@Test
	public void getLastTransactionTest() {

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
				purchaseVoucherList(voucherList, dtoResult.getUsrId(), session, false);
				log.info("Purchasing OK");
				
				String profile = "company";
				String startDate = "2000-10-20";
				String endDate = "2020-11-21";
				
				Transaction transaction = transactionDaoImpl.getLastTransaction(idUserInserted);
				
				//rollback		
				log.info("executing rollback...");
				rollBack(dtoResult, idUserInserted, session, voucherList);
				log.info("rollback OK");				
				
				assertTrue(transaction!=null && transaction.getTrcId()!=null);

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
	
	//TEST KO: try to get last transaction with wrong user id
	@Test
	public void getLastTransactionTest_wrongUserID_KO() {

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
				purchaseVoucherList(voucherList, dtoResult.getUsrId(), session, false);
				log.info("Purchasing OK");
				
				String profile = "company";
				String startDate = "2000-10-20";
				String endDate = "2020-11-21";
				
				//try to get last transaction with wrong user id
				Transaction transaction = transactionDaoImpl.getLastTransaction(9999);
				
				//rollback		
				log.info("executing rollback...");
				rollBack(dtoResult, idUserInserted, session, voucherList);
				log.info("rollback OK");				
				
				assertTrue(transaction!=null && transaction.getTrcId()!=null);

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
	
	
	
	//---------------GE TRANSACTION LIST-------------------
	@Test
	public void transactionsListTest() {

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
				purchaseVoucherList(voucherList, dtoResult.getUsrId(), session, false);
				log.info("Purchasing OK");
				
				String profile = "company";
				String startDate = "2000-10-20";
				String endDate = "2020-11-21";
				
				//retrieve the transaction list 
				log.info("Getting transaction list...");
				List<Transaction> transactionList = transactionDao.getTransactionList(dtoResult.getUsrId(), profile, mapToTimestamp(startDate), mapToTimestamp(endDate));
				log.info("transaction list OK");

				
				//rollback		
				log.info("executing rollback...");
				rollBack(dtoResult, idUserInserted, session, voucherList);
				log.info("rollback OK");				
				
				assertTrue(transactionList!=null && transactionList.size()>0);

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
	
	
	//Test KO: try to get list with not existing user ID
	@Test
	public void transactionsListTest_notExistingUserID_KO() {

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
				purchaseVoucherList(voucherList, dtoResult.getUsrId(), session, false);
				log.info("Purchasing OK");

				
				String profile = "company";
				
				String startDate = "2000-10-20";
				String endDate = "2020-11-21";
				
				//setting not existing user id
				String wrongUserID="9999";
				
				//retrieve the transaction list 
				log.info("Getting transaction list...");
				List<Transaction> transactionList = transactionDao.getTransactionList(wrongUserID, profile, mapToTimestamp(startDate), mapToTimestamp(endDate));
				log.info("transaction list OK");


				log.info("executing rollback...");
				rollBack(dtoResult, idUserInserted, session, voucherList);
				log.info("rollback OK");	


				assertTrue(transactionList!=null && transactionList.size()>0);

				
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
	
//	------GET COMPANY ORDER LIST---------------------
	@Test
	public void getCompanyOrdersListTest() {
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
				purchaseVoucherList(voucherList, dtoResult.getUsrId(), session, false);
				log.info("Purchasing OK");

				
				String profile = "company";
				String startDate = "2000-10-20";
				String endDate = "2020-11-21";
				String payed = "not_payed";
				String state = "confirmed";
				
				//retrieve the transaction list 
				log.info("getting company order list...");				
				List<Transaction> transactionList = transactionDao.getCompanyOrderList(dtoResult.getUsrId(), profile, mapToTimestamp(startDate), mapToTimestamp(endDate), managePayed(payed), manageState(state));
				log.info("company order list OK");
				
				
				//rollback		
				log.info("executing rollback...");
				rollBack(dtoResult, idUserInserted, session, voucherList);
				log.info("rollback OK");				
				
				assertTrue(transactionList!=null && transactionList.size()>0);

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
	
	
	//Test KO: try to get list with not existing user ID
	@Test
	public void getCompanyOrdersListTest_notExistingUSerID_KO() {
		
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
				purchaseVoucherList(voucherList, dtoResult.getUsrId(), session, false);
				log.info("Purchasing OK");
				
				String profile = "company";				
				String startDate = "2000-10-20";
				String endDate = "2020-11-21";				
				String payed = "not_payed";
				String state = "confirmed";

				//setting not existing user id
				String wrongUserID="9999";

				//retrieve the transaction list 
				log.info("getting company order list...");
				List<Transaction> transactionList = transactionDao.getCompanyOrderList(wrongUserID, profile, mapToTimestamp(startDate), mapToTimestamp(endDate), managePayed(payed), manageState(state));
				log.info("company order list OK");

				//rollback		
				log.info("executing rollback...");
				rollBack(dtoResult, idUserInserted, session, voucherList);
				log.info("rollback OK");				

				assertTrue(transactionList!=null && transactionList.size()>0);


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
	
	
	
	
//	------GET MERCHANT REDEEMED VOUCHER LIST---------------------
	@Test
	public void getRedeemedListTest() {
		MerchantDTO dto = fillMerchantDTO();	
		MerchantDTO dtoResult = null;
		Integer idUserInserted = null;
		List<VoucherDTO> voucherList = new ArrayList<VoucherDTO>();


		try {
			//insert a merchant
			log.info("Statring signUp...");
			dtoResult = (MerchantDTO) merchantServiceImpl.signUp(dto);
			Session session = sessionFactory.getCurrentSession();
			session.getTransaction().commit();	
			
			if(dtoResult!=null) {
				log.info("signUp OK");
				idUserInserted=Integer.valueOf(dtoResult.getUsrId());
				
				
				Transaction transaction = insertRedeemTransaction(dtoResult.getUsrId(), session);
				
				
				//RECOVER TRANSACTION DETAILS
				String trcID = transaction.getTrcId().toString();
				ArrayList<TransactionDetail> transactionDetailList = (ArrayList<TransactionDetail>) transaction.getTransactionDetailCollection();
				if(transactionDetailList!=null && transactionDetailList.size()>0) {
					
					
					for(TransactionDetail transactionDetail : transactionDetailList) {
						VoucherDTO voucherDTO = new VoucherDTO();
						voucherDTO.setVchName(transactionDetail.getVoucher().getVchName());
						voucherList.add(voucherDTO);
					}
	
					
					Transaction transactionForAssert = new Transaction();
					transactionForAssert = transactionDao.findByPrimaryKey(transactionForAssert, Integer.valueOf(trcID));

				}
				
				String startDate = "2000-10-20";
				String endDate = "2020-11-21";

				
				//retrieve the transaction list 
				log.info("getting redeemed voucher list...");
				List<Transaction> transactionList = transactionDao.getRedeemedOrderList(dtoResult.getUsrId(), null, mapToTimestamp(startDate), mapToTimestamp(endDate), null, null);
				log.info("redeemed voucher list OK");
				
				
				//delete data just inserted in DB		
				log.info("executing rollback...");
				rollBack(dtoResult, idUserInserted, session, voucherList);
				log.info("rollback OK");
				
				
				assertTrue(transactionList!=null && transactionList.size()>0);

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
	
	
	//TEST KO: try to ge redeemed order list with wrong user id
	@Test
	public void getRedeemedListTest_notExistingUserID_KO() {
		MerchantDTO dto = fillMerchantDTO();	
		MerchantDTO dtoResult = null;
		Integer idUserInserted = null;
		List<VoucherDTO> voucherList = new ArrayList<VoucherDTO>();


		try {
			//insert a merchant
			log.info("Statring signUp...");
			dtoResult = (MerchantDTO) merchantServiceImpl.signUp(dto);
			Session session = sessionFactory.getCurrentSession();
			session.getTransaction().commit();	
			
			if(dtoResult!=null) {
				log.info("signUp OK");
				idUserInserted=Integer.valueOf(dtoResult.getUsrId());
				
				
				Transaction transaction = insertRedeemTransaction(dtoResult.getUsrId(), session);
				
				
				//RECOVER TRANSACTION DETAILS
				String trcID = transaction.getTrcId().toString();
				ArrayList<TransactionDetail> transactionDetailList = (ArrayList<TransactionDetail>) transaction.getTransactionDetailCollection();
				if(transactionDetailList!=null && transactionDetailList.size()>0) {
					
					
					for(TransactionDetail transactionDetail : transactionDetailList) {
						VoucherDTO voucherDTO = new VoucherDTO();
						voucherDTO.setVchName(transactionDetail.getVoucher().getVchName());
						voucherList.add(voucherDTO);
					}
	
					
					Transaction transactionForAssert = new Transaction();
					transactionForAssert = transactionDao.findByPrimaryKey(transactionForAssert, Integer.valueOf(trcID));

				}
				
				String startDate = "2000-10-20";
				String endDate = "2020-11-21";
				String wrongUserID = "9999";

				
				//retrieve the transaction list 
				log.info("getting redeemed voucher list...");
				List<Transaction> transactionList = transactionDao.getRedeemedOrderList(wrongUserID, null, mapToTimestamp(startDate), mapToTimestamp(endDate), null, null);
				log.info("redeemed voucher list OK");
				
				
				//delete data just inserted in DB		
				log.info("executing rollback...");
				rollBack(dtoResult, idUserInserted, session, voucherList);
				log.info("rollback OK");
				
				
				assertTrue(transactionList!=null && transactionList.size()>0);

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
	
	
	
//	------GET TRANSACTION DETAIL---------------------
	
	@Test
	public void transactionsDetailTest() {
		
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
				purchaseVoucherList(voucherList, dtoResult.getUsrId(), session, false);
				log.info("Purchasing OK");

				
				Transaction lastTransaction = transactionDao.getLastTransaction(idUserInserted);
				
				//retrieve the transaction  
				log.info("getting transaction detail...");
				TransactionDTO transaction = transactionDao.getTransactionDetail(dtoResult.getUsrId(), lastTransaction.getTrcId().toString());
				log.info("transaction detail OK");

				
				//rollback		
				log.info("executing rollback...");
				rollBack(dtoResult, idUserInserted, session, voucherList);
				log.info("rollback OK");				
				
				assertTrue(transaction!=null && transaction.getTrcId()!=null);

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
	
	//TEST KO: try to get transaction detail with wrong user id
	@Test
	public void transactionsDetailTest_wrongUserID_KO() {
		
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
				purchaseVoucherList(voucherList, dtoResult.getUsrId(), session, false);
				log.info("Purchasing OK");				
			
				String wrongTransactionID= "9999";
				
				//retrieve the transaction  
				log.info("getting transaction detail...");
				TransactionDTO transaction = transactionDao.getTransactionDetail(dtoResult.getUsrId(), wrongTransactionID);
				log.info("transaction detail OK");

				
				//rollback		
				log.info("executing rollback...");
				rollBack(dtoResult, idUserInserted, session, voucherList);
				log.info("rollback OK");				
				
				assertTrue(transaction!=null && transaction.getTrcId()!=null);

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
	
	
	
	//---------------GE TRANSACTION LIST BY PAYED AND TYPE-------------------
	@Test
	public void transactionsListByPayedAndTypeTest() {

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
				purchaseVoucherList(voucherList, dtoResult.getUsrId(), session, false);
				log.info("Purchasing OK");
				
				boolean payed=false;
				
				//retrieve the transaction list 
				log.info("Getting transaction list...");
				List<Transaction> transactionList = transactionDao.getTransactionListByPayedandType(payed, TransactionType.COMPANY_BUY_VOUCHER.getDescription());
				log.info("transaction list OK");

				
				//rollback		
				log.info("executing rollback...");
				rollBack(dtoResult, idUserInserted, session, voucherList);
				log.info("rollback OK");				
				
				assertTrue(transactionList!=null && transactionList.size()>0);

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
	
	//TEST KO: try to get transaction list by wrong transaction type
	@Test
	public void transactionsListByPayedAndTypeTest_wrongTransactionType_KO() {

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
				purchaseVoucherList(voucherList, dtoResult.getUsrId(), session, false);
				log.info("Purchasing OK");
				
				boolean payed=false;
				//setting wrong transaction type
				String wrongTransactionType = "AAA";
				
				//retrieve the transaction list 
				log.info("Getting transaction list...");
				List<Transaction> transactionList = transactionDao.getTransactionListByPayedandType(payed, wrongTransactionType);
				log.info("transaction list OK");

				
				//rollback		
				log.info("executing rollback...");
				rollBack(dtoResult, idUserInserted, session, voucherList);
				log.info("rollback OK");				
				
				assertTrue(transactionList!=null && transactionList.size()>0);

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
	
	
//	------COMPANY ORDER DELETE---------------------
	
	@Test
	public void companyOrderDelTest() {
		
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
				purchaseVoucherList(voucherList, dtoResult.getUsrId(), session, false);
				log.info("Purchasing OK");

				
				Transaction transactionBeforeDelete = transactionDao.getLastTransaction(idUserInserted);
				
				//retrieve the transaction  
				log.info("deleting order...");
		    	org.hibernate.Transaction txn = session.beginTransaction();
		    	transactionDao.companyOrderDel(transactionBeforeDelete.getTrcId().toString());
				txn.commit();
				log.info("order deleted OK");

				Transaction transactionAfterDelete = transactionDao.getLastTransaction(idUserInserted);

				
				//rollback		
				log.info("executing rollback...");
				rollBack(dtoResult, idUserInserted, session, voucherList);
				log.info("rollback OK");				
				
				assertTrue(transactionAfterDelete!=null && transactionAfterDelete.getTrcCancDate()!=null);

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
	
	
	//TEST KO: try to delete order with wrong transaction ID
	@Test
	public void companyOrderDelTest_wrongTrcID_KO() {
		
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
				purchaseVoucherList(voucherList, dtoResult.getUsrId(), session, false);
				log.info("Purchasing OK");

				
				Transaction transactionBeforeDelete = transactionDao.getLastTransaction(idUserInserted);
				
				//set wrong id
				String wrongID = "9999";
				
				//try to delete an order with no existing id
				log.info("deleting order...");
		    	org.hibernate.Transaction txn = session.beginTransaction();
		    	transactionDao.companyOrderDel(wrongID);
				txn.commit();
				log.info("order deleted OK");

				Transaction transactionAfterDelete = transactionDao.getLastTransaction(idUserInserted);
				
				//rollback		
				log.info("executing rollback...");
				rollBack(dtoResult, idUserInserted, session, voucherList);
				log.info("rollback OK");				
				
				assertTrue(transactionAfterDelete!=null && transactionAfterDelete.getTrcCancDate()!=null);

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
	
	
	//---------------GET PENDING TRANSACTION -------------------
	@Test
	public void getPendingTransactionTest() {

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
				
				boolean isPendingTrnasaction=true;
				
				//purchase 2 voucher for the inserted company
				log.info("Purchasing vouchers...");
				List<VoucherDTO> voucherList = fillVoucherList();
				purchaseVoucherList(voucherList, dtoResult.getUsrId(), session, isPendingTrnasaction);
				log.info("Purchasing OK");
				
				
				Transaction lastTransaction = transactionDao.getLastTransaction(idUserInserted);				
				Date lastTransactionDate = lastTransaction.getTrcDate();
				
				Date dateMinusDelay = removeDelayToDate(lastTransactionDate, 10);		

				
				
				//retrieve the transaction list 
				log.info("Getting transaction list...");
				List<Transaction> transactionList = transactionDao.getPendingTransactions(dateMinusDelay, 9);
				log.info("transaction list OK");

				
				//rollback		
				log.info("executing rollback...");
				rollBack(dtoResult, idUserInserted, session, voucherList);
				log.info("rollback OK");				
				
				assertTrue(transactionList!=null && transactionList.size()>0);

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
	
	
	//TEST KO: try to get pending transactions with overdelay date
	@Test
	public void getPendingTransactionTest_overDelay_KO() {

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
				
				boolean isPendingTrnasaction=true;
				
				//purchase 2 voucher for the inserted company
				log.info("Purchasing vouchers...");
				List<VoucherDTO> voucherList = fillVoucherList();
				purchaseVoucherList(voucherList, dtoResult.getUsrId(), session, isPendingTrnasaction);
				log.info("Purchasing OK");
				
				
				Transaction lastTransaction = transactionDao.getLastTransaction(idUserInserted);				
				Date lastTransactionDate = lastTransaction.getTrcDate();
				
				Date dateMinusDelay = removeDelayToDate(lastTransactionDate, 10);						
				
				//retrieve the transaction list 
				log.info("Getting transaction list...");
				//Setting an overdelay (11 seconds) date
				List<Transaction> transactionList = transactionDao.getPendingTransactions(dateMinusDelay, 11);
				log.info("transaction list OK");

				
				//rollback		
				log.info("executing rollback...");
				rollBack(dtoResult, idUserInserted, session, voucherList);
				log.info("rollback OK");				
				
				assertTrue(transactionList!=null && transactionList.size()>0);

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
	
	
	
	
	//--------------UTILITIES----------------------------
	
	private CompanyDTO fillCompanyDTO() {
		CompanyDTO dto = new CompanyDTO();
		dto.setUsrEmail("liga@gmail.com");
		dto.setUsrPassword("Liga.654");
		dto.setCpyPartitaIva("000PARTITAIVA000");
		dto.setCpyCodiceFiscale("000CODICEFISCALE000");
		dto.setCpyPec("s.liga@aktsrl.com");
		return dto;
	}
	
	private MerchantDTO fillMerchantDTO() {
		MerchantDTO dto = new MerchantDTO();
		dto.setUsrEmail("s.liga@aktsrl.com");
		dto.setUsrPassword("Merchant.654");
		dto.setMrcAddress("via del corso 12");
		dto.setMrcBank("ING direct");
		dto.setMrcCity("Roma");
		dto.setMrcCodiceFiscale("123CodFiscale123");
		dto.setMrcFirstNameRef("Ciro");
		dto.setMrcLastNameRef("Rossi");
		dto.setMrcRagioneSociale("Ragione sociale for Test");
		dto.setMrcIban("IbanTest123");
		return dto;
	}
	
	private Date removeDelayToDate(Date date, long delay) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.SECOND, -(int)delay);
		Date dateMinusDelay = calendar.getTime();
		return dateMinusDelay;
	}
	
	
	public Timestamp mapToTimestamp(String dataString) throws ParseException {		

		if(dataString!=null) {
		    SimpleDateFormat dateFormat = Constants.FORMATTER_YYYY_MM_DD;
		    dateFormat.setLenient(false);

		    Date parsedDate = dateFormat.parse(dataString);
		    Timestamp timestamp = new java.sql.Timestamp(parsedDate.getTime());
		    return timestamp;

		}		
		return null;		
	}

	private String manageState(String state) {
		if(state!=null && state.equalsIgnoreCase(Constants.TRANSACTION_STATUS_CONFIRMED_STRING)) {
			return "1";
		}
		else if(state!=null && state.equalsIgnoreCase(Constants.TRANSACTION_STATUS_PENDING_STRING)) {
			return "0";
		}
		return null;
	}


	private String managePayed(String payed) {
		if(payed!=null && payed.equalsIgnoreCase(Constants.TRANSACTION_PAYED_STRING)) {
			return "1";
		}
		else if(payed!=null && payed.equalsIgnoreCase(Constants.TRANSACTION_NOT_PAYED_STRING)) {
			return "0";
		}
		return null;
	}
	
	
	public List<VoucherDTO> purchaseVoucherList(List<VoucherDTO> voucherList, String usrId, Session session, boolean isPendingTransaction) throws ParseException {
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
	    	if(isPendingTransaction) {
		    	transaction.setTrcState(false);
	    	}
	    	else {
	    		transaction.setTrcState(true);	
	    	}	    	
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

	    }
		return resp;
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
	
	
	private Transaction insertRedeemTransaction(String idMerchantInserted, Session session) {
		
    	List<Voucher> voucherList = new ArrayList<Voucher>();
	    try {    	
	    	
			Voucher voucher1 = new Voucher();
			Voucher voucher2 = new Voucher();
			
		    String sDate1="25/10/2015";  
			Date date1=new SimpleDateFormat("dd/MM/yyyy").parse(sDate1);
		    String sDate2="25/10/2018";  
		    Date date2=new SimpleDateFormat("dd/MM/yyyy").parse(sDate2);
		    
	    	//INSERT VOUCHER
			insertVouchers(session, voucherList, voucher1, voucher2, date1, date2);
	    	
	    	
	    	//INSERT ALLOCATION TRANSACTION
	    	//recover the merchant
	    	User merchant = new User();	
	    	merchant = userDao.findByPrimaryKey(merchant, Integer.valueOf(idMerchantInserted));

	    	//insert allocation transaction
	    	Transaction transaction = new Transaction();
	    	transaction.setTrcTxId("testTx");
	    	transaction.setUsrIdDa(merchant);
	    	transaction.setTrcType(TransactionType.REDEEM_VOUCHER.getDescription());
	    	transaction.setTrcDate(date1);
	    	transaction.setTrcState(Constants.TRANSACTION_STATUS_CONFIRMED);
	    	transaction.setTrcIban("IbanForTest1234");
	    	if(merchant.getMerchant()!=null && merchant.getMerchant().getMrcFirstNameRef()!=null && merchant.getMerchant().getMrcLastNameRef()!=null) {
		    	transaction.setTrcAccountHolder(merchant.getMerchant().getMrcFirstNameRef() + " " + merchant.getMerchant().getMrcLastNameRef());
	    	}
	    	transaction.setTrcPayed(Constants.TRANSACTION_PAYED);
	    	transaction.setTrcMailSent(false);
	    	
			org.hibernate.Transaction txn3 = session.beginTransaction();
			transactionDao.insert(transaction);
	    	txn3.commit();
	    	
	    	
	    	//INSERT TRANSACTION DETAIL
	    	insertTransactionDetail(session, voucher1, voucher2, transaction);
	    	
	    	//RECOVER THE TRANSACTION
	    	Transaction insertedTransaction = transactionDao.getLastTransaction(Integer.valueOf(idMerchantInserted));
	    	
	    	if(insertedTransaction!=null && (insertedTransaction.getTransactionDetailCollection()==null || insertedTransaction.getTransactionDetailCollection().size()==0)) {
				List<TransactionDetail> transactionDetailList = new ArrayList<TransactionDetail>();
				transactionDetailList = transactionDetailDao.findByTrcId(transaction.getTrcId().toString());
				insertedTransaction.setTransactionDetailCollection(transactionDetailList);
	    	}
	    	
			return insertedTransaction;  		

			
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	private void insertVouchers(Session session, List<Voucher> voucherList, Voucher voucher1, Voucher voucher2, Date date1,
			Date date2) {
		voucher1.setVchName("voucherTest1");
		voucher2.setVchName("voucherTest2");

		voucher1.setVchCreationDate(date1);
		voucher2.setVchCreationDate(date1);

		voucher1.setVchEndDate(date2);
		voucher2.setVchEndDate(date2);

		voucher1.setVchState(true);
		voucher2.setVchState(true);

		voucher1.setVchValue(new BigDecimal(100));
		voucher2.setVchValue(new BigDecimal(150));


		voucherList.add(voucher1);
		voucherList.add(voucher2);

		org.hibernate.Transaction txn1 = session.beginTransaction();
		voucherDaoImpl.insert(voucher1);
		txn1.commit();

		org.hibernate.Transaction txn2 = session.beginTransaction();
		voucherDaoImpl.insert(voucher2);
		txn2.commit();
	}
	

	private void insertTransactionDetail(Session session, Voucher voucher1, Voucher voucher2, Transaction transaction) {
		TransactionDetail transactionDetail1 = new TransactionDetail();
		TransactionDetail transactionDetail2 = new TransactionDetail();
		
		TransactionDetailPK transactionDetailPK1 = new TransactionDetailPK();
		TransactionDetailPK transactionDetailPK2 = new TransactionDetailPK();

		
		transactionDetailPK1.setTrcId(transaction.getTrcId());
		transactionDetailPK1.setVchName(voucher1.getVchName());
		transactionDetail1.setTransactionDetailPK(transactionDetailPK1);
		transactionDetail1.setTransaction(transaction);
		transactionDetail1.setTrdQuantity(Long.parseLong("10"));
		transactionDetail1.setVoucher(voucher1);
		
		transactionDetailPK2.setTrcId(transaction.getTrcId());
		transactionDetailPK2.setVchName(voucher2.getVchName());
		transactionDetail2.setTransactionDetailPK(transactionDetailPK2);
		transactionDetail2.setTransaction(transaction);
		transactionDetail2.setTrdQuantity(Long.parseLong("10"));
		transactionDetail2.setVoucher(voucher2);

		
		org.hibernate.Transaction txn4 = session.beginTransaction();
		transactionDetailDao.insert(transactionDetail1);
		txn4.commit();
		
		org.hibernate.Transaction txn5 = session.beginTransaction();
		transactionDetailDao.insert(transactionDetail2);
		txn5.commit();
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
