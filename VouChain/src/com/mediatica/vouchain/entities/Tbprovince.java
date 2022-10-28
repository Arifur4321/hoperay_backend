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
@Table(name = "tbprovince")
@NamedQueries({
    @NamedQuery(name = "Tbprovince.findAll", query = "SELECT t FROM Tbprovince t order BY t.nomeProvincia "),
    @NamedQuery(name = "Tbprovince.findBySiglaProvincia", query = "SELECT t FROM Tbprovince t WHERE t.siglaProvincia = :siglaProvincia"),
    @NamedQuery(name = "Tbprovince.findByCodiceistatregioneProvincia", query = "SELECT t FROM Tbprovince t WHERE t.codiceistatregioneProvincia = :codiceistatregioneProvincia")})
public class Tbprovince implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 2)
    @Column(name = "sigla_provincia")
    private String siglaProvincia;
    @Lob
    @Size(max = 65535)
    @Column(name = "nome_provincia")
    private String nomeProvincia;
    @Lob
    @Size(max = 65535)
    @Column(name = "capoluogo_provincia")
    private String capoluogoProvincia;
    @Size(max = 2)
    @Column(name = "codiceistatregione_provincia")
    private String codiceistatregioneProvincia;
    @Lob
    @Size(max = 65535)
    @Column(name = "codiceistat_provincia")
    private String codiceistatProvincia;

    public Tbprovince() {
    }

    public Tbprovince(String siglaProvincia) {
        this.siglaProvincia = siglaProvincia;
    }

    public String getSiglaProvincia() {
        return siglaProvincia;
    }

    public void setSiglaProvincia(String siglaProvincia) {
        this.siglaProvincia = siglaProvincia;
    }

    public String getNomeProvincia() {
        return nomeProvincia;
    }

    public void setNomeProvincia(String nomeProvincia) {
        this.nomeProvincia = nomeProvincia;
    }

    public String getCapoluogoProvincia() {
        return capoluogoProvincia;
    }

    public void setCapoluogoProvincia(String capoluogoProvincia) {
        this.capoluogoProvincia = capoluogoProvincia;
    }

    public String getCodiceistatregioneProvincia() {
        return codiceistatregioneProvincia;
    }

    public void setCodiceistatregioneProvincia(String codiceistatregioneProvincia) {
        this.codiceistatregioneProvincia = codiceistatregioneProvincia;
    }

    public String getCodiceistatProvincia() {
        return codiceistatProvincia;
    }

    public void setCodiceistatProvincia(String codiceistatProvincia) {
        this.codiceistatProvincia = codiceistatProvincia;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (siglaProvincia != null ? siglaProvincia.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Tbprovince)) {
            return false;
        }
        Tbprovince other = (Tbprovince) object;
        if ((this.siglaProvincia == null && other.siglaProvincia != null) || (this.siglaProvincia != null && !this.siglaProvincia.equals(other.siglaProvincia))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.mediatica.vouchain.entities.Tbprovince[ siglaProvincia=" + siglaProvincia + " ]";
    }
    
}
