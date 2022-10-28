package com.mediatica.vouchain.dto;

import java.util.List;

public class VoucherAllocationDTO {
	
	private String fromId;
	private String bckChainFromAddress;//utility field
	
	private String toId;
	private String bckChainToAddress;//utility field
	
	private String privateKey;
	
	private String profile;
	
    private String transactionId; //utility field
    
    private String iban; //only for redeem
    
    private String accountHolder; //only for redeem
    
    private String qrValue;
	
	private List<VoucherDTO> voucherList;
  
	public String getFromId() {
		return fromId;
	}

	public void setFromId(String fromId) {
		this.fromId = fromId;
	}

	public String getToId() {
		return toId;
	}

	public void setToId(String toId) {
		this.toId = toId;
	}

	public List<VoucherDTO> getVoucherList() {
		return voucherList;
	}

	public void setVoucherList(List<VoucherDTO> voucherList) {
		this.voucherList = voucherList;
	}

	public String getBckChainFromAddress() {
		return bckChainFromAddress;
	}

	public void setBckChainFromAddress(String bckChainFromAddress) {
		this.bckChainFromAddress = bckChainFromAddress;
	}

	public String getBckChainToAddress() {
		return bckChainToAddress;
	}

	public void setBckChainToAddress(String bckChainToAddress) {
		this.bckChainToAddress = bckChainToAddress;
	}
	
	

	public String getTransactionId() {
		return transactionId;
	}

	public void setTransactionId(String transactionId) {
		this.transactionId = transactionId;
	}
	
	

	public String getProfile() {
		return profile;
	}

	public void setProfile(String profile) {
		this.profile = profile;
	}

	public String getQrValue() {
		return qrValue;
	}

	public void setQrValue(String qrValue) {
		this.qrValue = qrValue;
	}

	@Override
	public String toString() {
		return "VoucherAllocationDTO [companyId=" + fromId + ", bckCompanyAddress=" + bckChainFromAddress
				+ ", employeeId=" + toId + ", bckEmployeeAddress=" + bckChainToAddress + ", voucherList="
				+ voucherList + "]";
	}

	public String getIban() {
		return iban;
	}

	public void setIban(String iban) {
		this.iban = iban;
	}

	public String getAccountHolder() {
		return accountHolder;
	}

	public void setAccountHolder(String accountHolder) {
		this.accountHolder = accountHolder;
	}

	public String getPrivateKey() {
		return privateKey;
	}

	public void setPrivateKey(String privateKey) {
		this.privateKey = privateKey;
	}
	
	
	
	

}
