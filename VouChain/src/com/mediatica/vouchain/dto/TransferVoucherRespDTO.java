package com.mediatica.vouchain.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class TransferVoucherRespDTO {
	private String raw_tx;
	private boolean is_signed;
	public String getRaw_tx() {
		return raw_tx;
	}
	public void setRaw_tx(String raw_tx) {
		this.raw_tx = raw_tx;
	}
	public boolean isIs_signed() {
		return is_signed;
	}
	public void setIs_signed(boolean is_signed) {
		this.is_signed = is_signed;
	}




	
}
