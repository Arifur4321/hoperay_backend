/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mediatica.vouchain.entities;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Collection;
import java.util.Date;
import java.util.Locale;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.mediatica.vouchain.utilities.Utils;

/**
 *
 * @author Pietro
 */
@Entity
@Table(name = "voucher")
@NamedQueries({
    @NamedQuery(name = "Voucher.findAll", query = "SELECT v FROM Voucher v"),
    @NamedQuery(name = "Voucher.findByVchName", query = "SELECT v FROM Voucher v WHERE v.vchName = :vchName"),
    @NamedQuery(name = "Voucher.findByVchCreationDate", query = "SELECT v FROM Voucher v WHERE v.vchCreationDate = :vchCreationDate"),
    @NamedQuery(name = "Voucher.findByVchEndDate", query = "SELECT v FROM Voucher v WHERE v.vchEndDate = :vchEndDate"),
    @NamedQuery(name = "Voucher.findByVchValue", query = "SELECT v FROM Voucher v WHERE v.vchValue = :vchValue")})
public class Voucher implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "vch_name")
    private String vchName;
    @Column(name = "vch_creation_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date vchCreationDate;
    @Column(name = "vch_end_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date vchEndDate;
    @Column(name = "vch_value")
    private BigDecimal vchValue;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "voucher")
    private Collection<TransactionDetail> transactionDetailCollection;
    
    @Column(name = "vch_state")
    private Boolean vchState;

    private String valueInString;


	public Boolean getVchState() {
		return vchState;
	}

	public void setVchState(Boolean vchState) {
		this.vchState = vchState;
	}

	public Voucher() {
    }

    public Voucher(String vchName) {
        this.vchName = vchName;
    }

    public String getVchName() {
        return vchName;
    }

    public void setVchName(String vchName) {
        this.vchName = vchName;
    }

    public Date getVchCreationDate() {
        return vchCreationDate;
    }

    public void setVchCreationDate(Date vchCreationDate) {
        this.vchCreationDate = vchCreationDate;
    }

    public Date getVchEndDate() {
        return vchEndDate;
    }

    public void setVchEndDate(Date vchEndDate) {
        this.vchEndDate = vchEndDate;
    }



    public Collection<TransactionDetail> getTransactionDetailCollection() {
        return transactionDetailCollection;
    }

    public void setTransactionDetailCollection(Collection<TransactionDetail> transactionDetailCollection) {
        this.transactionDetailCollection = transactionDetailCollection;
    }
    
    public BigDecimal getVchValue() {
    	if(vchValue!=null) {
        	vchValue = vchValue.setScale(2, RoundingMode.CEILING);
    	}
 
		return vchValue;
	}
    


	public void setVchValue(BigDecimal vchValue) {
		this.vchValue = vchValue;
	}

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (vchName != null ? vchName.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Voucher)) {
            return false;
        }
        Voucher other = (Voucher) object;
        if ((this.vchName == null && other.vchName != null) || (this.vchName != null && !this.vchName.equals(other.vchName))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.mediatica.vouchain.entities.Voucher[ vchName=" + vchName + " ]";
    }

	public String getValueInString() {
    	if(vchValue!=null) {
        	vchValue = vchValue.setScale(2, RoundingMode.CEILING);
    		valueInString=Utils.formatNumeberTotwoDecimalDigitsAndThousandSeparator(vchValue.doubleValue());
    	}
    	return valueInString;
	}

	public void setValueInString(String valueInString) {
		this.valueInString = valueInString;
	}
    
}
