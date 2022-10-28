package com.mediatica.vouchain.dto;

import java.util.List;

public class RequestBurnVouchersDTO {
	private String address_from;
	private List<TransferDTO> transfer;
	public String getAddress_from() {
		return address_from;
	}
	public void setAddress_from(String address_from) {
		this.address_from = address_from;
	}
	public List<TransferDTO> getTransfer() {
		return transfer;
	}
	public void setTransfer(List<TransferDTO> transfer) {
		this.transfer = transfer;
	}


}
