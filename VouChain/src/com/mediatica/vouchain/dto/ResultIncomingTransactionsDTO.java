package com.mediatica.vouchain.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
@JsonIgnoreProperties(ignoreUnknown = true)
public class ResultIncomingTransactionsDTO {
	
	private String type;
	
	List<IncomingTransactionsDTO> transactions;

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public List<IncomingTransactionsDTO> getTransactions() {
		return transactions;
	}

	public void setTransactions(List<IncomingTransactionsDTO> transactions) {
		this.transactions = transactions;
	}
	
	
	
}
