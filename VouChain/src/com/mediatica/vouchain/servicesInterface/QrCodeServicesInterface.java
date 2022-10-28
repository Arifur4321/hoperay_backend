package com.mediatica.vouchain.servicesInterface;

import java.util.List;

import com.mediatica.vouchain.dto.MerchantDTO;
import com.mediatica.vouchain.dto.QrCodeDTO;
import com.mediatica.vouchain.entities.QrCode;
import com.mediatica.vouchain.entities.User;

/**
 * 
 * it manages the services about a qrcode entity
 *
 */
public interface QrCodeServicesInterface {

	/**
	 * 
	 * it retrieves all the qrcode entities stored in the database
	 * 
	 * @return
	 */
	public List<QrCodeDTO> getQrCodeList(Integer id);
	
	public QrCodeDTO saveQrCode(QrCodeDTO qrCodeDTO, User usr);
	
	public QrCode findByMrcCash(Integer mrc, String cash);
	
	public QrCode findByValue(String value);

	QrCodeDTO modQrCode(QrCodeDTO qrCodeDTO, User usr);

	void cancQrCode(QrCodeDTO qrCodeDTO, User usr);
	
	byte[] produceQrCode(MerchantDTO merchantDTO);

}
