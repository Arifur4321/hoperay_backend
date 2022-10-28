/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mediatica.vouchain.entities;
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


import java.io.Serializable;
import java.util.Collection;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author Pietro
 */
@Entity
@Table(name = "company")
@NamedQueries({
    @NamedQuery(name = "Company.findAll", query = "SELECT c FROM Company c"),
    @NamedQuery(name = "Company.findByUsrId", query = "SELECT c FROM Company c WHERE c.usrId = :usrId"),
    @NamedQuery(name = "Company.findByCpyRagioneSociale", query = "SELECT c FROM Company c WHERE c.cpyRagioneSociale = :cpyRagioneSociale"),
    @NamedQuery(name = "Company.findByCpyAddress", query = "SELECT c FROM Company c WHERE c.cpyAddress = :cpyAddress"),
    @NamedQuery(name = "Company.findByCpyCity", query = "SELECT c FROM Company c WHERE c.cpyCity = :cpyCity"),
    @NamedQuery(name = "Company.findByCpyProv", query = "SELECT c FROM Company c WHERE c.cpyProv = :cpyProv"),
    @NamedQuery(name = "Company.findByCpyZip", query = "SELECT c FROM Company c WHERE c.cpyZip = :cpyZip"),
    @NamedQuery(name = "Company.findByCpyPec", query = "SELECT c FROM Company c WHERE c.cpyPec = :cpyPec"),
    @NamedQuery(name = "Company.findByCpyCodiceFiscale", query = "SELECT c FROM Company c WHERE c.cpyCodiceFiscale = :cpyCodiceFiscale"),
    @NamedQuery(name = "Company.findByCpyPartitaIva", query = "SELECT c FROM Company c WHERE c.cpyPartitaIva = :cpyPartitaIva"),
    @NamedQuery(name = "Company.findByCpyContractChecked", query = "SELECT c FROM Company c WHERE c.cpyContractChecked = :cpyContractChecked"),
    @NamedQuery(name = "Company.findByCpyFirstNameRef", query = "SELECT c FROM Company c WHERE c.cpyFirstNameRef = :cpyFirstNameRef"),
    @NamedQuery(name = "Company.findByCpyLastNameRef", query = "SELECT c FROM Company c WHERE c.cpyLastNameRef = :cpyLastNameRef"),
    @NamedQuery(name = "Company.findByCpyPhoneNoRef", query = "SELECT c FROM Company c WHERE c.cpyPhoneNoRef = :cpyPhoneNoRef"),
    @NamedQuery(name = "Company.findByCpyCuu", query = "SELECT c FROM Company c WHERE c.cpyCuu = :cpyCuu")})
public class Company implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "usr_id")
    private Integer usrId;
    @Size(max = 45)
    @Column(name = "cpy_ragione_sociale")
    private String cpyRagioneSociale;
    @Size(max = 45)
    @Column(name = "cpy_address")
    private String cpyAddress;
    @Size(max = 45)
    @Column(name = "cpy_city")
    private String cpyCity;
    @Size(max = 2)
    @Column(name = "cpy_prov")
    private String cpyProv;
    @Size(max = 5)
    @Column(name = "cpy_zip")
    private String cpyZip;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "cpy_pec")
    private String cpyPec;
    @Size(max = 45)
    @Column(name = "cpy_codice_fiscale")
    private String cpyCodiceFiscale;
    @Size(max = 45)
    @Column(name = "cpy_partita_iva")
    private String cpyPartitaIva;
    @Column(name = "cpy_contract_checked")
    private Boolean cpyContractChecked;
    @Lob
    @Column(name = "cpy_contract")
    private byte[] cpyContract;
    @Lob
    @Column(name = "cpy_hashed_contract")
    private byte[] cpyHashedContract;
    @Size(max = 45)
    @Column(name = "cpy_first_name_ref")
    private String cpyFirstNameRef;
    @Size(max = 45)
    @Column(name = "cpy_last_name_ref")
    private String cpyLastNameRef;
    @Size(max = 45)
    @Column(name = "cpy_phone_no_ref")
    private String cpyPhoneNoRef;
    @Size(max = 7)
    @Column(name = "cpy_cuu")
    private String cpyCuu;
    @JoinColumn(name = "usr_id", referencedColumnName = "usr_id", insertable = false, updatable = false)
    @OneToOne(optional = false)
    private User user;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "cpyUsrId")
    private Collection<Employee> employeeCollection;

    public Company() {
    }

    public Company(Integer usrId) {
        this.usrId = usrId;
    }

    public Company(Integer usrId, String cpyPec) {
        this.usrId = usrId;
        this.cpyPec = cpyPec;
    }

    public Integer getUsrId() {
        return usrId;
    }

    public void setUsrId(Integer usrId) {
        this.usrId = usrId;
    }

    public String getCpyRagioneSociale() {
        return cpyRagioneSociale;
    }

    public void setCpyRagioneSociale(String cpyRagioneSociale) {
        this.cpyRagioneSociale = cpyRagioneSociale;
    }

    public String getCpyAddress() {
        return cpyAddress;
    }

    public void setCpyAddress(String cpyAddress) {
        this.cpyAddress = cpyAddress;
    }

    public String getCpyCity() {
        return cpyCity;
    }

    public void setCpyCity(String cpyCity) {
        this.cpyCity = cpyCity;
    }

    public String getCpyProv() {
        return cpyProv;
    }

    public void setCpyProv(String cpyProv) {
        this.cpyProv = cpyProv;
    }

    public String getCpyZip() {
        return cpyZip;
    }

    public void setCpyZip(String cpyZip) {
        this.cpyZip = cpyZip;
    }

    public String getCpyPec() {
        return cpyPec;
    }

    public void setCpyPec(String cpyPec) {
        this.cpyPec = cpyPec;
    }

    public String getCpyCodiceFiscale() {
        return cpyCodiceFiscale;
    }

    public void setCpyCodiceFiscale(String cpyCodiceFiscale) {
        this.cpyCodiceFiscale = cpyCodiceFiscale;
    }

    public String getCpyPartitaIva() {
        return cpyPartitaIva;
    }

    public void setCpyPartitaIva(String cpyPartitaIva) {
        this.cpyPartitaIva = cpyPartitaIva;
    }

    public Boolean getCpyContractChecked() {
        return cpyContractChecked;
    }

    public void setCpyContractChecked(Boolean cpyContractChecked) {
        this.cpyContractChecked = cpyContractChecked;
    }

    public byte[] getCpyContract() {
        return cpyContract;
    }

    public void setCpyContract(byte[] cpyContract) {
        this.cpyContract = cpyContract;
    }

    public byte[] getCpyHashedContract() {
        return cpyHashedContract;
    }

    public void setCpyHashedContract(byte[] cpyHashedContract) {
        this.cpyHashedContract = cpyHashedContract;
    }

    public String getCpyFirstNameRef() {
        return cpyFirstNameRef;
    }

    public void setCpyFirstNameRef(String cpyFirstNameRef) {
        this.cpyFirstNameRef = cpyFirstNameRef;
    }

    public String getCpyLastNameRef() {
        return cpyLastNameRef;
    }

    public void setCpyLastNameRef(String cpyLastNameRef) {
        this.cpyLastNameRef = cpyLastNameRef;
    }

    public String getCpyPhoneNoRef() {
        return cpyPhoneNoRef;
    }

    public void setCpyPhoneNoRef(String cpyPhoneNoRef) {
        this.cpyPhoneNoRef = cpyPhoneNoRef;
    }

    public String getCpyCuu() {
        return cpyCuu;
    }

    public void setCpyCuu(String cpyCuu) {
        this.cpyCuu = cpyCuu;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Collection<Employee> getEmployeeCollection() {
        return employeeCollection;
    }

    public void setEmployeeCollection(Collection<Employee> employeeCollection) {
        this.employeeCollection = employeeCollection;
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
        if (!(object instanceof Company)) {
            return false;
        }
        Company other = (Company) object;
        if ((this.usrId == null && other.usrId != null) || (this.usrId != null && !this.usrId.equals(other.usrId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.mediatica.vouchain.entities.Company[ usrId=" + usrId + " ]";
    }
    
}
