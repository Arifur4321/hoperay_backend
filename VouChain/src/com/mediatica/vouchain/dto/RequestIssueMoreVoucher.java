package com.mediatica.vouchain.dto;

public class RequestIssueMoreVoucher {

	private String voucher_name;
	private String address_to;
	private int quantity;
	public String getVoucher_name() {
		return voucher_name;
	}
	public void setVoucher_name(String voucher_name) {
		this.voucher_name = voucher_name;
	}
	public String getAddress_to() {
		return address_to;
	}
	public void setAddress_to(String address_to) {
		this.address_to = address_to;
	}
	public int getQuantity() {
		return quantity;
	}
	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}
	
	
	
}
