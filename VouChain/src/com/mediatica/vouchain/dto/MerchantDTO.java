package com.mediatica.vouchain.dto;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;

import com.mediatica.vouchain.entities.Merchant;
import com.mediatica.vouchain.entities.User;
import com.mediatica.vouchain.servicesImpl.GeographicServiceImpl;
/**
 * 
 * @author Pietro Napolitano
 *
 */

public class MerchantDTO extends DTO  {

	    private String mrcRagioneSociale;

	    private String mrcPartitaIva;

	    private String mrcCodiceFiscale;

	    private String mrcAddress;

	    private String mrcCity;

	    private String mrcProv;

	    private String mrcZip;

	    private String mrcPhoneNo;

	    private String mrcFirstNameReq;

	    private String mrcLastNameReq;

	    private String mrcRoleReq;

	    private String mrcOfficeName;

	    private String mrcPhoneNoOffice;

	    private String mrcFirstNameRef;

	    private String mrcLastNameRef;

	    private String mrcAddressOffice;

	    private String mrcCityOffice;

	    private String mrcProvOffice;

	    private String mrcIban;

	    private String mrcBank;

	    private String mrcChecked;
	    
	    private String mrcAccessType;
	    
	    private String mrcPinCode;
	    
	    private String mrcCash;
	    
	    private String mrcLongitude;
	    
	    private String mrcLatitude;
	    
	    private String mrcLinkMap;
	    
	    private String mrcDistanza;
	    
	    private String mrcNotificationEnabled;
	    
	    private byte[] mrcImageProfile;
	    
	    
	    
	    
		public String getMrcRagioneSociale() {
			return mrcRagioneSociale;
		}

		public void setMrcRagioneSociale(String mrcRagioneSociale) {
			this.mrcRagioneSociale = mrcRagioneSociale;
		}

		public String getMrcPartitaIva() {
			return mrcPartitaIva;
		}

		public void setMrcPartitaIva(String mrcPartitaIva) {
			this.mrcPartitaIva = mrcPartitaIva;
		}

		public String getMrcCodiceFiscale() {
			return mrcCodiceFiscale;
		}

		public void setMrcCodiceFiscale(String mrcCodiceFiscale) {
			this.mrcCodiceFiscale = mrcCodiceFiscale;
		}

		public String getMrcAddress() {
			return mrcAddress;
		}

		public void setMrcAddress(String mrcAddress) {
			this.mrcAddress = mrcAddress;
		}

		public String getMrcCity() {
			return mrcCity;
		}

		public void setMrcCity(String mrcCity) {
			this.mrcCity = mrcCity;
		}

		public String getMrcProv() {
			return mrcProv;
		}

		public void setMrcProv(String mrcProv) {
			this.mrcProv = mrcProv;
		}

		public String getMrcZip() {
			return mrcZip;
		}

		public void setMrcZip(String mrcZip) {
			this.mrcZip = mrcZip;
		}

		public String getMrcPhoneNo() {
			return mrcPhoneNo;
		}

		public void setMrcPhoneNo(String mrcPhoneNo) {
			this.mrcPhoneNo = mrcPhoneNo;
		}

		public String getMrcFirstNameReq() {
			return mrcFirstNameReq;
		}

		public void setMrcFirstNameReq(String mrcFirstNameReq) {
			this.mrcFirstNameReq = mrcFirstNameReq;
		}

		public String getMrcLastNameReq() {
			return mrcLastNameReq;
		}

		public void setMrcLastNameReq(String mrcLastNameReq) {
			this.mrcLastNameReq = mrcLastNameReq;
		}

		public String getMrcRoleReq() {
			return mrcRoleReq;
		}

		public void setMrcRoleReq(String mrcRoleReq) {
			this.mrcRoleReq = mrcRoleReq;
		}

		public String getMrcOfficeName() {
			return mrcOfficeName;
		}

		public void setMrcOfficeName(String mrcOfficeName) {
			this.mrcOfficeName = mrcOfficeName;
		}

		public String getMrcPhoneNoOffice() {
			return mrcPhoneNoOffice;
		}

		public void setMrcPhoneNoOffice(String mrcPhoneNoOffice) {
			this.mrcPhoneNoOffice = mrcPhoneNoOffice;
		}

		public String getMrcFirstNameRef() {
			return mrcFirstNameRef;
		}

		public void setMrcFirstNameRef(String mrcFirstNameRef) {
			this.mrcFirstNameRef = mrcFirstNameRef;
		}

		public String getMrcLastNameRef() {
			return mrcLastNameRef;
		}

		public void setMrcLastNameRef(String mrcLastNameRef) {
			this.mrcLastNameRef = mrcLastNameRef;
		}

		public String getMrcAddressOffice() {
			return mrcAddressOffice;
		}

		public void setMrcAddressOffice(String mrcAddressOffice) {
			this.mrcAddressOffice = mrcAddressOffice;
		}

		public String getMrcCityOffice() {
			return mrcCityOffice;
		}

		public void setMrcCityOffice(String mrcCityOffice) {
			this.mrcCityOffice = mrcCityOffice;
		}

		public String getMrcIban() {
			return mrcIban;
		}

		public void setMrcIban(String mrcIban) {
			this.mrcIban = mrcIban;
		}

		public String getMrcBank() {
			return mrcBank;
		}

		public void setMrcBank(String mrcBank) {
			this.mrcBank = mrcBank;
		}

		public String getAccessType() {
			return mrcAccessType;
		}

		public void setAccessType(String mrcAccessType) {
			this.mrcAccessType = mrcAccessType;
		}

		public String getPinCode() {
			return mrcPinCode;
		}

		public void setPinCode(String mrcPinCode) {
			this.mrcPinCode = mrcPinCode;
		}

		public String getMrcCash() {
			return mrcCash;
		}

		public void setMrcCash(String mrcCash) {
			this.mrcCash = mrcCash;
		}

		public String getMrcLongitude() {
			return mrcLongitude;
		}

		public void setMrcLongitude(String mrcLongitude) {
			this.mrcLongitude = mrcLongitude;
		}

		public String getMrcLatitude() {
			return mrcLatitude;
		}

		public void setMrcLatitude(String mrcLatitude) {
			this.mrcLatitude = mrcLatitude;
		}

		public byte[] getMrcImageProfile() {
			return mrcImageProfile;
		}

		public void setMrcImageProfile(byte[] mrcImageProfile) {
			this.mrcImageProfile = mrcImageProfile;
		}

		public String getMrcLinkMap() {
			return mrcLinkMap;
		}

		public void setMrcLinkMap(String mrcLinkMap) {
			this.mrcLinkMap = mrcLinkMap;
		}
		
		public String getMrcDistanza() {
			return mrcDistanza;
		}

		public void setMrcDistanza(String mrcDistanza) {
			this.mrcDistanza = mrcDistanza;
		}

		public String getMrcAccessType() {
			return mrcAccessType;
		}

		public void setMrcAccessType(String mrcAccessType) {
			this.mrcAccessType = mrcAccessType;
		}

		public String getMrcPinCode() {
			return mrcPinCode;
		}

		public void setMrcPinCode(String mrcPinCode) {
			this.mrcPinCode = mrcPinCode;
		}

		public String getMrcNotificationEnabled() {
			return mrcNotificationEnabled;
		}

		public void setMrcNotificationEnabled(String mrcNotificationEnabled) {
			this.mrcNotificationEnabled = mrcNotificationEnabled;
		}

		@Override
		public Object wrap(Object usr, boolean includePassword) {
			User user = (User)usr;
			MerchantDTO merchant = new MerchantDTO();
		    //user fields
			merchant.usrId=user.getUsrId()!=null?user.getUsrId().toString():"";
			merchant.usrBchAddress=user.getUsrBchAddress();
			merchant.usrPrivateKey=user.getUsrPrivateKey();
			merchant.usrEmail=user.getUsrEmail();
			if(includePassword) {
				merchant.usrPassword=user.getUsrPassword(); 
			}
			if(user.getUsrAccessType()!=null){
				merchant.mrcAccessType=user.getUsrAccessType();
				if(user.getUsrAccessType().equals("PIN") && user.getUsrPin()!=null) {
					merchant.mrcPinCode=user.getUsrPin().toString();
				}
			}
			// fields
			merchant.mrcAddress=user.getMerchant().getMrcAddress() ;
			merchant.mrcAddressOffice=user.getMerchant().getMrcAddressOffice() ;
			merchant.mrcBank=user.getMerchant().getMrcBank() ;
			String checked = "false";
			if(user.getMerchant().getMrcChecked()!=null && user.getMerchant().getMrcChecked()==1) {
				checked = "true";
			}
			merchant.setMrcChecked(checked) ;
			merchant.mrcCity=user.getMerchant().getMrcCity() ;
			merchant.mrcCityOffice=user.getMerchant().getMrcCityOffice() ;

			merchant.mrcCodiceFiscale=user.getMerchant().getMrcCodiceFiscale() ;

			merchant.mrcFirstNameRef=user.getMerchant().getMrcFirstNameRef() ;
			merchant.mrcFirstNameReq=user.getMerchant().getMrcFirstNameReq() ;
			merchant.mrcIban=user.getMerchant().getMrcIban() ;
			merchant.mrcLastNameRef=user.getMerchant().getMrcLastNameRef() ;
			merchant.mrcLastNameReq=user.getMerchant().getMrcLastNameReq() ;
			merchant.mrcOfficeName=user.getMerchant().getMrcOfficeName() ;
			merchant.mrcPartitaIva=user.getMerchant().getMrcPartitaIva() ;
			merchant.mrcPhoneNo=user.getMerchant().getMrcPhoneNo() ;
			merchant.mrcPhoneNoOffice=user.getMerchant().getMrcPhoneNoOffice() ;
			merchant.mrcProv=user.getMerchant().getMrcProv() ;
			merchant.mrcProvOffice=user.getMerchant().getMrcProvOffice() ;
			merchant.mrcRagioneSociale=user.getMerchant().getMrcRagioneSociale() ;
			merchant.mrcRoleReq=user.getMerchant().getMrcRoleReq() ;
			merchant.mrcZip=user.getMerchant().getMrcZip() ;
			merchant.mrcNotificationEnabled=user.getUsrNotificationEnable() ;
			if(user.getMerchant().getMrcCash()!=null) {
				merchant.mrcCash=user.getMerchant().getMrcCash() ;
			}
			if(user.getMerchant().getMrcLongitude()!=null) {
				merchant.mrcLongitude=user.getMerchant().getMrcLongitude().toString() ;
			}
			if(user.getMerchant().getMrcLatitude()!=null) {
				merchant.mrcLatitude=user.getMerchant().getMrcLatitude().toString() ;
			}
			if(user.getMerchant().getMrcImageProfile()!=null) {
				merchant.mrcImageProfile=user.getMerchant().getMrcImageProfile() ;
			}
			
			return merchant;
		}
	
		@Override
		public Object unwrap(Object merchantDTO) {
			MerchantDTO merchant = (MerchantDTO)merchantDTO;
			
			Merchant merch = new Merchant();
			User usr = new User();
			
			int checked=0;
			if(merchant.getMrcChecked()!=null && merchant.getMrcChecked().equalsIgnoreCase("true")) {
				checked=1;
			}
			merch.setMrcChecked(checked);
			
			merch.setMrcAddress(merchant.getMrcAddress());
			merch.setMrcAddressOffice(merchant.getMrcAddressOffice());
			merch.setMrcBank(merchant.getMrcBank());
			merch.setMrcCity(merchant.getMrcCity());
			merch.setMrcCityOffice(merchant.getMrcCityOffice());

			merch.setMrcCodiceFiscale(merchant.getMrcCodiceFiscale());

			merch.setMrcFirstNameRef(merchant.getMrcFirstNameRef());
			merch.setMrcFirstNameReq(merchant.getMrcFirstNameReq());
			merch.setMrcIban(merchant.getMrcIban());
			merch.setMrcLastNameRef(merchant.getMrcLastNameRef());
			merch.setMrcLastNameReq(merchant.getMrcLastNameReq());
			merch.setMrcOfficeName(merchant.getMrcOfficeName());
			merch.setMrcPartitaIva(merchant.getMrcPartitaIva());
			merch.setMrcPhoneNo(merchant.getMrcPhoneNo());
			merch.setMrcPhoneNoOffice(merchant.getMrcPhoneNoOffice());
			merch.setMrcProv(merchant.getMrcProv());
			merch.setMrcProvOffice(merchant.getMrcProvOffice());
			merch.setMrcRagioneSociale(merchant.getMrcRagioneSociale());
			merch.setMrcRoleReq(merchant.getMrcRoleReq());
			merch.setMrcZip(merchant.getMrcZip());
			
			if(merchant.getMrcCash()!=null) {
				merch.setMrcCash(merchant.getMrcCash());
			}
			if(merchant.getMrcLongitude()!=null) {
				merch.setMrcLongitude(new BigDecimal(merchant.getMrcLongitude()));
			}
			if(merchant.getMrcLatitude()!=null) {
				merch.setMrcLatitude(new BigDecimal(merchant.getMrcLatitude()));
			}
			if(merchant.getMrcImageProfile()!=null) {
				merch.setMrcImageProfile(merchant.getMrcImageProfile());
			}
			
			
			
			boolean isActive=false;
			if(merchant.getUsrActive()!= null && merchant.getUsrActive().equals("true")) {
				isActive=true;
			}
			usr.setUsrActive(isActive);
			usr.setUsrEmail(merchant.getUsrEmail());
			usr.setUsrPassword(merchant.getUsrPassword());
			usr.setUsrNotificationEnable(merchant.getMrcNotificationEnabled());
			usr.setMerchant(merch);
			return usr;
		}

		public String getMrcChecked() {
			return mrcChecked;
		}

		public void setMrcChecked(String mrcChecked) {
			this.mrcChecked = mrcChecked;
		}

		public String getMrcProvOffice() {
			return mrcProvOffice;
		}

		public void setMrcProvOffice(String mrcProvOffice) {
			this.mrcProvOffice = mrcProvOffice;
		}


}
