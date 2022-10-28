package com.mediatica.vouchain.dto;

import java.util.List;

public class DestinationDTO {

	private String address_to;
	List<TransferDTO> transfer;

	public List<TransferDTO> getTransfer() {
		return transfer;
	}
	public void setTransfer(List<TransferDTO> transfer) {
		this.transfer = transfer;
	}
	public String getAddress_to() {
		return address_to;
	}
	public void setAddress_to(String address_to) {
		this.address_to = address_to;
	}
	
	
	
	
}
