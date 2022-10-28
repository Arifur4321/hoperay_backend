package com.mediatica.vouchain.entities;



import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 *
 * @author Pietro
 */
@Entity
@Table(name = "invoice")
@NamedQueries({
    @NamedQuery(name = "Invoice.findAll", query = "SELECT i FROM Invoice i"),
    @NamedQuery(name = "Invoice.findByIdinvoice", query = "SELECT i FROM Invoice i WHERE i.idinvoice = :idinvoice")})
public class Invoice implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "idinvoice")
    private Integer idinvoice;

    public Invoice() {
    }

    public Invoice(Integer idinvoice) {
        this.idinvoice = idinvoice;
    }

    public Integer getIdinvoice() {
        return idinvoice;
    }

    public void setIdinvoice(Integer idinvoice) {
        this.idinvoice = idinvoice;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idinvoice != null ? idinvoice.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Invoice)) {
            return false;
        }
        Invoice other = (Invoice) object;
        if ((this.idinvoice == null && other.idinvoice != null) || (this.idinvoice != null && !this.idinvoice.equals(other.idinvoice))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.mediatica.vouchain.entities.Invoice[ idinvoice=" + idinvoice + " ]";
    }
    
}
