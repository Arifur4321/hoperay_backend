/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mediatica.vouchain.entities;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author Pietro
 */
@Entity
@Table(name = "tbregioni")
@NamedQueries({
    @NamedQuery(name = "Tbregioni.findAll", query = "SELECT t FROM Tbregioni t"),
    @NamedQuery(name = "Tbregioni.findByCodiceistatRegione", query = "SELECT t FROM Tbregioni t WHERE t.codiceistatRegione = :codiceistatRegione")})
public class Tbregioni implements Serializable {

    private static final long serialVersionUID = 1L;
    @Lob
    @Size(max = 65535)
    @Column(name = "nome_regione")
    private String nomeRegione;
    @Lob
    @Size(max = 65535)
    @Column(name = "capoluogo_regione")
    private String capoluogoRegione;
    @Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 3)
    @Column(name = "codiceistat_regione")
    private String codiceistatRegione;

    public Tbregioni() {
    }

    public Tbregioni(String codiceistatRegione) {
        this.codiceistatRegione = codiceistatRegione;
    }

    public String getNomeRegione() {
        return nomeRegione;
    }

    public void setNomeRegione(String nomeRegione) {
        this.nomeRegione = nomeRegione;
    }

    public String getCapoluogoRegione() {
        return capoluogoRegione;
    }

    public void setCapoluogoRegione(String capoluogoRegione) {
        this.capoluogoRegione = capoluogoRegione;
    }

    public String getCodiceistatRegione() {
        return codiceistatRegione;
    }

    public void setCodiceistatRegione(String codiceistatRegione) {
        this.codiceistatRegione = codiceistatRegione;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (codiceistatRegione != null ? codiceistatRegione.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Tbregioni)) {
            return false;
        }
        Tbregioni other = (Tbregioni) object;
        if ((this.codiceistatRegione == null && other.codiceistatRegione != null) || (this.codiceistatRegione != null && !this.codiceistatRegione.equals(other.codiceistatRegione))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.mediatica.vouchain.entities.Tbregioni[ codiceistatRegione=" + codiceistatRegione + " ]";
    }
    
}
