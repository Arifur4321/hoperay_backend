/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mediatica.vouchain.entities;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 *
 * @author Pietro
 */
@Entity
@Table(name = "transaction_detail")
@NamedQueries({
    @NamedQuery(name = "TransactionDetail.findAll", query = "SELECT t FROM TransactionDetail t"),
    @NamedQuery(name = "TransactionDetail.findByTrcId", query = "SELECT t FROM TransactionDetail t WHERE t.transactionDetailPK.trcId = :trcId"),
    @NamedQuery(name = "TransactionDetail.findByVchName", query = "SELECT t FROM TransactionDetail t WHERE t.transactionDetailPK.vchName = :vchName"),
    @NamedQuery(name = "TransactionDetail.findByTrdQuantity", query = "SELECT t FROM TransactionDetail t WHERE t.trdQuantity = :trdQuantity")})
public class TransactionDetail implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected TransactionDetailPK transactionDetailPK;
    @Column(name = "trd_quantity")
    private Long trdQuantity;
    @JoinColumn(name = "trc_id", referencedColumnName = "trc_id", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Transaction transaction;
    @JoinColumn(name = "vch_name", referencedColumnName = "vch_name", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Voucher voucher;

    public TransactionDetail() {
    }

    public TransactionDetail(TransactionDetailPK transactionDetailPK) {
        this.transactionDetailPK = transactionDetailPK;
    }

    public TransactionDetail(int trcId, String vchName) {
        this.transactionDetailPK = new TransactionDetailPK(trcId, vchName);
    }

    public TransactionDetailPK getTransactionDetailPK() {
        return transactionDetailPK;
    }

    public void setTransactionDetailPK(TransactionDetailPK transactionDetailPK) {
        this.transactionDetailPK = transactionDetailPK;
    }

    public Long getTrdQuantity() {
        return trdQuantity;
    }

    public void setTrdQuantity(Long trdQuantity) {
        this.trdQuantity = trdQuantity;
    }

    public Transaction getTransaction() {
        return transaction;
    }

    public void setTransaction(Transaction transaction) {
        this.transaction = transaction;
    }

    public Voucher getVoucher() {
        return voucher;
    }

    public void setVoucher(Voucher voucher) {
        this.voucher = voucher;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (transactionDetailPK != null ? transactionDetailPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof TransactionDetail)) {
            return false;
        }
        TransactionDetail other = (TransactionDetail) object;
        if ((this.transactionDetailPK == null && other.transactionDetailPK != null) || (this.transactionDetailPK != null && !this.transactionDetailPK.equals(other.transactionDetailPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.mediatica.vouchain.entities.TransactionDetail[ transactionDetailPK=" + transactionDetailPK + " ]";
    }
    
}
