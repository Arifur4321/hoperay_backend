package com.mediatica.vouchain.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class GetIncomingTransactionsResponseDTO {
	private ResultIncomingTransactionsDTO result;
	private String error;
	

	public ResultIncomingTransactionsDTO getResult() {
		return result;
	}

	public void setResult(ResultIncomingTransactionsDTO result) {
		this.result = result;
	}

	public String getError() {
		return error;
	}

	public void setError(String error) {
		this.error = error;
	}
	
}
