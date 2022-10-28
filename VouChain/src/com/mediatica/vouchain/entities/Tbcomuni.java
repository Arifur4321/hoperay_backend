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
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
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
@Table(name = "tbcomuni")
@NamedQueries({
    @NamedQuery(name = "Tbcomuni.findAll", query = "SELECT t FROM Tbcomuni t ORDER BY t.nomeComune"),
    @NamedQuery(name = "Tbcomuni.findById", query = "SELECT t FROM Tbcomuni t WHERE t.id = :id"),
    @NamedQuery(name = "Tbcomuni.findBySiglaprovinciaComune", query = "SELECT t FROM Tbcomuni t WHERE t.siglaprovinciaComune = :siglaprovinciaComune ORDER BY t.nomeComune"),
    @NamedQuery(name = "Tbcomuni.findByCodiceCatastale", query = "SELECT t FROM Tbcomuni t WHERE t.codiceCatastale = :codiceCatastale"),
    @NamedQuery(name = "Tbcomuni.findByCodiceIstat", query = "SELECT t FROM Tbcomuni t WHERE t.codiceIstat = :codiceIstat")})
public class Tbcomuni implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Lob
    @Size(max = 65535)
    @Column(name = "nome_comune")
    private String nomeComune;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 2)
    @Column(name = "siglaprovincia_comune")
    private String siglaprovinciaComune;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 4)
    @Column(name = "codice_catastale")
    private String codiceCatastale;
    @Basic(optional = false)
    @NotNull
    @Column(name = "codice_istat")
    private int codiceIstat;

    public Tbcomuni() {
    }

    public Tbcomuni(Integer id) {
        this.id = id;
    }

    public Tbcomuni(Integer id, String siglaprovinciaComune, String codiceCatastale, int codiceIstat) {
        this.id = id;
        this.siglaprovinciaComune = siglaprovinciaComune;
        this.codiceCatastale = codiceCatastale;
        this.codiceIstat = codiceIstat;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNomeComune() {
        return nomeComune;
    }

    public void setNomeComune(String nomeComune) {
        this.nomeComune = nomeComune;
    }

    public String getSiglaprovinciaComune() {
        return siglaprovinciaComune;
    }

    public void setSiglaprovinciaComune(String siglaprovinciaComune) {
        this.siglaprovinciaComune = siglaprovinciaComune;
    }

    public String getCodiceCatastale() {
        return codiceCatastale;
    }

    public void setCodiceCatastale(String codiceCatastale) {
        this.codiceCatastale = codiceCatastale;
    }

    public int getCodiceIstat() {
        return codiceIstat;
    }

    public void setCodiceIstat(int codiceIstat) {
        this.codiceIstat = codiceIstat;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Tbcomuni)) {
            return false;
        }
        Tbcomuni other = (Tbcomuni) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.mediatica.vouchain.entities.Tbcomuni[ id=" + id + " ]";
    }
    
}
