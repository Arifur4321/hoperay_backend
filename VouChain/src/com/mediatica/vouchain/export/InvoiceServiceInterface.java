package com.mediatica.vouchain.export;

import java.io.File;
import java.util.List;

import com.mediatica.vouchain.entities.Transaction;
import com.mediatica.vouchain.entities.TransactionDetail;

public interface InvoiceServiceInterface {
	public File generateInvoiceXML(Transaction transaction, List<TransactionDetail> detailList);
}
