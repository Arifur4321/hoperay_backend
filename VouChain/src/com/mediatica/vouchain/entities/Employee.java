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
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author Pietro
 */
@Entity
@Table(name = "employee")
@NamedQueries({
    @NamedQuery(name = "Employee.findAll", query = "SELECT e FROM Employee e"),
    @NamedQuery(name = "Employee.findByUsrId", query = "SELECT e FROM Employee e WHERE e.usrId = :usrId"),
    @NamedQuery(name = "Employee.findByEmpFirstName", query = "SELECT e FROM Employee e WHERE e.empFirstName = :empFirstName"),
    @NamedQuery(name = "Employee.findByEmpLastName", query = "SELECT e FROM Employee e WHERE e.empLastName = :empLastName"),
    @NamedQuery(name = "Employee.findByEmpMatricola", query = "SELECT e FROM Employee e WHERE e.empMatricola = :empMatricola"),
    @NamedQuery(name = "Employee.findByEmpInvitationCode", query = "SELECT e FROM Employee e WHERE e.empInvitationCode = :empInvitationCode"),
    @NamedQuery(name = "Employee.findByEmpCheckedLogin", query = "SELECT e FROM Employee e WHERE e.empCheckedLogin = :empCheckedLogin")})
public class Employee implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "usr_id")
    private Integer usrId;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "emp_first_name")
    private String empFirstName;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "emp_last_name")
    private String empLastName;
    @Size(max = 45)
    @Column(name = "emp_matricola")
    private String empMatricola;
    @Size(max = 45)
    @Column(name = "emp_invitation_code")
    private String empInvitationCode;
    @Column(name = "emp_checked_login")
    private Boolean empCheckedLogin;
    @JoinColumn(name = "cpy_usr_id", referencedColumnName = "usr_id")
    @ManyToOne(optional = false)
    private Company cpyUsrId;
    @JoinColumn(name = "usr_id", referencedColumnName = "usr_id", insertable = false, updatable = false)
    @OneToOne(optional = false)
    private User user;

    public Employee() {
    }

    public Employee(Integer usrId) {
        this.usrId = usrId;
    }

    public Employee(Integer usrId, String empFirstName, String empLastName) {
        this.usrId = usrId;
        this.empFirstName = empFirstName;
        this.empLastName = empLastName;
    }

    public Integer getUsrId() {
        return usrId;
    }

    public void setUsrId(Integer usrId) {
        this.usrId = usrId;
    }

    public String getEmpFirstName() {
        return empFirstName;
    }

    public void setEmpFirstName(String empFirstName) {
        this.empFirstName = empFirstName;
    }

    public String getEmpLastName() {
        return empLastName;
    }

    public void setEmpLastName(String empLastName) {
        this.empLastName = empLastName;
    }

    public String getEmpMatricola() {
        return empMatricola;
    }

    public void setEmpMatricola(String empMatricola) {
        this.empMatricola = empMatricola;
    }

    public String getEmpInvitationCode() {
        return empInvitationCode;
    }

    public void setEmpInvitationCode(String empInvitationCode) {
        this.empInvitationCode = empInvitationCode;
    }

    public Boolean getEmpCheckedLogin() {
        return empCheckedLogin;
    }

    public void setEmpCheckedLogin(Boolean empCheckedLogin) {
        this.empCheckedLogin = empCheckedLogin;
    }

    public Company getCpyUsrId() {
        return cpyUsrId;
    }

    public void setCpyUsrId(Company cpyUsrId) {
        this.cpyUsrId = cpyUsrId;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
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
        if (!(object instanceof Employee)) {
            return false;
        }
        Employee other = (Employee) object;
        if ((this.usrId == null && other.usrId != null) || (this.usrId != null && !this.usrId.equals(other.usrId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.mediatica.vouchain.entities.Employee[ usrId=" + usrId + " ]";
    }
    
}
