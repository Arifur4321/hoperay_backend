/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mediatica.vouchain.entities;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author Pietro
 */
@Embeddable
public class TransactionDetailPK implements Serializable {

    @Basic(optional = false)
    @NotNull
    @Column(name = "trc_id")
    private int trcId;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "vch_name")
    private String vchName;

    public TransactionDetailPK() {
    }

    public TransactionDetailPK(int trcId, String vchName) {
        this.trcId = trcId;
        this.vchName = vchName;
    }

    public int getTrcId() {
        return trcId;
    }

    public void setTrcId(int trcId) {
        this.trcId = trcId;
    }

    public String getVchName() {
        return vchName;
    }

    public void setVchName(String vchName) {
        this.vchName = vchName;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (int) trcId;
        hash += (vchName != null ? vchName.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof TransactionDetailPK)) {
            return false;
        }
        TransactionDetailPK other = (TransactionDetailPK) object;
        if (this.trcId != other.trcId) {
            return false;
        }
        if ((this.vchName == null && other.vchName != null) || (this.vchName != null && !this.vchName.equals(other.vchName))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.mediatica.vouchain.entities.TransactionDetailPK[ trcId=" + trcId + ", vchName=" + vchName + " ]";
    }
    
}
