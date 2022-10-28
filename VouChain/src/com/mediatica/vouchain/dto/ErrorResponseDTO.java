package com.mediatica.vouchain.dto;
/**
 * 
 * @author Pietro Napolitano
 *
 */

public class ErrorResponseDTO {

	private int code;
	
	private String status;
	
	private String errorDescription;
	
	public int getCode() {
		return code;
	}
	public void setCode(int code) {
		this.code = code;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getErrorDescription() {
		return errorDescription;
	}
	public void setErrorDescription(String errorDescription) {
		this.errorDescription = errorDescription;
	}

	
	
	
}
