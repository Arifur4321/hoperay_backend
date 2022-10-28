package test.com.mediatica.vouchain.servicesImpl;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Formatter;
import java.util.List;
import java.util.Locale;

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
import com.mediatica.vouchain.dao.CompanyDaoImpl;
import com.mediatica.vouchain.dao.TransactionDaoImpl;
import com.mediatica.vouchain.dao.TransactionDetailDaoImpl;
import com.mediatica.vouchain.dao.UserDaoImpl;
import com.mediatica.vouchain.dao.VoucherDaoImpl;
import com.mediatica.vouchain.dto.CompanyDTO;
import com.mediatica.vouchain.dto.EmployeeDTO;
import com.mediatica.vouchain.dto.MerchantDTO;
import com.mediatica.vouchain.entities.Transaction;
import com.mediatica.vouchain.entities.TransactionDetail;
import com.mediatica.vouchain.entities.TransactionDetailPK;
import com.mediatica.vouchain.entities.User;
import com.mediatica.vouchain.entities.Voucher;
import com.mediatica.vouchain.exceptions.EmailYetInTheSystemException;
import com.mediatica.vouchain.servicesImpl.CompanyServiceImpl;
import com.mediatica.vouchain.servicesImpl.MerchantServiceImpl;
import com.mediatica.vouchain.servicesImpl.VoucherServiceImpl;
import com.mediatica.vouchain.servicesInterface.CompanyServicesInterface;
import com.mediatica.vouchain.servicesInterface.EmailServiceInterface;
import com.mediatica.vouchain.servicesInterface.EmployeeServicesInterface;

import junit.framework.Assert;

@Transactional
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("file:WebContent/WEB-INF/vouchain-servlet.xml")
@WebAppConfiguration
public class EmailServiceTest {
	

	@Autowired
	SessionFactory sessionFactory;	

	@Autowired
	VoucherServiceImpl voucherServiceImpl;
	
	@Autowired
	VoucherDaoImpl voucherDaoImpl;
	
	@Autowired
	TransactionDaoImpl transactionDao;
	
	@Autowired
	TransactionDetailDaoImpl transactionDetailDao;
	
	@Autowired
	CompanyDaoImpl companyDao;
	
	@Autowired
	UserDaoImpl userDao;
		
	@Autowired
	CompanyServicesInterface companyServicesInterface;
	
	@Autowired
	EmployeeServicesInterface employeeServicesInterface;
	
	@Autowired
	EmailServiceInterface emailServiceInterface;
	
	@Autowired
	MerchantServiceImpl merchantServiceImpl;
	
	private static org.slf4j.Logger log = LoggerFactory.getLogger(CompanyServiceImpl.class);
	
	


	//------------EXPEND VOUCHER CONFIRM EMAIL----------------------------------------
	
	@Test
	public void voucherExpendConfeMailTest() {
		
		
		CompanyDTO companyDTO = fillCompanyDTO();
		EmployeeDTO employeeDTO = fillEmployeeDTO();
		MerchantDTO merchantDTO = fillMerchantDTO();

		CompanyDTO companyDTOResult = null;
		EmployeeDTO employeeDTOResult = null;
		MerchantDTO merchantDTOResult = null;
		
		String idEmployeeInserted = null;
		String idCompanyInserted = null;
		String idMerchantInserted = null;

		try {
			
			Session session = sessionFactory.getCurrentSession();

			//INSERT COMPANY
			log.info("Statring company signUp...");			
			companyDTOResult = (CompanyDTO) companyServicesInterface.signUp(companyDTO);
			session.getTransaction().commit();			
			if(companyDTOResult!=null) {
				
				log.info("signUp OK");
				idCompanyInserted=companyDTOResult.getUsrId();
				employeeDTO.setCpyUsrId(companyDTOResult.getUsrId());
				
				//INSERT EMPLOYEE
				log.info("Starting employee signUp...");
				org.hibernate.Transaction txn1 = session.beginTransaction();
				employeeDTOResult = (EmployeeDTO) employeeServicesInterface.signUp(employeeDTO);
				txn1.commit();				
				if(employeeDTOResult!=null) {
					log.info("signUp OK");
					idEmployeeInserted=employeeDTOResult.getUsrId();
				}
				
				//INSERT MERCHANT
				log.info("Starting merchant signUp...");
				org.hibernate.Transaction txn2 = session.beginTransaction();
				merchantDTOResult = (MerchantDTO) merchantServiceImpl.signUp(merchantDTO);
				txn2.commit();				
				if(merchantDTOResult!=null) {
					log.info("signUp OK");
					idMerchantInserted=merchantDTOResult.getUsrId();
				}

				//INSERT TRANSACTION
				Transaction transaction = insertExpendTransaction(idEmployeeInserted, idMerchantInserted, session);
				
				//RECOVER TRANSACTION DETAILS
				String trcID = transaction.getTrcId().toString();
				ArrayList<TransactionDetail> transactionDetailList = (ArrayList<TransactionDetail>) transaction.getTransactionDetailCollection();
				if(transactionDetailList!=null && transactionDetailList.size()>0) {
					String vchName1 = transactionDetailList.get(0).getVoucher().getVchName();
					String vchName2 = transactionDetailList.get(1).getVoucher().getVchName();	
					
					org.hibernate.Transaction txn3 = session.beginTransaction();
					emailServiceInterface.voucherConfeMail(trcID);
					txn3.commit();							
					
					//delete data just inserted in DB		
					log.info("executing rollback...");
					rollBackFromVoucherConfeMai(vchName1, vchName2, idCompanyInserted, idEmployeeInserted, idMerchantInserted, trcID, session);
					log.info("rollback OK");
					
					Transaction transactionForAssert = new Transaction();
					transactionForAssert = transactionDao.findByPrimaryKey(transactionForAssert, Integer.valueOf(trcID));
					
					assertTrue(transactionForAssert.getTrcMailSent());
				}
				else {
					//delete data just inserted in DB		
					log.info("executing rollback...");
					rollBackFromVoucherConfeMai("", "", idCompanyInserted, idEmployeeInserted, idMerchantInserted, trcID, session);
					log.info("rollback OK");
				}
			

			}			
		}	
		catch (EmailYetInTheSystemException e) {
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
	
	//TEST KO: try to send mail for not existing transaction
	@Test
	public void voucherExpendConfeMailTest_notExistingTransactionID_KO() {
		
		
		CompanyDTO companyDTO = fillCompanyDTO();
		EmployeeDTO employeeDTO = fillEmployeeDTO();
		MerchantDTO merchantDTO = fillMerchantDTO();

		CompanyDTO companyDTOResult = null;
		EmployeeDTO employeeDTOResult = null;
		MerchantDTO merchantDTOResult = null;
		
		String idEmployeeInserted = null;
		String idCompanyInserted = null;
		String idMerchantInserted = null;

		try {
			
			Session session = sessionFactory.getCurrentSession();

			//INSERT COMPANY
			log.info("Statring company signUp...");			
			companyDTOResult = (CompanyDTO) companyServicesInterface.signUp(companyDTO);
			session.getTransaction().commit();			
			if(companyDTOResult!=null) {
				
				log.info("signUp OK");
				idCompanyInserted=companyDTOResult.getUsrId();
				employeeDTO.setCpyUsrId(companyDTOResult.getUsrId());
				
				//INSERT EMPLOYEE
				log.info("Starting employee signUp...");
				org.hibernate.Transaction txn1 = session.beginTransaction();
				employeeDTOResult = (EmployeeDTO) employeeServicesInterface.signUp(employeeDTO);
				txn1.commit();				
				if(employeeDTOResult!=null) {
					log.info("signUp OK");
					idEmployeeInserted=employeeDTOResult.getUsrId();
				}
				
				//INSERT MERCHANT
				log.info("Starting merchant signUp...");
				org.hibernate.Transaction txn2 = session.beginTransaction();
				merchantDTOResult = (MerchantDTO) merchantServiceImpl.signUp(merchantDTO);
				txn2.commit();				
				if(merchantDTOResult!=null) {
					log.info("signUp OK");
					idMerchantInserted=merchantDTOResult.getUsrId();
				}

				//INSERT TRANSACTION
				Transaction transaction = insertExpendTransaction(idEmployeeInserted, idMerchantInserted, session);
				
				//RECOVER TRANSACTION DETAILS
				String trcID = transaction.getTrcId().toString();
				ArrayList<TransactionDetail> transactionDetailList = (ArrayList<TransactionDetail>) transaction.getTransactionDetailCollection();
				if(transactionDetailList!=null && transactionDetailList.size()>0) {
					String vchName1 = transactionDetailList.get(0).getVoucher().getVchName();
					String vchName2 = transactionDetailList.get(1).getVoucher().getVchName();	
					
					
					
					//set not existing ID
					trcID = "9999";
					
					org.hibernate.Transaction txn3 = session.beginTransaction();
					try {
						emailServiceInterface.voucherConfeMail(trcID);
						txn3.commit();							

					} catch (Exception e) {
						txn3.commit();	
						//delete data just inserted in DB		
						log.info("executing rollback...");
						rollBackFromVoucherConfeMai(vchName1, vchName2, idCompanyInserted, idEmployeeInserted, idMerchantInserted, trcID, session);
						log.info("rollback OK");
						fail();
						e.printStackTrace();
					}
					
					//delete data just inserted in DB		
					log.info("executing rollback...");
					rollBackFromVoucherConfeMai(vchName1, vchName2, idCompanyInserted, idEmployeeInserted, idMerchantInserted, trcID, session);
					log.info("rollback OK");
					
					Transaction transactionForAssert = new Transaction();
					transactionForAssert = transactionDao.findByPrimaryKey(transactionForAssert, Integer.valueOf(trcID));
					
					assertTrue(transactionForAssert.getTrcMailSent());
				}
				else {
					//delete data just inserted in DB		
					log.info("executing rollback...");
					rollBackFromVoucherConfeMai("", "", idCompanyInserted, idEmployeeInserted, idMerchantInserted, trcID, session);
					log.info("rollback OK");
				}
			

			}			
		}	
		catch (EmailYetInTheSystemException e) {
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
	
	//------------REDEEM CONFIRM EMAIL----------------------------------------
	
	@Test
	public void voucherRedeemConfeMailTest() {

		MerchantDTO merchantDTO = fillMerchantDTO();

		MerchantDTO merchantDTOResult = null;

		String idMerchantInserted = null;

		try {
			
			Session session = sessionFactory.getCurrentSession();

			//INSERT COMPANY
			log.info("Statring merchant signUp...");			
			merchantDTOResult = (MerchantDTO) merchantServiceImpl.signUp(merchantDTO);
			session.getTransaction().commit();			
			if(merchantDTOResult!=null) {
				
				idMerchantInserted = merchantDTOResult.getUsrId().toString();
				//INSERT TRANSACTION
				Transaction transaction = insertRedeemTransaction(idMerchantInserted, session);
				
				//RECOVER TRANSACTION DETAILS
				String trcID = transaction.getTrcId().toString();
				ArrayList<TransactionDetail> transactionDetailList = (ArrayList<TransactionDetail>) transaction.getTransactionDetailCollection();
				if(transactionDetailList!=null && transactionDetailList.size()>0) {
					String vchName1 = transactionDetailList.get(0).getVoucher().getVchName();
					String vchName2 = transactionDetailList.get(1).getVoucher().getVchName();	
					
					org.hibernate.Transaction txn3 = session.beginTransaction();
					emailServiceInterface.voucherConfeMail(trcID);
					txn3.commit();							
					
					Transaction transactionForAssert = new Transaction();
					transactionForAssert = transactionDao.findByPrimaryKey(transactionForAssert, Integer.valueOf(trcID));
					
					//delete data just inserted in DB		
					log.info("executing rollback...");
					rollBackFromVoucherConfeMai(vchName1, vchName2, "9999", "9999", idMerchantInserted, trcID, session);
					log.info("rollback OK");

					
					assertTrue(transactionForAssert.getTrcMailSent());
				}
				else {
					//delete data just inserted in DB		
					log.info("executing rollback...");
					rollBackFromVoucherConfeMai("", "", "9999", "9999", idMerchantInserted, trcID, session);
					log.info("rollback OK");
				}
			

			}			
		}	
		catch (EmailYetInTheSystemException e) {
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
	
	
	//TEST KO: try to send mail for not existing transaction
	@Test
	public void voucherRedeemConfeMailTest_notExistingTransactionID_KO() {

		MerchantDTO merchantDTO = fillMerchantDTO();

		MerchantDTO merchantDTOResult = null;

		String idMerchantInserted = null;

		try {
			
			Session session = sessionFactory.getCurrentSession();

			//INSERT COMPANY
			log.info("Statring merchant signUp...");			
			merchantDTOResult = (MerchantDTO) merchantServiceImpl.signUp(merchantDTO);
			session.getTransaction().commit();			
			if(merchantDTOResult!=null) {
				
				idMerchantInserted = merchantDTOResult.getUsrId().toString();
				//INSERT TRANSACTION
				Transaction transaction = insertRedeemTransaction(idMerchantInserted, session);
				
				//RECOVER TRANSACTION DETAILS
				String trcID = transaction.getTrcId().toString();
				ArrayList<TransactionDetail> transactionDetailList = (ArrayList<TransactionDetail>) transaction.getTransactionDetailCollection();
				if(transactionDetailList!=null && transactionDetailList.size()>0) {
					String vchName1 = transactionDetailList.get(0).getVoucher().getVchName();
					String vchName2 = transactionDetailList.get(1).getVoucher().getVchName();	
					
					
					//set not existing ID
					trcID = "9999";
					
					org.hibernate.Transaction txn3 = session.beginTransaction();
					try {
						emailServiceInterface.voucherConfeMail(trcID);
						txn3.commit();							

					} catch (Exception e) {
						txn3.commit();		
						//delete data just inserted in DB		
						log.info("executing rollback...");
						rollBackFromVoucherConfeMai(vchName1, vchName2, "9999", "9999", idMerchantInserted, trcID, session);
						log.info("rollback OK");
						fail();
						e.printStackTrace();
					}
					
					//delete data just inserted in DB		
					log.info("executing rollback...");
					rollBackFromVoucherConfeMai(vchName1, vchName2, "9999", "9999", idMerchantInserted, trcID, session);
					log.info("rollback OK");
					
					Transaction transactionForAssert = new Transaction();
					transactionForAssert = transactionDao.findByPrimaryKey(transactionForAssert, Integer.valueOf(trcID));
					
					assertTrue(transactionForAssert.getTrcMailSent());
				}
				else {
					//delete data just inserted in DB		
					log.info("executing rollback...");
					rollBackFromVoucherConfeMai("", "", "9999", "9999", idMerchantInserted, trcID, session);
					log.info("rollback OK");
				}
			

			}			
		}	
		catch (EmailYetInTheSystemException e) {
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


	
//------------PAYMENT REDEEM CONFIRM MAIL----------------------------------------

	
	@Test
	public void paymentRedeemConfeMailTest() {

		MerchantDTO merchantDTO = fillMerchantDTO();

		MerchantDTO merchantDTOResult = null;

		String idMerchantInserted = null;

		try {
			
			Session session = sessionFactory.getCurrentSession();

			//INSERT MERCHANT
			log.info("Statring merchant signUp...");			
			merchantDTOResult = (MerchantDTO) merchantServiceImpl.signUp(merchantDTO);
			session.getTransaction().commit();			
			if(merchantDTOResult!=null) {
				
				idMerchantInserted = merchantDTOResult.getUsrId().toString();
				//INSERT TRANSACTION
				Transaction transaction = insertRedeemTransaction(idMerchantInserted, session);
				
				//RECOVER TRANSACTION DETAILS
				String trcID = transaction.getTrcId().toString();
				ArrayList<TransactionDetail> transactionDetailList = (ArrayList<TransactionDetail>) transaction.getTransactionDetailCollection();
				if(transactionDetailList!=null && transactionDetailList.size()>0) {
					String vchName1 = transactionDetailList.get(0).getVoucher().getVchName();
					String vchName2 = transactionDetailList.get(1).getVoucher().getVchName();	
					
					org.hibernate.Transaction txn3 = session.beginTransaction();
					emailServiceInterface.paymentRedeemConfeMail(trcID);
					txn3.commit();							
					
					Transaction transactionForAssert = new Transaction();
					transactionForAssert = transactionDao.findByPrimaryKey(transactionForAssert, Integer.valueOf(trcID));
					
					//delete data just inserted in DB		
					log.info("executing rollback...");
					rollBackFromVoucherConfeMai(vchName1, vchName2, "9999", "9999", idMerchantInserted, trcID, session);
					log.info("rollback OK");

					
					assertTrue(transactionForAssert.getTrcMailSent());
				}
				else {
					//delete data just inserted in DB		
					log.info("executing rollback...");
					rollBackFromVoucherConfeMai("", "", "9999", "9999", idMerchantInserted, trcID, session);
					log.info("rollback OK");
				}
			

			}			
		}	
		catch (EmailYetInTheSystemException e) {
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
	
	//TEST KO: try to send mail for not existing transaction
	@Test
	public void paymentRedeemConfeMailTest_notExistingTransactionID_KO() {

		MerchantDTO merchantDTO = fillMerchantDTO();

		MerchantDTO merchantDTOResult = null;

		String idMerchantInserted = null;

		try {
			
			Session session = sessionFactory.getCurrentSession();

			//INSERT MERCHANT
			log.info("Statring merchant signUp...");			
			merchantDTOResult = (MerchantDTO) merchantServiceImpl.signUp(merchantDTO);
			session.getTransaction().commit();			
			if(merchantDTOResult!=null) {
				
				idMerchantInserted = merchantDTOResult.getUsrId().toString();
				//INSERT TRANSACTION
				Transaction transaction = insertRedeemTransaction(idMerchantInserted, session);
				
				//RECOVER TRANSACTION DETAILS
				String trcID = transaction.getTrcId().toString();
				ArrayList<TransactionDetail> transactionDetailList = (ArrayList<TransactionDetail>) transaction.getTransactionDetailCollection();
				if(transactionDetailList!=null && transactionDetailList.size()>0) {
					String vchName1 = transactionDetailList.get(0).getVoucher().getVchName();
					String vchName2 = transactionDetailList.get(1).getVoucher().getVchName();	
					
					
					
					//set not existing ID
					trcID = "9999";
					
					org.hibernate.Transaction txn3 = session.beginTransaction();
					try {
						emailServiceInterface.paymentRedeemConfeMail(trcID);
						txn3.commit();							

					} catch (Exception e) {
						txn3.commit();	
						//delete data just inserted in DB		
						log.info("executing rollback...");
						rollBackFromVoucherConfeMai(vchName1, vchName2, "9999", "9999", idMerchantInserted, trcID, session);
						log.info("rollback OK");
						fail();

						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
					Transaction transactionForAssert = new Transaction();
					transactionForAssert = transactionDao.findByPrimaryKey(transactionForAssert, Integer.valueOf(trcID));
					
					//delete data just inserted in DB		
					log.info("executing rollback...");
					rollBackFromVoucherConfeMai(vchName1, vchName2, "9999", "9999", idMerchantInserted, trcID, session);
					log.info("rollback OK");

					
					assertTrue(transactionForAssert.getTrcMailSent());
				}
				else {
					//delete data just inserted in DB		
					log.info("executing rollback...");
					rollBackFromVoucherConfeMai("", "", "9999", "9999", idMerchantInserted, trcID, session);
					log.info("rollback OK");
				}
			

			}			
		}	
		catch (EmailYetInTheSystemException e) {
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

	//------------ALLOCATION CONFIRM EMAIL----------------------------------------

	@Test
	public void atter() {
	//	String valueInString=String.format(Locale.US,"%.2f", 1234567.89);
	//	System.out.println("********************"+valueInString);
		DecimalFormat formatter = (DecimalFormat) NumberFormat.getInstance(Locale.ITALIAN);
		DecimalFormatSymbols symbols = formatter.getDecimalFormatSymbols();

		symbols.setGroupingSeparator('.');
		formatter.setDecimalFormatSymbols(symbols);
		formatter.setMaximumFractionDigits(2);
		formatter.setMinimumFractionDigits(2);

		System.out.println("***********"+formatter.format(1234));
		System.out.println("***********"+formatter.format(12341234.567));
		System.out.println("***********"+formatter.format(123.567));
		
		
		assertTrue(true);
	}
	
	
	@Test
	public void voucherAllocationConfeMailTest() {
		
		CompanyDTO companyDTO = fillCompanyDTO();
		EmployeeDTO employeeDTO = fillEmployeeDTO();

		CompanyDTO companyDTOResult = null;
		EmployeeDTO employeeDTOResult = null;
		
		String idEmployeeInserted = null;
		String idCompanyInserted = null;


		try {
			
			Session session = sessionFactory.getCurrentSession();

			//INSERT COMPANY
			log.info("Statring company signUp...");			
			companyDTOResult = (CompanyDTO) companyServicesInterface.signUp(companyDTO);
			session.getTransaction().commit();			
			if(companyDTOResult!=null) {
				
				log.info("signUp OK");
				idCompanyInserted=companyDTOResult.getUsrId();
				employeeDTO.setCpyUsrId(companyDTOResult.getUsrId());
				
				//INSERT EMPLOYEE
				log.info("Starting employee signUp...");
				org.hibernate.Transaction txn1 = session.beginTransaction();
				employeeDTOResult = (EmployeeDTO) employeeServicesInterface.signUp(employeeDTO);
				txn1.commit();				
				if(employeeDTOResult!=null) {
					log.info("signUp OK");
					idEmployeeInserted=employeeDTOResult.getUsrId();
				}

				//INSERT TRANSACTION
				Transaction transaction = insertAllocationTransaction(idCompanyInserted, idEmployeeInserted, session);
				
				//RECOVER TRANSACTION DETAILS
				String trcID = transaction.getTrcId().toString();
				ArrayList<TransactionDetail> transactionDetailList = (ArrayList<TransactionDetail>) transaction.getTransactionDetailCollection();
				if(transactionDetailList!=null && transactionDetailList.size()>0) {
					String vchName1 = transactionDetailList.get(0).getVoucher().getVchName();
					String vchName2 = transactionDetailList.get(1).getVoucher().getVchName();	
					
					org.hibernate.Transaction txn2 = session.beginTransaction();
					emailServiceInterface.voucherConfeMail(trcID);
					txn2.commit();							
					
					//delete data just inserted in DB		
					log.info("executing rollback...");
					rollBackFromVoucherConfeMai(vchName1, vchName2, idCompanyInserted, idEmployeeInserted, "9999", trcID, session);
					log.info("rollback OK");
					
					Transaction transactionForAssert = new Transaction();
					transactionForAssert = transactionDao.findByPrimaryKey(transactionForAssert, Integer.valueOf(trcID));
					
					assertTrue(transactionForAssert.getTrcMailSent());
				}
				else {
					//delete data just inserted in DB		
					log.info("executing rollback...");
					rollBackFromVoucherConfeMai("", "", idCompanyInserted, idEmployeeInserted, "9999", trcID, session);
					log.info("rollback OK");
				}
			

			}			
		}	
		catch (EmailYetInTheSystemException e) {
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
	
	//TEST KO: try to send mail for not existing transaction
	@Test
	public void voucherAllocationConfeMailTest_notExistingTransactionID_KO() {
		
		CompanyDTO companyDTO = fillCompanyDTO();
		EmployeeDTO employeeDTO = fillEmployeeDTO();

		CompanyDTO companyDTOResult = null;
		EmployeeDTO employeeDTOResult = null;
		
		String idEmployeeInserted = null;
		String idCompanyInserted = null;


		try {
			
			Session session = sessionFactory.getCurrentSession();

			//INSERT COMPANY
			log.info("Statring company signUp...");			
			companyDTOResult = (CompanyDTO) companyServicesInterface.signUp(companyDTO);
			session.getTransaction().commit();			
			if(companyDTOResult!=null) {
				
				log.info("signUp OK");
				idCompanyInserted=companyDTOResult.getUsrId();
				employeeDTO.setCpyUsrId(companyDTOResult.getUsrId());
				
				//INSERT EMPLOYEE
				log.info("Starting employee signUp...");
				org.hibernate.Transaction txn1 = session.beginTransaction();
				employeeDTOResult = (EmployeeDTO) employeeServicesInterface.signUp(employeeDTO);
				txn1.commit();				
				if(employeeDTOResult!=null) {
					log.info("signUp OK");
					idEmployeeInserted=employeeDTOResult.getUsrId();
				}

				//INSERT TRANSACTION
				Transaction transaction = insertAllocationTransaction(idCompanyInserted, idEmployeeInserted, session);
				
				//RECOVER TRANSACTION DETAILS
				String trcID = transaction.getTrcId().toString();
				ArrayList<TransactionDetail> transactionDetailList = (ArrayList<TransactionDetail>) transaction.getTransactionDetailCollection();
				if(transactionDetailList!=null && transactionDetailList.size()>0) {
					String vchName1 = transactionDetailList.get(0).getVoucher().getVchName();
					String vchName2 = transactionDetailList.get(1).getVoucher().getVchName();	
					
					
					//set not existing ID
					trcID = "9999";
					
					org.hibernate.Transaction txn2 = session.beginTransaction();
					try {
						emailServiceInterface.voucherConfeMail(trcID);
						txn2.commit();
						
					} catch (Exception e) {						
						txn2.commit();
						//delete data just inserted in DB		
						log.info("executing rollback...");
						rollBackFromVoucherConfeMai(vchName1, vchName2, idCompanyInserted, idEmployeeInserted, "9999", trcID, session);
						log.info("rollback OK");
						e.printStackTrace();
						fail();
					}							
					
					//delete data just inserted in DB		
					log.info("executing rollback...");
					rollBackFromVoucherConfeMai(vchName1, vchName2, idCompanyInserted, idEmployeeInserted, "9999", trcID, session);
					log.info("rollback OK");
					
					Transaction transactionForAssert = new Transaction();
					transactionForAssert = transactionDao.findByPrimaryKey(transactionForAssert, Integer.valueOf(trcID));
					
					assertTrue(transactionForAssert.getTrcMailSent());
				}
				else {
					//delete data just inserted in DB		
					log.info("executing rollback...");
					rollBackFromVoucherConfeMai("", "", idCompanyInserted, idEmployeeInserted, "9999", trcID, session);
					log.info("rollback OK");
				}
			

			}			
		}	
		catch (EmailYetInTheSystemException e) {
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
	
	

	//------------PAYMENT ORDER MAIL----------------------------------------

	
	@Test
	public void paymentOrderConfeMailTest() {
		
		CompanyDTO companyDTO = fillCompanyDTO();

		CompanyDTO companyDTOResult = null;
		
		String idCompanyInserted = null;


		try {
			
			Session session = sessionFactory.getCurrentSession();

			//INSERT COMPANY
			log.info("Statring company signUp...");			
			companyDTOResult = (CompanyDTO) companyServicesInterface.signUp(companyDTO);
			session.getTransaction().commit();			
			if(companyDTOResult!=null) {
				
				log.info("signUp OK");
				idCompanyInserted=companyDTOResult.getUsrId();

				//INSERT TRANSACTION
				Transaction transaction = insertPurchaseTransaction(idCompanyInserted, session);
				
				//RECOVER TRANSACTION DETAILS
				String trcID = transaction.getTrcId().toString();
				ArrayList<TransactionDetail> transactionDetailList = (ArrayList<TransactionDetail>) transaction.getTransactionDetailCollection();
				if(transactionDetailList!=null && transactionDetailList.size()>0) {
					String vchName1 = transactionDetailList.get(0).getVoucher().getVchName();
					String vchName2 = transactionDetailList.get(1).getVoucher().getVchName();	
					
					org.hibernate.Transaction txn2 = session.beginTransaction();
					emailServiceInterface.paymentOrderConfeMail(trcID);
					txn2.commit();							
					
					//delete data just inserted in DB		
					log.info("executing rollback...");
					rollBackFromVoucherConfeMai(vchName1, vchName2, idCompanyInserted, "9999", "9999", trcID, session);
					log.info("rollback OK");
					
					Transaction transactionForAssert = new Transaction();
					transactionForAssert = transactionDao.findByPrimaryKey(transactionForAssert, Integer.valueOf(trcID));
					
					assertTrue(transactionForAssert.getTrcMailSent());
				}
				else {
					//delete data just inserted in DB		
					log.info("executing rollback...");
					rollBackFromVoucherConfeMai("", "", idCompanyInserted, "9999", "9999", trcID, session);
					log.info("rollback OK");
				}
			

			}			
		}	
		catch (EmailYetInTheSystemException e) {
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
	
	//TEST KO: try to send mail for not existing transaction
	@Test
	public void paymentOrderConfeMailTest_notExistingTransactionID_KO() {
		
		CompanyDTO companyDTO = fillCompanyDTO();

		CompanyDTO companyDTOResult = null;
		
		String idCompanyInserted = null;


		try {
			
			Session session = sessionFactory.getCurrentSession();

			//INSERT COMPANY
			log.info("Statring company signUp...");			
			companyDTOResult = (CompanyDTO) companyServicesInterface.signUp(companyDTO);
			session.getTransaction().commit();			
			if(companyDTOResult!=null) {
				
				log.info("signUp OK");
				idCompanyInserted=companyDTOResult.getUsrId();

				//INSERT TRANSACTION
				Transaction transaction = insertPurchaseTransaction(idCompanyInserted, session);
				
				//RECOVER TRANSACTION DETAILS
				String trcID = transaction.getTrcId().toString();
				ArrayList<TransactionDetail> transactionDetailList = (ArrayList<TransactionDetail>) transaction.getTransactionDetailCollection();
				if(transactionDetailList!=null && transactionDetailList.size()>0) {
					String vchName1 = transactionDetailList.get(0).getVoucher().getVchName();
					String vchName2 = transactionDetailList.get(1).getVoucher().getVchName();					
					
					
					//set not existing ID
					trcID = "9999";
					
					org.hibernate.Transaction txn2 = session.beginTransaction();
					try {
						emailServiceInterface.paymentOrderConfeMail(trcID);
						txn2.commit();
					} catch (Exception e) {
						txn2.commit();						
						//delete data just inserted in DB		
						log.info("executing rollback...");
						rollBackFromVoucherConfeMai(vchName1, vchName2, idCompanyInserted, "9999", "9999", trcID, session);
						log.info("rollback OK");
						fail();
						e.printStackTrace();
					}
												
					
					//delete data just inserted in DB		
					log.info("executing rollback...");
					rollBackFromVoucherConfeMai(vchName1, vchName2, idCompanyInserted, "9999", "9999", trcID, session);
					log.info("rollback OK");
					
					Transaction transactionForAssert = new Transaction();
					transactionForAssert = transactionDao.findByPrimaryKey(transactionForAssert, Integer.valueOf(trcID));
					
					assertTrue(transactionForAssert.getTrcMailSent());
				}
				else {
					//delete data just inserted in DB		
					log.info("executing rollback...");
					rollBackFromVoucherConfeMai("", "", idCompanyInserted, "9999", "9999", trcID, session);
					log.info("rollback OK");
				}
			

			}			
		}	
		catch (EmailYetInTheSystemException e) {
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




//------------VOUCHER ORDER MAIL LIST----------------------------------------


	@Test
	public void voucherOrdereMailTest() {	

		CompanyDTO dto = fillCompanyDTO();	
		CompanyDTO dtoResult = null;
		Integer idUserInserted = null;

		try {

			log.info("Statring signUp...");
			dtoResult = (CompanyDTO) companyServicesInterface.signUp(dto);
			Session session = sessionFactory.getCurrentSession();
			session.getTransaction().commit();	
			
			idUserInserted=Integer.valueOf(dtoResult.getUsrId());
			
			List<Voucher> voucherList = insertVoucherPurchaseTransaction(dtoResult.getUsrId(), session);
			
			Transaction transaction = transactionDao.getLastTransaction(Integer.valueOf(dtoResult.getUsrId()));

			org.hibernate.Transaction txn1 = session.beginTransaction();
			emailServiceInterface.voucherOrdereMail(transaction.getTrcId().toString());			
	    	txn1.commit();
			
			Transaction transactionWithMailSentTrue = transactionDao.getLastTransaction(Integer.valueOf(dtoResult.getUsrId()));

	    	
			log.info("deleting transaction detail and vouchers...");
			for(Voucher voucher : voucherList) {
				deleteVoucherAndTransaction(voucher.getVchName(), session, Integer.valueOf(dtoResult.getUsrId()), idUserInserted);
			}
			
			for(Voucher voucher : voucherList) {	
				//delete data just inserted in DB		
				log.info("executing rollback...");
				rollBack(voucher.getVchName(), session, Integer.valueOf(dtoResult.getUsrId()), idUserInserted);
				log.info("rollback OK");
			
			}			
			log.info("transaction detail and vouchers deleted OK");
			
			log.info("assert mail send is true...");
			assertTrue(transactionWithMailSentTrue.getTrcMailSent());
			log.info("mail send is true");

		}	
		catch (EmailYetInTheSystemException e) {
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
	
	
	//TEST KO: try to send order email for wrong trc id
	@Test
	public void voucherOrdereMailTest_wrong_trcID_KO() {	

		CompanyDTO dto = fillCompanyDTO();	
		CompanyDTO dtoResult = null;
		Integer idUserInserted = null;

		try {

			log.info("Statring signUp...");
			dtoResult = (CompanyDTO) companyServicesInterface.signUp(dto);
			Session session = sessionFactory.getCurrentSession();
			session.getTransaction().commit();	
			
			idUserInserted=Integer.valueOf(dtoResult.getUsrId());
			
			List<Voucher> voucherList = insertVoucherPurchaseTransaction(dtoResult.getUsrId(), session);
			
			Transaction transaction = transactionDao.getLastTransaction(Integer.valueOf(dtoResult.getUsrId()));

			//set not existing transaction ID
			org.hibernate.Transaction txn1 = session.beginTransaction();
			emailServiceInterface.voucherOrdereMail("9999");			
	    	txn1.commit();
			
			Transaction transactionWithMailSentTrue = transactionDao.getLastTransaction(Integer.valueOf(dtoResult.getUsrId()));

	    	
			log.info("deleting transaction detail and vouchers...");
			for(Voucher voucher : voucherList) {
				deleteVoucherAndTransaction(voucher.getVchName(), session, Integer.valueOf(dtoResult.getUsrId()), idUserInserted);
			}
			
			for(Voucher voucher : voucherList) {	
				//delete data just inserted in DB		
				log.info("executing rollback...");
				rollBack(voucher.getVchName(), session, Integer.valueOf(dtoResult.getUsrId()), idUserInserted);
				log.info("rollback OK");
			
			}			
			log.info("transaction detail and vouchers deleted OK");
			
			log.info("assert mail send is true...");
			assertTrue(transactionWithMailSentTrue.getTrcMailSent());
			log.info("mail send is true");

		}	
		catch (EmailYetInTheSystemException e) {
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
	
	//-----UTILITIES------------------
	

	private List<Voucher> insertVoucherPurchaseTransaction(String usrId, Session session) {
		
    	List<Voucher> voucherList = new ArrayList<Voucher>();
	    try {    	
	    	
	    	//INSERT VOUCHER
			Voucher voucher1 = new Voucher();
			Voucher voucher2 = new Voucher();
			
		    String sDate1="25/10/2015";  
			Date date1=new SimpleDateFormat("dd/MM/yyyy").parse(sDate1);
		    String sDate2="25/10/2018";  
		    Date date2=new SimpleDateFormat("dd/MM/yyyy").parse(sDate2); 
			
			insertVouchers(session, voucherList, voucher1, voucher2, date1, date2);
	    	
	    	
	    	//INSERT TRANSACTION
	    	User company = new User();	
	    	company = userDao.findByPrimaryKey(company, Integer.valueOf(usrId));	    	
	    	
	    	Transaction transaction = new Transaction();
	    	transaction.setTrcTxId("testTx");
	    	transaction.setUsrIdA(company);
	    	//transaction.setUsrIdDa(company);
	    	transaction.setTrcType(TransactionType.COMPANY_BUY_VOUCHER.getDescription());
	    	transaction.setTrcDate(date1);
	    	transaction.setTrcState(Constants.TRANSACTION_STATUS_CONFIRMED);
	    	transaction.setTrcIban("878787878");
	    	transaction.setTrcPayed(Constants.TRANSACTION_PAYED);
	    	transaction.setTrcMailSent(false);
	    	
			org.hibernate.Transaction txn3 = session.beginTransaction();
			transactionDao.insert(transaction);
	    	txn3.commit();
	    	
	    	
	    	insertTransactionDetail(session, voucher1, voucher2, transaction);
			
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return voucherList;  		

	}
	
	
	
	
	
	private Transaction insertPurchaseTransaction(String companyId, Session session) {
		
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
	    	
	    	
	    	//INSERT PURCHASE TRANSACTION
	    	//recover the company
	    	User company = new User();	
	    	company = userDao.findByPrimaryKey(company, Integer.valueOf(companyId));
	    	
	    	//insert purchase transaction
	    	Transaction transaction = new Transaction();
	    	transaction.setTrcTxId("testTx");
	    	transaction.setUsrIdA(company);
	    	transaction.setTrcType(TransactionType.COMPANY_BUY_VOUCHER.getDescription());
	    	transaction.setTrcDate(date1);
	    	transaction.setTrcState(Constants.TRANSACTION_STATUS_CONFIRMED);
	    	transaction.setTrcIban("878787878");
	    	transaction.setTrcPayed(Constants.TRANSACTION_PAYED);
	    	transaction.setTrcMailSent(false);
	    	
			org.hibernate.Transaction txn3 = session.beginTransaction();
			transactionDao.insert(transaction);
	    	txn3.commit();
	    	
	    	
	    	//INSERT TRANSACTION DETAIL
	    	insertTransactionDetail(session, voucher1, voucher2, transaction);
	    	
	    	//RECOVER THE TRANSACTION
	    	Transaction insertedTransaction = transactionDao.getLastTransaction(Integer.valueOf(companyId));
	    	
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
	
	private Transaction insertAllocationTransaction(String companyId, String employeeId, Session session) {
		
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
	    	//recover the company
	    	User company = new User();	
	    	company = userDao.findByPrimaryKey(company, Integer.valueOf(companyId));
	    	//recover the employee
	    	User employee = new User();	
	    	employee = userDao.findByPrimaryKey(employee, Integer.valueOf(employeeId));
	    	//insert allocation transaction
	    	Transaction transaction = new Transaction();
	    	transaction.setTrcTxId("testTx");
	    	transaction.setUsrIdDa(company);
	    	transaction.setUsrIdA(employee);
	    	transaction.setTrcType(TransactionType.COMPANY_GIVE_VOUCHER_TO_EMPLOYEE.getDescription());
	    	transaction.setTrcDate(date1);
	    	transaction.setTrcState(Constants.TRANSACTION_STATUS_CONFIRMED);
	    	transaction.setTrcIban("878787878");
	    	transaction.setTrcPayed(Constants.TRANSACTION_PAYED);
	    	transaction.setTrcMailSent(false);
	    	
			org.hibernate.Transaction txn3 = session.beginTransaction();
			transactionDao.insert(transaction);
	    	txn3.commit();
	    	
	    	
	    	//INSERT TRANSACTION DETAIL
	    	insertTransactionDetail(session, voucher1, voucher2, transaction);
	    	
	    	//RECOVER THE TRANSACTION
	    	Transaction insertedTransaction = transactionDao.getLastTransaction(Integer.valueOf(companyId));
	    	
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
	
	
	private Transaction insertExpendTransaction(String idEmployeeInserted, String idMerchantInserted, Session session) {
		
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
	    	//recover the employee
	    	User employee = new User();	
	    	employee = userDao.findByPrimaryKey(employee, Integer.valueOf(idEmployeeInserted));
	    	//recover the merchant
	    	User merchant = new User();	
	    	merchant = userDao.findByPrimaryKey(merchant, Integer.valueOf(idMerchantInserted));

	    	//insert allocation transaction
	    	Transaction transaction = new Transaction();
	    	transaction.setTrcTxId("testTx");
	    	transaction.setUsrIdDa(employee);
	    	transaction.setUsrIdA(merchant);
	    	transaction.setTrcType(TransactionType.EMPLOYEE_GIVE_VOUCHER_TO_MERCHANT.getDescription());
	    	transaction.setTrcDate(date1);
	    	transaction.setTrcState(Constants.TRANSACTION_STATUS_CONFIRMED);
	    	transaction.setTrcIban("IbanForTest1234");
	    	transaction.setTrcPayed(Constants.TRANSACTION_PAYED);
	    	transaction.setTrcMailSent(false);
	    	
			org.hibernate.Transaction txn3 = session.beginTransaction();
			transactionDao.insert(transaction);
	    	txn3.commit();
	    	
	    	
	    	//INSERT TRANSACTION DETAIL
	    	insertTransactionDetail(session, voucher1, voucher2, transaction);
	    	
	    	//RECOVER THE TRANSACTION
	    	Transaction insertedTransaction = transactionDao.getLastTransaction(Integer.valueOf(idEmployeeInserted));
	    	
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
	
	
	private void rollBack(String vchName, Session session, Integer userID, Integer idUserInserted) {
		deleteVoucherByVchName(vchName, session);

		//delete the Transaction	
		deleteTransactionByUsrId(userID.toString(), session);
		
		//delete the Company		
		deleteCompanyByID(idUserInserted.toString(), session);
		
		//delete the User		
		deleteUserByID(idUserInserted.toString(), session);
		
	}
	
	private void deleteVoucherAndTransaction(String vchName, Session session, Integer userID, Integer idUserInserted) {
		
		//delete Transaction detail
		deleteTransactionDetailByVchName(vchName, session);
		
		//delete voucher
		deleteVoucherByVchName(vchName, session);

		
	}
	
	private void rollBackFromVoucherConfeMai(String vchName1, String vchName2, String idCompanyInserted,
			String idEmployeeInserted, String idMerchantInserted,  String trcID, Session session) {
		
		//delete first Transaction Detail
		deleteTransactionDetailByVchName(vchName1, session);
		
		//delete first Voucher
		deleteVoucherByVchName(vchName1, session);		
		
		//delete second Transaction Detail
		deleteTransactionDetailByVchName(vchName2, session);
		
		//delete second Voucher
		deleteVoucherByVchName(vchName2, session);		
		
		//delete the Transaction
		deleteTransactionByUsrId(idCompanyInserted, session);
		
		//delete the Transaction
		deleteTransactionByUsrId(idEmployeeInserted, session);
		
		//delete the Transaction
		deleteTransactionByUsrId(idMerchantInserted, session);
		
		//delete the Employee
		deleteEmployeeByID(idEmployeeInserted, session);
		
		//delete the employee User
		deleteUserByID(idEmployeeInserted, session);
		
		//delete the Company
		deleteCompanyByID(idCompanyInserted, session);
		
		//delete the company User
		deleteUserByID(idCompanyInserted, session);
		
		//delete the Merchant
		deleteMerchantByID(idMerchantInserted, session);
		
		//delete the merchant User
		deleteUserByID(idMerchantInserted, session);
		
	}
	



	private void deleteCompanyByID(String idCompanyInserted, Session session) {
		org.hibernate.Transaction txn8 = session.beginTransaction();
		Query query8 = session.createQuery("delete from Company where usr_id = " + idCompanyInserted);
		query8.executeUpdate();				
		txn8.commit();
	}
	
	private void deleteMerchantByID(String idMerchantInserted, Session session) {
		org.hibernate.Transaction txn8 = session.beginTransaction();
		Query query8 = session.createQuery("delete from Merchant where usr_id = " + idMerchantInserted);
		query8.executeUpdate();				
		txn8.commit();
	}



	private void deleteUserByID(String idEmployeeInserted, Session session) {
		org.hibernate.Transaction txn7 = session.beginTransaction();
		Query query7 = session.createQuery("delete from User where usr_id = " + idEmployeeInserted);
		query7.executeUpdate();				
		txn7.commit();
	}



	private void deleteEmployeeByID(String idEmployeeInserted, Session session) {
		org.hibernate.Transaction txn6 = session.beginTransaction();
		Query query6 = session.createQuery("delete from Employee where usr_id = " + idEmployeeInserted);
		query6.executeUpdate();				
		txn6.commit();
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
		dto.setUsrEmail("vouchainsystem@gmail.com");
		dto.setUsrPassword("Liga.654");
		dto.setCpyPartitaIva("000PARTITAIVA000");
		dto.setCpyCodiceFiscale("000CODICEFISCALE00");
		dto.setCpyPec("pietro.napolitano@cartesio.net");
		dto.setCpyRagioneSociale("ragione Sociale Test");
		dto.setCpyCity("àòùèéì");
		return dto;
	}
	
	private EmployeeDTO fillEmployeeDTO() {
		EmployeeDTO dto = new EmployeeDTO();
		dto.setUsrEmail("pedro7510@gmail.com");
		dto.setUsrPassword("Employee.654");
		dto.setEmpFirstName("Giorgiò");
		dto.setEmpLastName("Bianchi");
		return dto;
	}
	

	private MerchantDTO fillMerchantDTO() {
		MerchantDTO dto = new MerchantDTO();
		dto.setUsrEmail("pietro.napolitano@gmail.com");
		dto.setUsrPassword("Merchant.654");
		dto.setMrcAddress("via del corso 12");
		dto.setMrcBank("ING direct");
		dto.setMrcCity("Roma");
		dto.setMrcCodiceFiscale("123CodFiscale12è");
		dto.setMrcFirstNameRef("Ciro");
		dto.setMrcLastNameRef("Rossi");
		dto.setMrcRagioneSociale("Ragione sociale for Test");
		dto.setMrcIban("IbanTest123");
		return dto;
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

		voucher1.setVchValue(new BigDecimal(1));
		voucher2.setVchValue(new BigDecimal(1500.23));


		voucherList.add(voucher1);
		voucherList.add(voucher2);

		org.hibernate.Transaction txn1 = session.beginTransaction();
		voucherDaoImpl.insert(voucher1);
		txn1.commit();

		org.hibernate.Transaction txn2 = session.beginTransaction();
		voucherDaoImpl.insert(voucher2);
		txn2.commit();
	}



	
}
