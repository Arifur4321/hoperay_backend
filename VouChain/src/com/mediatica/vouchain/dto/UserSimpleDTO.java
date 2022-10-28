package com.mediatica.vouchain.dto;
/**
 * 
 * @author Pietro Napolitano
 *
 */
public class UserSimpleDTO {
	private String usrEmail;
	
	private String profile;
	
	private String usrPassword;
	
	private String resetCode;
	
	public String getUsrEmail() {
		return usrEmail;
	}
	public void setUsrEmail(String usrEmail) {
		this.usrEmail = usrEmail;
	}
	public String getProfile() {
		return profile;
	}
	public void setProfile(String profile) {
		this.profile = profile;
	}
	public String getUsrPassword() {
		return usrPassword;
	}
	public void setUsrPassword(String usrPassword) {
		this.usrPassword = usrPassword;
	}
	public String getResetCode() {
		return resetCode;
	}
	public void setResetCode(String resetCode) {
		this.resetCode = resetCode;
	}
	
	
	
}
