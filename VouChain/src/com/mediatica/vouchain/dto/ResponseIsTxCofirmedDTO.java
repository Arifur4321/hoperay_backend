package com.mediatica.vouchain.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ResponseIsTxCofirmedDTO {
	
	private ConfirmationDTO result;
	
	private String error;
	
	public ConfirmationDTO getResult() {
		
		return result;
	}
	public void setResult(ConfirmationDTO result) {
		this.result = result;
	}
	public String getError() {
		return error;
	}
	public void setError(String error) {
		this.error = error;
	}
}
