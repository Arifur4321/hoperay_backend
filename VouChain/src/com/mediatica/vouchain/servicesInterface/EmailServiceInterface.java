package com.mediatica.vouchain.servicesInterface;

import com.mediatica.vouchain.entities.Transaction;

public interface EmailServiceInterface {

	void voucherOrdereMail(String trcId);

	void voucherConfeMail(String trcId) throws Exception;

	void paymentOrderConfeMail(String trcId) throws Exception;

	void paymentRedeemConfeMail(String trcId) throws Exception;

	void voucherConfeMail2(Transaction transaction) throws Exception;

}
