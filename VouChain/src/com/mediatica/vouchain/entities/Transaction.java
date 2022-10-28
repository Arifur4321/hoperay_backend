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
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
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
@Table(name = "transaction")
@NamedQueries({
    @NamedQuery(name = "Transaction.findAll", query = "SELECT t FROM Transaction t"),
    @NamedQuery(name = "Transaction.findByTrcId", query = "SELECT t FROM Transaction t WHERE t.trcId = :trcId"),
    @NamedQuery(name = "Transaction.findByTrcTxId", query = "SELECT t FROM Transaction t WHERE t.trcTxId = :trcTxId"),
    @NamedQuery(name = "Transaction.findByTrcType", query = "SELECT t FROM Transaction t WHERE t.trcType = :trcType"),
    @NamedQuery(name = "Transaction.findByTrcDate", query = "SELECT t FROM Transaction t WHERE t.trcDate = :trcDate"),
    @NamedQuery(name = "Transaction.findByTrcState", query = "SELECT t FROM Transaction t WHERE t.trcState = :trcState"),
    @NamedQuery(name = "Transaction.findByTrcIban", query = "SELECT t FROM Transaction t WHERE t.trcIban = :trcIban"),
    @NamedQuery(name = "Transaction.findByTrcPayed", query = "SELECT t FROM Transaction t WHERE t.trcPayed = :trcPayed"),
    @NamedQuery(name = "Transaction.findByTrcCancDate", query = "SELECT t FROM Transaction t WHERE t.trcCancDate = :trcCancDate")})
public class Transaction implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "trc_id")
    private Integer trcId;
    @Size(max = 150)
    @Column(name = "trc_tx_id")
    private String trcTxId;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 5)
    @Column(name = "trc_type")
    private String trcType;
    @Column(name = "trc_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date trcDate;
    @Column(name = "trc_state")
    private Boolean trcState;
    @Size(max = 45)
    @Column(name = "trc_iban")
    private String trcIban;
    
    @Column(name = "trc_accountholder")
    private String trcAccountHolder;
    
    @Column(name = "trc_payed")
    private Boolean trcPayed;
    @Column(name = "trc_mail_sent")
    private Boolean trcMailSent;
    @Column(name = "trc_canc_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date trcCancDate;
    @Lob
    @Column(name = "trc_invoice")
    private byte[] trcInvoice;
    
    @Column(name = "qr_value")
    private String trcQrValue;
    @Column(name = "qr_causale")
    private String trcQrCausale;
    
    @Lob
    @Column(name = "trc_invoice_xml")
    private byte[] trcInvoiceXml;
    
	@JoinColumn(name = "usr_id_da", referencedColumnName = "usr_id")
    @ManyToOne
    private User usrIdDa;
    @JoinColumn(name = "usr_id_a", referencedColumnName = "usr_id")
    @ManyToOne
    private User usrIdA;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "transaction")
    private Collection<TransactionDetail> transactionDetailCollection;

    public Transaction() {
    }

    public Transaction(Integer trcId) {
        this.trcId = trcId;
    }

    public Transaction(Integer trcId, String trcType) {
        this.trcId = trcId;
        this.trcType = trcType;
    }

    public Integer getTrcId() {
        return trcId;
    }

    public void setTrcId(Integer trcId) {
        this.trcId = trcId;
    }

    public String getTrcTxId() {
        return trcTxId;
    }

    public void setTrcTxId(String trcTxId) {
        this.trcTxId = trcTxId;
    }

    public String getTrcType() {
        return trcType;
    }

    public void setTrcType(String trcType) {
        this.trcType = trcType;
    }

    public Date getTrcDate() {
        return trcDate;
    }

    public void setTrcDate(Date trcDate) {
        this.trcDate = trcDate;
    }

    public Boolean getTrcState() {
        return trcState;
    }

    public void setTrcState(Boolean trcState) {
        this.trcState = trcState;
    }

    public String getTrcIban() {
        return trcIban;
    }

    public void setTrcIban(String trcIban) {
        this.trcIban = trcIban;
    }

    public Boolean getTrcPayed() {
        return trcPayed;
    }

    public void setTrcPayed(Boolean trcPayed) {
        this.trcPayed = trcPayed;
    }

    public Date getTrcCancDate() {
        return trcCancDate;
    }

    public void setTrcCancDate(Date trcCancDate) {
        this.trcCancDate = trcCancDate;
    }

    public byte[] getTrcInvoice() {
        return trcInvoice;
    }

    public void setTrcInvoice(byte[] trcInvoice) {
        this.trcInvoice = trcInvoice;
    }

    public User getUsrIdDa() {
        return usrIdDa;
    }

    public void setUsrIdDa(User usrIdDa) {
        this.usrIdDa = usrIdDa;
    }

    public User getUsrIdA() {
        return usrIdA;
    }

    
    
    public Boolean getTrcMailSent() {
		return trcMailSent;
	}

	public void setTrcMailSent(Boolean trcMailSent) {
		this.trcMailSent = trcMailSent;
	}

	public void setUsrIdA(User usrIdA) {
        this.usrIdA = usrIdA;
    }

    public Collection<TransactionDetail> getTransactionDetailCollection() {
        return transactionDetailCollection;
    }

    public void setTransactionDetailCollection(Collection<TransactionDetail> transactionDetailCollection) {
        this.transactionDetailCollection = transactionDetailCollection;
    }
    
    

    public String getTrcAccountHolder() {
		return trcAccountHolder;
	}

	public void setTrcAccountHolder(String trcAccountHolder) {
		this.trcAccountHolder = trcAccountHolder;
	}
	
    public byte[] getTrcInvoiceXml() {
		return trcInvoiceXml;
	}

	public void setTrcInvoiceXml(byte[] trcInvoiceXml) {
		this.trcInvoiceXml = trcInvoiceXml;
	}

	public String getTrcQrValue() {
		return trcQrValue;
	}

	public void setTrcQrValue(String trcQrValue) {
		this.trcQrValue = trcQrValue;
	}

	public String getTrcQrCausale() {
		return trcQrCausale;
	}

	public void setTrcQrCausale(String trcQrCausale) {
		this.trcQrCausale = trcQrCausale;
	}

	@Override
    public int hashCode() {
        int hash = 0;
        hash += (trcId != null ? trcId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Transaction)) {
            return false;
        }
        Transaction other = (Transaction) object;
        if ((this.trcId == null && other.trcId != null) || (this.trcId != null && !this.trcId.equals(other.trcId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.mediatica.vouchain.entities.Transaction[ trcId=" + trcId + " ]";
    }
    
}
