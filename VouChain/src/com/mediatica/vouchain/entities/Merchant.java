/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mediatica.vouchain.entities;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author Pietro
 */
@Entity
@Table(name = "merchant")
@NamedQueries({
    @NamedQuery(name = "Merchant.findAll", query = "SELECT m FROM Merchant m"),
    @NamedQuery(name = "Merchant.findByUsrId", query = "SELECT m FROM Merchant m WHERE m.usrId = :usrId"),
    @NamedQuery(name = "Merchant.findByMrcRagioneSociale", query = "SELECT m FROM Merchant m WHERE m.mrcRagioneSociale = :mrcRagioneSociale"),
    @NamedQuery(name = "Merchant.findByMrcPartitaIva", query = "SELECT m FROM Merchant m WHERE m.mrcPartitaIva = :mrcPartitaIva"),
    @NamedQuery(name = "Merchant.findByMrcCodiceFiscale", query = "SELECT m FROM Merchant m WHERE m.mrcCodiceFiscale = :mrcCodiceFiscale"),
    @NamedQuery(name = "Merchant.findByMrcAddress", query = "SELECT m FROM Merchant m WHERE m.mrcAddress = :mrcAddress"),
    @NamedQuery(name = "Merchant.findByMrcCity", query = "SELECT m FROM Merchant m WHERE m.mrcCity = :mrcCity"),
    @NamedQuery(name = "Merchant.findByMrcProv", query = "SELECT m FROM Merchant m WHERE m.mrcProv = :mrcProv"),
    @NamedQuery(name = "Merchant.findByMrcZip", query = "SELECT m FROM Merchant m WHERE m.mrcZip = :mrcZip"),
    @NamedQuery(name = "Merchant.findByMrcPhoneNo", query = "SELECT m FROM Merchant m WHERE m.mrcPhoneNo = :mrcPhoneNo"),
    @NamedQuery(name = "Merchant.findByMrcFirstNameReq", query = "SELECT m FROM Merchant m WHERE m.mrcFirstNameReq = :mrcFirstNameReq"),
    @NamedQuery(name = "Merchant.findByMrcLastNameReq", query = "SELECT m FROM Merchant m WHERE m.mrcLastNameReq = :mrcLastNameReq"),
    @NamedQuery(name = "Merchant.findByMrcRoleReq", query = "SELECT m FROM Merchant m WHERE m.mrcRoleReq = :mrcRoleReq"),
    @NamedQuery(name = "Merchant.findByMrcOfficeName", query = "SELECT m FROM Merchant m WHERE m.mrcOfficeName = :mrcOfficeName"),
    @NamedQuery(name = "Merchant.findByMrcPhoneNoOffice", query = "SELECT m FROM Merchant m WHERE m.mrcPhoneNoOffice = :mrcPhoneNoOffice"),
    @NamedQuery(name = "Merchant.findByMrcFirstNameRef", query = "SELECT m FROM Merchant m WHERE m.mrcFirstNameRef = :mrcFirstNameRef"),
    @NamedQuery(name = "Merchant.findByMrcLastNameRef", query = "SELECT m FROM Merchant m WHERE m.mrcLastNameRef = :mrcLastNameRef"),
    @NamedQuery(name = "Merchant.findByMrcAddressOffice", query = "SELECT m FROM Merchant m WHERE m.mrcAddressOffice = :mrcAddressOffice"),
    @NamedQuery(name = "Merchant.findByMrcCityOffice", query = "SELECT m FROM Merchant m WHERE m.mrcCityOffice = :mrcCityOffice"),
    @NamedQuery(name = "Merchant.findByMrcIban", query = "SELECT m FROM Merchant m WHERE m.mrcIban = :mrcIban"),
    @NamedQuery(name = "Merchant.findByMrcBank", query = "SELECT m FROM Merchant m WHERE m.mrcBank = :mrcBank"),
    @NamedQuery(name = "Merchant.findByMrcChecked", query = "SELECT m FROM Merchant m WHERE m.mrcChecked = :mrcChecked")})
public class Merchant implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "usr_id")
    private Integer usrId;
    @Size(max = 45)
    @Column(name = "mrc_ragione_sociale")
    private String mrcRagioneSociale;
    @Size(max = 45)
    @Column(name = "mrc_partita_iva")
    private String mrcPartitaIva;
    @Size(max = 45)
    @Column(name = "mrc_codice_fiscale")
    private String mrcCodiceFiscale;
    @Size(max = 45)
    @Column(name = "mrc_address")
    private String mrcAddress;
    @Size(max = 45)
    @Column(name = "mrc_city")
    private String mrcCity;
    @Size(max = 2)
    @Column(name = "mrc_prov")
    private String mrcProv;
    @Size(max = 5)
    @Column(name = "mrc_zip")
    private String mrcZip;
    @Size(max = 45)
    @Column(name = "mrc_phone_no")
    private String mrcPhoneNo;
    @Size(max = 45)
    @Column(name = "mrc_first_name_req")
    private String mrcFirstNameReq;
    @Size(max = 45)
    @Column(name = "mrc_last_name_req")
    private String mrcLastNameReq;
    @Size(max = 45)
    @Column(name = "mrc_role_req")
    private String mrcRoleReq;
    @Size(max = 45)
    @Column(name = "mrc_office_name")
    private String mrcOfficeName;
    @Size(max = 45)
    @Column(name = "mrc_phone_no_office")
    private String mrcPhoneNoOffice;
    @Size(max = 45)
    @Column(name = "mrc_first_name_ref")
    private String mrcFirstNameRef;
    @Size(max = 45)
    @Column(name = "mrc_last_name_ref")
    private String mrcLastNameRef;
    @Size(max = 45)
    @Column(name = "mrc_address_office")
    private String mrcAddressOffice;
    @Size(max = 45)
    @Column(name = "mrc_city_office")
    private String mrcCityOffice;
    @Size(max = 2)
    @Column(name = "mrc_prov_office")
    private String mrcProvOffice;
    @Size(max = 45)
    @Column(name = "mrc_iban")
    private String mrcIban;
    @Size(max = 45)
    @Column(name = "mrc_bank")
    private String mrcBank;
    @Size(max = 5)
    @Column(name = "mrc_cash")
    private String mrcCash;
    @Column(name = "mrc_checked")
    private Integer mrcChecked;
    @Column(name = "mrc_longitude")
    private BigDecimal mrcLongitude;
    @Column(name = "mrc_latitude")
    private BigDecimal mrcLatitude;
    @Column(name = "mrc_image_profile")
    private byte[] mrcImageProfile;
    
    @JoinColumn(name = "usr_id", referencedColumnName = "usr_id", insertable = false, updatable = false)
    @OneToOne(optional = false)
    private User user;

    public Merchant() {
    }

    public Merchant(Integer usrId) {
        this.usrId = usrId;
    }

    public Integer getUsrId() {
        return usrId;
    }

    public void setUsrId(Integer usrId) {
        this.usrId = usrId;
    }

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



    public Integer getMrcChecked() {
        return mrcChecked;
    }

    public void setMrcChecked(Integer mrcChecked) {
        this.mrcChecked = mrcChecked;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
    
  
    public String getMrcProvOffice() {
		return mrcProvOffice;
	}

	public void setMrcProvOffice(String mrcProvOffice) {
		this.mrcProvOffice = mrcProvOffice;
	}

	public String getMrcCash() {
		return mrcCash;
	}

	public void setMrcCash(String mrcCash) {
		this.mrcCash = mrcCash;
	}

	public BigDecimal getMrcLongitude() {
		return mrcLongitude;
	}

	public void setMrcLongitude(BigDecimal mrcLongitude) {
		this.mrcLongitude = mrcLongitude;
	}

	public BigDecimal getMrcLatitude() {
		return mrcLatitude;
	}

	public void setMrcLatitude(BigDecimal mrcLatitude) {
		this.mrcLatitude = mrcLatitude;
	}

	public byte[] getMrcImageProfile() {
		return mrcImageProfile;
	}

	public void setMrcImageProfile(byte[] mrcImageProfile) {
		this.mrcImageProfile = mrcImageProfile;
	}

	@Override
    public int hashCode() {
        int hash = 0;
        hash += (usrId != null ? usrId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Merchant)) {
            return false;
        }
        Merchant other = (Merchant) object;
        if ((this.usrId == null && other.usrId != null) || (this.usrId != null && !this.usrId.equals(other.usrId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.mediatica.vouchain.entities.Merchant[ usrId=" + usrId + " ]";
    }
    
}
