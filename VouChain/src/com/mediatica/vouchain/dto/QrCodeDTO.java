package com.mediatica.vouchain.dto;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.sql.Blob;
import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.oned.EAN13Writer;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import com.google.zxing.qrcode.encoder.QRCode;
import com.mediatica.vouchain.entities.QrCode;
import com.mediatica.vouchain.entities.User;
/**
 * 
 * @author Luca Gulinelli
 *
 */

public class QrCodeDTO {
	
		private String qrId;

	    private String qrValue;

	    private String qrMrc;

	    private String qrCash;
	    
	    private byte[] qrImage;
	    
	    private byte[] qrCodePDF;
	    
	    private String status;
	    
	    private String errorDescription;
		
		
		public String getStatus() {
			return status;
		}
		public void setStatus(String status) {
			this.status = status;
		}
		public String getErrorDescription() {
			return errorDescription;
		}
		public void setErrorDescription(String errorDescription) {
			this.errorDescription = errorDescription;
		}
	    

		public String getQrValue() {
			return qrValue;
		}

		public void setQrValue(String qrValue) {
			this.qrValue = qrValue;
		}

		public String getQrMrc() {
			return qrMrc;
		}

		public void setQrMrc(String qrMrc) {
			this.qrMrc = qrMrc;
		}

		public String getQrCash() {
			return qrCash;
		}

		public void setQrCash(String qrCash) {
			this.qrCash = qrCash;
		}

		public byte[] getQrImage() {
			return qrImage;
		}

		public void setQrImage(byte[] qrImage) {
			this.qrImage = qrImage;
		}

		public String getQrId() {
			return qrId;
		}

		public void setQrId(String qrId) {
			this.qrId = qrId;
		}

		public byte[] getQrCodePDF() {
			return qrCodePDF;
		}

		public void setQrCodePDF(byte[] qrCodePDF) {
			this.qrCodePDF = qrCodePDF;
		}

		public Object wrap(Object entity) {
	    	QrCode qrCode = (QrCode)entity;
	    	QrCodeDTO dto = new QrCodeDTO();
	    	dto.setQrId(qrCode.getQrId().toString());
			dto.setQrValue(qrCode.getQrValue());
			dto.setQrMrc(qrCode.getQrMrcId().toString());
			dto.setQrCash(qrCode.getQrCash().toString());
			dto.setQrImage(qrCode.getQrImgCode());
	    	
	    	return dto; 
		}
		
		public Object unwrap(Object code, User user) {
			
			QrCodeDTO qrCodeDTO = (QrCodeDTO) code;
			QrCode qrCode = new QrCode();
			
			qrCode.setQrId(Integer.parseInt(qrCodeDTO.getQrId()));
			qrCode.setQrValue(qrCodeDTO.getQrValue());
			qrCode.setQrCash(Integer.parseInt(qrCodeDTO.getQrCash()));
			qrCode.setQrMrcId(Integer.parseInt(qrCodeDTO.getQrMrc()));
			qrCode.setQrImgCode(qrCodeDTO.getQrImage());
			qrCode.setUser(user);
			
			return qrCode;
		}


}
