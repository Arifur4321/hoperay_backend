package com.mediatica.vouchain.dto;
/**
 * 
 * @author Pietro Napolitano
 *
 */

public class SimpleResponseDTO {
	
	private String status;
	
	private String errorDescription;
	
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
