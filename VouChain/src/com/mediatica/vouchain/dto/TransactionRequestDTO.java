package com.mediatica.vouchain.dto;

public class TransactionRequestDTO {
	
	private String startDate;
	private String endDate;
	private String usrId;
	private String trcState; //confirmed-pending
	private String profile; //employee,company,merchant
	private String trcPayed; //payed;not_payed
	
	public String getStartDate() {
		return startDate;
	}
	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}
	public String getEndDate() {
		return endDate;
	}
	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}
	public String getUsrId() {
		return usrId;
	}
	public void setUsrId(String usrId) {
		this.usrId = usrId;
	}
	public String getTrcState() {
		return trcState;
	}
	public void setTrcState(String trcState) {
		this.trcState = trcState;
	}
	public String getProfile() {
		return profile;
	}
	public void setProfile(String profile) {
		this.profile = profile;
	}
	public String getTrcPayed() {
		return trcPayed;
	}
	public void setTrcPayed(String trcPayed) {
		this.trcPayed = trcPayed;
	}
	
	
	
	

}
