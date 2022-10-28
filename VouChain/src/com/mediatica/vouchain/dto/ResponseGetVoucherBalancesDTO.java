package com.mediatica.vouchain.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ResponseGetVoucherBalancesDTO {

	private List<VoucherBlckChainDTO> result;
	private String id;
	private String error;
	
	public List<VoucherBlckChainDTO> getResult() {
		return result;
	}
	public void setResult(List<VoucherBlckChainDTO> result) {
		this.result = result;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getError() {
		return error;
	}
	public void setError(String error) {
		this.error = error;
	}

	
	
	
}
