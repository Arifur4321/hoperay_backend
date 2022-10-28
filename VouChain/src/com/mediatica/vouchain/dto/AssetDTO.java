package com.mediatica.vouchain.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class AssetDTO {
	
	private String name;
	
	private long qty;
	

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public long getQty() {
		return qty;
	}
	public void setQty(long qty) {
		this.qty = qty;
	}
	
}
