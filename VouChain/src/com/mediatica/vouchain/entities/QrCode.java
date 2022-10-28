/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mediatica.vouchain.entities;

import java.io.Serializable;
import java.sql.Blob;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
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
 * @author Luca Gulinelli
 */
@Entity
@Table(name = "qr_code")
@NamedQueries({
    @NamedQuery(name = "QrCode.findAll", query = "SELECT q FROM QrCode q")})
public class QrCode implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @NotNull
    @Column(name = "qr_id")
    private Integer qrId;
    @Size(max = 150)
    @Column(name = "qr_value")
    private String qrValue;
    @Column(name = "usr_id")
    private Integer qrMrcId;
    @Column(name = "qr_cash")
    private Integer qrCash;
    @JoinColumn(name = "usr_id", referencedColumnName = "usr_id", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private User user;
    @Column(name = "qr_image")
    private byte[] qrImgCode;

    public QrCode() {
    }

	public Integer getQrId() {
		return qrId;
	}

	public void setQrId(Integer qrId) {
		this.qrId = qrId;
	}

	public String getQrValue() {
		return qrValue;
	}

	public void setQrValue(String qrValue) {
		this.qrValue = qrValue;
	}

	public Integer getQrMrcId() {
		return qrMrcId;
	}

	public void setQrMrcId(Integer qrMrcId) {
		this.qrMrcId = qrMrcId;
	}

	public Integer getQrCash() {
		return qrCash;
	}

	public void setQrCash(Integer qrCash) {
		this.qrCash = qrCash;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}
	
	
	

	public byte[] getQrImgCode() {
		return qrImgCode;
	}

	public void setQrImgCode(byte[] qrImgCode) {
		this.qrImgCode = qrImgCode;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((qrId == null) ? 0 : qrId.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		QrCode other = (QrCode) obj;
		if (qrId == null) {
			if (other.qrId != null)
				return false;
		} else if (!qrId.equals(other.qrId))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "QrCode [qrId=" + qrId + ", qrValue=" + qrValue + "]";
	}
}
