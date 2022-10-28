package com.mediatica.vouchain.scheduler;

import java.util.Collection;
import java.util.Date;
import java.util.List;



import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.mediatica.vouchain.bankingServices.BankingServices;
import com.mediatica.vouchain.config.Constants;
import com.mediatica.vouchain.config.enumerations.TransactionStatus;
import com.mediatica.vouchain.config.enumerations.TransactionType;
import com.mediatica.vouchain.dao.TransactionDaoImpl;
import com.mediatica.vouchain.dto.RequestIssueMoreVoucher;
import com.mediatica.vouchain.dto.RequestIssueNewVoucherDTO;
import com.mediatica.vouchain.dto.ResponseIssueMoreVoucher;
import com.mediatica.vouchain.dto.ResponseIssueNewVoucherDTO;
import com.mediatica.vouchain.entities.Transaction;
import com.mediatica.vouchain.entities.TransactionDetail;
import com.mediatica.vouchain.entities.Voucher;
import com.mediatica.vouchain.exceptions.VoucherYetInTheSystemException;
import com.mediatica.vouchain.export.InvoiceServiceImpl;
import com.mediatica.vouchain.rest.client.BlockChainRestClient;
import com.mediatica.vouchain.servicesImpl.EmailServiceImpl;
import com.mediatica.vouchain.servicesImpl.VoucherServiceImpl;

 @Transactional
 @Component
 @PropertySource("file:${vouchain_home}/configurations/config.properties")
public class PaymentCheckScheduler {

	 
	private static org.slf4j.Logger log = LoggerFactory.getLogger(PaymentCheckScheduler.class);


	@Autowired
	TransactionDaoImpl transactionDaoImpl;
	
	@Autowired 
	BankingServices bankingService;
	
	@Autowired
	BlockChainRestClient blockChainClient;
	
	@Autowired 
	VoucherServiceImpl voucherService;
	
	@Autowired
	EmailServiceImpl emailServiceImpl;
	
	@Value("${voucher_yet_in_blockchain}")
	private String voucherYetInTheSystem;
	
	@Value("${payment_scheduler_enabled}")
	private String paymentSchedulerEnabled;
	
	@Value("${payment_to_merchant_scheduler_enabled}")
	private String paymentToMerchantScheduler;
	
	
	@Scheduled(fixedRateString =  "${payment_scheduler_elapse_time}")
	public void paymentCheckScheduler() {
		if(paymentSchedulerEnabled.equalsIgnoreCase("true")) {
			log.info("***** PAYMENT CHECK SCHEDULER STARTED AT {}*****",(new Date()));
			try {
					List<Transaction> transactions =transactionDaoImpl.getTransactionListByPayedandType(Constants.TRANSACTION_NOT_PAYED,TransactionType.COMPANY_BUY_VOUCHER.getDescription());
					if(transactions!=null && !transactions.isEmpty()) {
						log.info("Found {} transactions to verify by Bank API",transactions.size());
						for(Transaction t:transactions) {
							if(bankingService.verifyCompaniesPayments(t)) {
								log.debug("Generating the invoice for transaction {}",t.getTrcId());
								//t.setTrcInvoice(invoiceService.generateInvoice(t));
								//t.setTrcPayed(Constants.TRANSACTION_PAYED);
								//transactionDaoImpl.update(t);
								String bckChainAddress =  t.getUsrIdA().getUsrBchAddress();
								 Collection<TransactionDetail> tdList = t.getTransactionDetailCollection();
								 //even if in the case of the new voucher we will have ever only one Voucher/Detail 
								 String transactionBckId= null;
								 for(TransactionDetail detail:tdList) {
									 Voucher v=detail.getVoucher();
									 Long vchQuantity = detail.getTrdQuantity();
									 String vchName = v.getVchName();
									 double value = v.getVchValue().doubleValue();
									 if(!v.getVchState()) {
										 log.info("Entering in new voucher for transaction id: {}",detail.getTransactionDetailPK().getTrcId());
										RequestIssueNewVoucherDTO request = new RequestIssueNewVoucherDTO();
										request.setAddress_to(bckChainAddress);
										request.setExpiration(Constants.FORMATTER_YYYY_MM_DD.format(v.getVchEndDate()));
										request.setName(vchName);
										request.setQuantity(vchQuantity.intValue());
										request.setValue(value); 
										ResponseIssueNewVoucherDTO response = blockChainClient.invokeIssueNewVoucher(request);
										if(response.getError()==null || response.getError().isEmpty()) {
											detail.getVoucher().setVchState(true);
											transactionBckId = response.getResult();
										}else {
											if(response.getMessage().toLowerCase().contains(voucherYetInTheSystem.toLowerCase())) {
												log.debug("the voucher named {} is yet in the blockchain system",vchName);
												throw new VoucherYetInTheSystemException("Voucher is yet on the blckchain system");
											}
											throw new Exception("Error calling blockchain: "+response.getError());
										}
									 }else {
										 log.info("Entering in more voucher for transaction id: {}",detail.getTransactionDetailPK().getTrcId());
										 RequestIssueMoreVoucher request = new RequestIssueMoreVoucher();
										 request.setAddress_to(bckChainAddress);
										 request.setVoucher_name(vchName);
										 request.setQuantity(vchQuantity.intValue());
										 ResponseIssueMoreVoucher response = blockChainClient.invokeIssueMoreVoucher(request);
											if(response.getError()==null || response.getError().isEmpty()) {
												detail.getVoucher().setVchState(true); 
												transactionBckId = response.getResult();
											}else {
												if(response.getMessage().toLowerCase().contains(voucherYetInTheSystem.toLowerCase())) {
													log.debug("the voucher named {} is yet in the blockchain system",vchName);
													throw new VoucherYetInTheSystemException("Voucher is yet on the blckchain system");
												}
												throw new Exception("Error calling blockchain: "+response.getError());
											}
									 }
								 }
								t.setTrcTxId(transactionBckId);
								t.setTrcState(TransactionStatus.CONFIRMED.getValue());
								t.setTrcPayed(Constants.TRANSACTION_PAYED);
								transactionDaoImpl.update(t);	
								emailServiceImpl.paymentOrderConfeMail(t.getTrcId().toString());
							}else {
								log.info("Transaction {} is not yet payed ",t.getTrcId());
							}
						}
						
					}else {
						log.info("No transactions to verify found");
					}
			}catch(Exception e) {
				log.error("Error in payment check scheduler ",e);
			}
			log.info("***** PAYMENT CHECK SCHEDULER ENDED AT {}*****",(new Date()));
		}else {
			log.warn("The PAYMENT CHECK SCHEDULER is not enabled");
		} 
	}
	

	@Scheduled(fixedRateString =  "${payment_to_merchant_scheduler_elapse_time}")
	public void payMerchantsScheduler() {
		if(paymentToMerchantScheduler.equalsIgnoreCase("true")) {
			log.info("***** PAYMENT TO MERCHANTS CHECK SCHEDULER STARTED AT {}*****",(new Date()));
			try {
				List<Transaction> transactions =transactionDaoImpl.getTransactionListByPayedandType(Constants.TRANSACTION_NOT_PAYED,TransactionType.REDEEM_VOUCHER.getDescription());
				if(transactions!=null && !transactions.isEmpty()) {
					log.info("Found {} transactions to pay by Bank API",transactions.size());
					for(Transaction t:transactions) {
						boolean isPayedCorretly = bankingService.payMerchants(t);
						if(isPayedCorretly) {
							t.setTrcPayed(Constants.TRANSACTION_PAYED);
							transactionDaoImpl.update(t);
						}else {
							log.warn("Some problem encountered paying transaction {} let s try later",t);
						}
					}
				}else {
					log.info("No transactions to pay found");
				}
				
			}catch(Exception e) {
				log.error("Error in payMerchantsScheduler ",e);
			}
			log.info("***** PAYMENT TO MERCHANTS CHECK SCHEDULER ENDED AT {}*****",(new Date()));		
		}else {
			log.warn("The PAYMENT CHECK SCHEDULER is not enabled");
		}
		
	}
	
	
}
