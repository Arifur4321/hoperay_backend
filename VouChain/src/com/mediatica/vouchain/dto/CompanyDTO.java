package com.mediatica.vouchain.dto;

import com.mediatica.vouchain.entities.Company;
import com.mediatica.vouchain.entities.User;
/**
 * 
 * @author Pietro Napolitano
 *
 */

public class CompanyDTO extends DTO {

    private static final long serialVersionUID = 1L;

    
    private String cpyPec;
    private String cpyCodiceFiscale;
    private String cpyPartitaIva;
    private String cpyContractChecked;
    private byte[] cpyContract;
//    private String cpyActive; 
    
    private byte[] cpyHashedContract;
    private String cpyFirstNameRef;
    private String cpyLastNameRef;
    private String cpyPhoneNoRef;
    private String cpyCuu;
    
    private String cpyRagioneSociale;
    private String cpyAddress;
    private String cpyCity;
    private String cpyProv;
    private String cpyZip;
    
    
	

	public String getCpyPec() {
		return cpyPec;
	}
	public void setCpyPec(String cpyPec) {
		this.cpyPec = cpyPec;
	}
	public String getCpyCodiceFiscale() {
		return cpyCodiceFiscale;
	}
	public void setCpyCodiceFiscale(String cpyCodiceFiscale) {
		this.cpyCodiceFiscale = cpyCodiceFiscale;
	}
	public String getCpyPartitaIva() {
		return cpyPartitaIva;
	}
	public void setCpyPartitaIva(String cpyPartitaIva) {
		this.cpyPartitaIva = cpyPartitaIva;
	}
	public String getCpyContractChecked() {
		return cpyContractChecked;
	}
	public void setCpyContractChecked(String cpyContractChecked) {
		this.cpyContractChecked = cpyContractChecked;
	}
	public byte[] getCpyContract() {
		return cpyContract;
	}
	public void setCpyContract(byte[] cpyContract) {
		this.cpyContract = cpyContract;
	}
//	public String getCpyActive() {
//		return cpyActive;
//	}
//	public void setCpyActive(String cpyActive) {
//		this.cpyActive = cpyActive;
//	}
	
	
	public byte[] getCpyHashedContract() {
		return cpyHashedContract;
	}
	public void setCpyHashedContract(byte[] cpyHashedContract) {
		this.cpyHashedContract = cpyHashedContract;
	}
	public String getCpyFirstNameRef() {
		return cpyFirstNameRef;
	}
	public void setCpyFirstNameRef(String cpyFirstNameRef) {
		this.cpyFirstNameRef = cpyFirstNameRef;
	}
	public String getCpyLastNameRef() {
		return cpyLastNameRef;
	}
	public void setCpyLastNameRef(String cpyLastNameRef) {
		this.cpyLastNameRef = cpyLastNameRef;
	}
	public String getCpyPhoneNoRef() {
		return cpyPhoneNoRef;
	}
	public void setCpyPhoneNoRef(String cpyPhoneNoRef) {
		this.cpyPhoneNoRef = cpyPhoneNoRef;
	}
	public String getCpyCuu() {
		return cpyCuu;
	}
	public void setCpyCuu(String cpyCuu) {
		this.cpyCuu = cpyCuu;
	}
	
	
	
	
	public String getCpyRagioneSociale() {
		return cpyRagioneSociale;
	}
	public void setCpyRagioneSociale(String cpyRagioneSociale) {
		this.cpyRagioneSociale = cpyRagioneSociale;
	}
	public String getCpyAddress() {
		return cpyAddress;
	}
	public void setCpyAddress(String cpyAddress) {
		this.cpyAddress = cpyAddress;
	}
	public String getCpyCity() {
		return cpyCity;
	}
	public void setCpyCity(String cpyCity) {
		this.cpyCity = cpyCity;
	}
	public String getCpyProv() {
		return cpyProv;
	}
	public void setCpyProv(String cpyProv) {
		this.cpyProv = cpyProv;
	}
	
	public String getCpyZip() {
		return cpyZip;
	}
	public void setCpyZip(String cpyZip) {
		this.cpyZip = cpyZip;
	}



	
	
	@Override
	public Object wrap(Object usr,boolean includePassword) {
		User user = (User)usr;
		CompanyDTO company = new CompanyDTO();
	    //user fields
		company.usrId=user.getUsrId()!=null?user.getUsrId().toString():"";
		//company.usrBchAddress=user.getUsrBchAddress();
		//company.usrPrivateKey=user.getUsrPrivateKey();
		
		company.usrEmail=user.getUsrEmail();
		if(includePassword) {
			company.usrPassword=user.getUsrPassword(); 
		}
		//company fields
		company.cpyPec=user.getCompany().getCpyPec();
		company.cpyCodiceFiscale=user.getCompany().getCpyCodiceFiscale();
		company.cpyPartitaIva=user.getCompany().getCpyPartitaIva();
		company.cpyCuu= user.getCompany().getCpyCuu();
		company.cpyFirstNameRef=user.getCompany().getCpyFirstNameRef();
		company.cpyLastNameRef=user.getCompany().getCpyLastNameRef();
		company.cpyPhoneNoRef= user.getCompany().getCpyPhoneNoRef();
	    
		company.cpyRagioneSociale=user.getCompany().getCpyRagioneSociale();
		company.cpyAddress=user.getCompany().getCpyAddress();
		company.cpyCity=user.getCompany().getCpyCity();
		company.cpyProv=user.getCompany().getCpyProv();
		company.cpyZip=user.getCompany().getCpyZip();
		
		String contractChecked = "false";
	    if(user.getCompany().getCpyContractChecked()!=null && user.getCompany().getCpyContractChecked()==true) {
	    	contractChecked = "true";
	    }
	    company.cpyContractChecked=contractChecked;
	    String usrActive = "false";
	    if(user.getCompany().getUser().getUsrActive()!=null && user.getCompany().getUser().getUsrActive()==true) {
	    	usrActive = "true";
	    }
	    company.usrActive=usrActive;

		return company;
	}
	@Override
	public Object unwrap(Object companyDTO) {
		CompanyDTO company = (CompanyDTO)companyDTO;
		
		Company cmp = new Company();
		User usr = new User();
		

		
		cmp.setCpyCodiceFiscale(company.getCpyCodiceFiscale());
		cmp.setCpyPartitaIva(company.getCpyPartitaIva());
		cmp.setCpyPec(company.getCpyPec());
		
		
		cmp.setCpyCuu(company.getCpyCuu());
		cmp.setCpyFirstNameRef(company.getCpyFirstNameRef());
		cmp.setCpyLastNameRef(company.getCpyLastNameRef());
		cmp.setCpyPhoneNoRef(company.getCpyPhoneNoRef());
	    
		cmp.setCpyRagioneSociale(company.getCpyRagioneSociale());
		cmp.setCpyAddress(company.getCpyAddress());
		cmp.setCpyCity(company.getCpyCity());
		cmp.setCpyProv(company.getCpyProv());
		cmp.setCpyZip(company.getCpyZip());
		
		if(company.getUsrId()!=null && !company.getUsrId().equals("")) {
			cmp.setUsrId(usr.getUsrId());
			usr.setUsrId(Integer.parseInt(company.getUsrId()));
		}
		
		boolean contractCheched=false;
		if(company.getCpyContractChecked()!= null && company.getCpyContractChecked().equals("true")) {
			contractCheched=true;
		}
		cmp.setCpyContractChecked(contractCheched);
		
		usr.setCompany(cmp);
		boolean isActive=false;
		if(company.getUsrActive()!= null && company.getUsrActive().equals("true")) {
			isActive=true;
		}
		usr.setUsrActive(isActive);
		usr.setUsrEmail(company.getUsrEmail());
		usr.setUsrPassword(company.getUsrPassword());
//		usr.setUsrPrivateKey(company.getUsrPrivateKey());
//		usr.setUsrSalt(company.getUsrSalt());
//		usr.setUsrBchAddress(company.getUsrBchAddress());

		usr.setCompany(cmp);
		
		return usr;
	}

	
	@Override
	public String toString() {
		return "CompanyDTO [usrId=" + usrId + ", usrBchAddress=" + usrBchAddress + ", usrPrivateKey=" + usrPrivateKey
				+ ", usrEmail=" + usrEmail + ", cpyPec="
				+ cpyPec + ", cpyCodiceFiscale=" + cpyCodiceFiscale + ", cpyPartitaIva=" + cpyPartitaIva
				+ ", cpyContractChecked=" + cpyContractChecked + "]";
	}

	
	

}
