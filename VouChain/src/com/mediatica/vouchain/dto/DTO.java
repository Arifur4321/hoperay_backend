package com.mediatica.vouchain.dto;

import java.text.ParseException;

/**
 * 
 * @author Pietro Napolitano
 *
 */

public abstract class DTO {  

	private String status;
	private String errorDescription;
	

	
    protected String usrId;
    protected String usrBchAddress;
    protected String usrPrivateKey;
    protected String usrEmail;
    protected String usrPassword;
    protected String usrActive;
   // private String usrSalt;
	
	
    public String getUsrActive() {
		return usrActive;
	}
	public void setUsrActive(String usrActive) {
		this.usrActive = usrActive;
	}
	public String getUsrId() {
		return usrId;
	}
	public void setUsrId(String usrId) {
		this.usrId = usrId;
	}
	public String getUsrBchAddress() {
		return usrBchAddress;
	}
	public void setUsrBchAddress(String usrBchAddress) {
		this.usrBchAddress = usrBchAddress;
	}
	public String getUsrPrivateKey() {
		return usrPrivateKey;
	}
	public void setUsrPrivateKey(String usrPrivateKey) {
		this.usrPrivateKey = usrPrivateKey;
	}
	public String getUsrEmail() {
		return usrEmail;
	}
	public void setUsrEmail(String usrEmail) {
		this.usrEmail = usrEmail;
	}
	public String getUsrPassword() {
		return usrPassword;
	}
	public void setUsrPassword(String usrPassword) {
		this.usrPassword = usrPassword;
	}
    


	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getErrorDescription() {
		return errorDescription;
	}

	public void setErrorDescription(String errorDescription) {
		this.errorDescription = errorDescription;
	}
	
	public abstract Object wrap(Object entity,boolean includePassword) ;
	
	public Object wrap(Object usr) {
		return wrap(usr,false);
	}
	
	public abstract Object unwrap(Object entity) throws ParseException ;
	


}
