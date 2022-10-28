package com.mediatica.vouchain.bankingServices;

import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.mediatica.vouchain.entities.Transaction;



@Service
public class BankingServices {

	private static org.slf4j.Logger log = LoggerFactory.getLogger(BankingServices.class);
	
	
	public boolean verifyCompaniesPayments(Transaction t) {
		log.debug("This is a stub and returns always true. Transaction ID {}",t.getTrcId());
		return true;
	}
	
	public boolean payMerchants(Transaction t) {
		log.debug("This is a stub and returns always true. Transaction ID {}",t.getTrcId());
		return true;
	}

	
}
