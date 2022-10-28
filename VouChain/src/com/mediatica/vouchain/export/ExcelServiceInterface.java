package com.mediatica.vouchain.export;

import java.util.List;

import com.mediatica.vouchain.dto.TransactionDTO;

public interface ExcelServiceInterface {
	
	public byte[] exportTransaction(List<TransactionDTO> transactionList, String usrId, String startDate, String endDate);

}
