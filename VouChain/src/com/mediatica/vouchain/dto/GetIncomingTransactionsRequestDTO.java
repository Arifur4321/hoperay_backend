package com.mediatica.vouchain.dto;

import java.util.List;

public class GetIncomingTransactionsRequestDTO {
	private String address;
	private List<String> assets;
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public List<String> getAssets() {
		return assets;
	}
	public void setAssets(List<String> assets) {
		this.assets = assets;
	}
	
	
	
}
