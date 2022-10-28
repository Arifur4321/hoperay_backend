package com.mediatica.vouchain.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ResponseTransferVoucherFromManyDTO {

	private TransferVoucherRespDTO result;
	private String error;
	private String message;
	public TransferVoucherRespDTO getResult() {
		return result;
	}
	public void setResult(TransferVoucherRespDTO result) {
		this.result = result;
	}
	public String getError() {
		return error;
	}
	public void setError(String error) {
		this.error = error;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	
	
}
