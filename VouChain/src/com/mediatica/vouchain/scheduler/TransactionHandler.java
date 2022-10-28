package com.mediatica.vouchain.scheduler;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.mediatica.vouchain.config.enumerations.TransactionType;
import com.mediatica.vouchain.dao.TransactionDaoImpl;
import com.mediatica.vouchain.entities.Transaction;
import com.mediatica.vouchain.rest.client.BlockChainRestClient;

@Transactional(propagation = Propagation.REQUIRES_NEW)
@Component
@PropertySource("file:${vouchain_home}/configurations/config.properties")
public class TransactionHandler {
	
	private static org.slf4j.Logger log = LoggerFactory.getLogger(TransactionHandler.class);

	@Autowired
	TransactionHandlerUtils tu;
	
	@Autowired
	TransactionDaoImpl transactionDaoImpl;
	
	
	@Autowired
	BlockChainRestClient blockChainClient;
	
	@Value("${transaction_handler_enabled}")
	private String transactionHandlerEnabled;
	
	@Value("${transaction_delta_time}")
	private int transactionDeltaTime;
	
	@Value("${confirmation_number}")
	private int confirmationNumber;
	

	

	@Scheduled(fixedRateString = "${transaction_handler_elapse_time}")
	public void TransactionHandlerScheduler() {
		if(transactionHandlerEnabled.equalsIgnoreCase("true")) {
			log.info("***** TRANSACTION HANDLER SCHEDULER STARTED AT {}*****",(new Date()));

				Date now = new Date();
				List<Transaction> pendingTransactions= new ArrayList<Transaction>();
				pendingTransactions=transactionDaoImpl.getPendingTransactions(now, transactionDeltaTime); 
				if(pendingTransactions!=null && !pendingTransactions.isEmpty()) {
					log.info("Found {} transactions to verify by blockchain API",pendingTransactions.size());
					System.out.println("Found {} transactions to verify by blockchain API"+ pendingTransactions.size());
					for(Transaction t:pendingTransactions) {
						try {
							if(t.getTrcType().equalsIgnoreCase(TransactionType.NEW_USER.getDescription())) {
								tu.manageNewUserCase(t);
							}else {
								log.info("{} it is not a transactione of type NWU ",t.getTrcId());
								tu.managingOtherCases(t);
							}
						}catch(Exception e) {
							log.error("Error in transaction handler scheduler ",e);
						}
					}//end for
				}else {
					log.info("No transaction found to verify");
						
				}
				

			log.info("***** TRANSACTION HANDLER SCHEDULER ENDED AT {}*****",(new Date()));
		}else {
			log.warn("The TRANSACTION HANDLER SCHEDULER is not enabled");
		}
		
		
	}



}
