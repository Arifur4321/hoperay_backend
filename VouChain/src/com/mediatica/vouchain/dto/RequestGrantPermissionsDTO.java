package com.mediatica.vouchain.dto;
/**
 * 
 * @author Pietro Napolitano
 *
 */

public class RequestGrantPermissionsDTO {
	
	String address;
	
	String role;

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}
	
	
}
