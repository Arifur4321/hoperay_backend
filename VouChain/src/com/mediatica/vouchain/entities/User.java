/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mediatica.vouchain.entities;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author Pietro
 */
@Entity
@Table(name = "user")
@NamedQueries({
    @NamedQuery(name = "User.findAll", query = "SELECT u FROM User u"),
    @NamedQuery(name = "User.findByUsrId", query = "SELECT u FROM User u WHERE u.usrId = :usrId"),
    @NamedQuery(name = "User.findByUsrEmail", query = "SELECT u FROM User u WHERE u.usrEmail = :usrEmail"),
    @NamedQuery(name = "User.findByUsrSalt", query = "SELECT u FROM User u WHERE u.usrSalt = :usrSalt"),
    @NamedQuery(name = "User.findByUsrPassword", query = "SELECT u FROM User u WHERE u.usrPassword = :usrPassword"),
    @NamedQuery(name = "User.findByUsrBchAddress", query = "SELECT u FROM User u WHERE u.usrBchAddress = :usrBchAddress"),
    @NamedQuery(name = "User.findByUsrPrivateKey", query = "SELECT u FROM User u WHERE u.usrPrivateKey = :usrPrivateKey"),
    @NamedQuery(name = "User.findByUsrActive", query = "SELECT u FROM User u WHERE u.usrActive = :usrActive")})
public class User implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "usr_id")
    private Integer usrId;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "usr_email")
    private String usrEmail;
    @Size(max = 45)
    @Column(name = "usr_salt")
    private String usrSalt;
    @Size(max = 200)
    @Column(name = "usr_password")
    private String usrPassword;
    @Size(max = 34)
    @Column(name = "usr_bch_address")
    private String usrBchAddress;
    @Size(max = 45)
    @Column(name = "usr_recover_email_code")
    private String usrRecoverEmailCode;
    @Column(name = "usr_session")
    private String userSession;
    @Column(name = "last_invocation_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date lastInvocationDate;
	@Size(max = 150)
    @Column(name = "usr_private_key")
    private String usrPrivateKey;
    @Column(name = "usr_active")
    private Boolean usrActive;
    @Column(name = "usr_access_type")
    private String usrAccessType;
    @Column(name = "usr_pin")
    private String usrPin;
    @Column(name = "usr_notification_enable")
    private String usrNotificationEnable;
    @OneToOne(cascade = CascadeType.ALL, mappedBy = "user")
    private Merchant merchant;
    @OneToOne(cascade = CascadeType.ALL, mappedBy = "user")
    private Company company;
    @OneToOne(cascade = CascadeType.ALL, mappedBy = "user")
    private Employee employee;
    @OneToMany(mappedBy = "usrIdDa")
    private Collection<Transaction> transactionCollection;
    @OneToMany(mappedBy = "usrIdA")
    private Collection<Transaction> transactionCollection1;

    public User() {
    }

    public User(Integer usrId) {
        this.usrId = usrId;
    }

    public User(Integer usrId, String usrEmail) {
        this.usrId = usrId;
        this.usrEmail = usrEmail;
    }

    public Integer getUsrId() {
        return usrId;
    }

    public void setUsrId(Integer usrId) {
        this.usrId = usrId;
    }

    public String getUsrEmail() {
        return usrEmail;
    }

    public void setUsrEmail(String usrEmail) {
        this.usrEmail = usrEmail;
    }

    public String getUsrSalt() {
        return usrSalt;
    }

    public void setUsrSalt(String usrSalt) {
        this.usrSalt = usrSalt;
    }

    public String getUsrPassword() {
        return usrPassword;
    }

    public void setUsrPassword(String usrPassword) {
        this.usrPassword = usrPassword;
    }

    public String getUsrBchAddress() {
        return usrBchAddress;
    }

    public void setUsrBchAddress(String usrBchAddress) {
        this.usrBchAddress = usrBchAddress;
    }
    
    public String getUserSession() {
        return userSession;
    }

    public void setUserSession(String userSession) {
        this.userSession = userSession;
    }

    public Date getLastInvocationDate() {
		return lastInvocationDate;
	}

	public void setLastInvocationDate(Date lastInvocationDate) {
		this.lastInvocationDate = lastInvocationDate;
	}

	public String getUsrPrivateKey() {
        return usrPrivateKey;
    }

    public void setUsrPrivateKey(String usrPrivateKey) {
        this.usrPrivateKey = usrPrivateKey;
    }

    public Boolean getUsrActive() {
        return usrActive;
    }

    public void setUsrActive(Boolean usrActive) {
        this.usrActive = usrActive;
    }

    public Merchant getMerchant() {
        return merchant;
    }

    public void setMerchant(Merchant merchant) {
        this.merchant = merchant;
    }

    public Company getCompany() {
        return company;
    }

    public void setCompany(Company company) {
        this.company = company;
    }

    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    public Collection<Transaction> getTransactionCollection() {
        return transactionCollection;
    }

    public void setTransactionCollection(Collection<Transaction> transactionCollection) {
        this.transactionCollection = transactionCollection;
    }

    public Collection<Transaction> getTransactionCollection1() {
        return transactionCollection1;
    }

    public void setTransactionCollection1(Collection<Transaction> transactionCollection1) {
        this.transactionCollection1 = transactionCollection1;
    }
    
    public String getUsrRecoverEmailCode() {
		return usrRecoverEmailCode;
	}

	public void setUsrRecoverEmailCode(String usrRecoverEmailCode) {
		this.usrRecoverEmailCode = usrRecoverEmailCode;
	}
	
    public String getUsrAccessType() {
		return usrAccessType;
	}

	public void setUsrAccessType(String usrAccessType) {
		this.usrAccessType = usrAccessType;
	}

	public String getUsrPin() {
		return usrPin;
	}

	public void setUsrPin(String usrPin) {
		this.usrPin = usrPin;
	}

	public String getUsrNotificationEnable() {
		return usrNotificationEnable;
	}

	public void setUsrNotificationEnable(String usrNotificationEnable) {
		this.usrNotificationEnable = usrNotificationEnable;
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
        if (!(object instanceof User)) {
            return false;
        }
        User other = (User) object;
        if ((this.usrId == null && other.usrId != null) || (this.usrId != null && !this.usrId.equals(other.usrId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.mediatica.vouchain.entities.User[ usrId=" + usrId + " ]";
    }
    
}
