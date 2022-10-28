package com.mediatica.vouchain.dto;

public class RequestIssueNewVoucherDTO {
	
	private String name;
	private double value;
	private String expiration; //yyyy-MM-dd 
	private String address_to;
	private int quantity;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getExpiration() {
		return expiration;
	}
	public void setExpiration(String expiration) {
		this.expiration = expiration;
	}

	public int getQuantity() {
		return quantity;
	}
	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}
	public String getAddress_to() {
		return address_to;
	}
	public void setAddress_to(String address_to) {
		this.address_to = address_to;
	}
	
	
	public double getValue() {
		return value;
	}
	public void setValue(double value) {
		this.value = value;
	}
	@Override
	public String toString() {
		return "RequestIssueNewVoucherDTO [name=" + name + ", value=" + value + ", expiration=" + expiration
				+ ", address_to=" + address_to + ", quantity=" + quantity + "]";
	}
	
	
	

}
