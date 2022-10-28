package com.mediatica.vouchain.servicesImpl;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.mediatica.vouchain.config.Constants;
import com.mediatica.vouchain.config.enumerations.TransactionStatus;
import com.mediatica.vouchain.config.enumerations.TransactionType;
import com.mediatica.vouchain.dao.MerchantDaoImpl;
import com.mediatica.vouchain.dao.QrCodeDaoImpl;
import com.mediatica.vouchain.dao.TransactionDaoImpl;
import com.mediatica.vouchain.dao.UserDaoImpl;
import com.mediatica.vouchain.dto.DTO;
import com.mediatica.vouchain.dto.EmployeeDTO;
import com.mediatica.vouchain.dto.MerchantDTO;
import com.mediatica.vouchain.dto.QrCodeDTO;
import com.mediatica.vouchain.dto.ResponseGrantPermissionDTO;
import com.mediatica.vouchain.dto.ResponseNewKeyPairDTO;
import com.mediatica.vouchain.entities.Employee;
import com.mediatica.vouchain.entities.Merchant;
import com.mediatica.vouchain.entities.QrCode;
import com.mediatica.vouchain.entities.Transaction;
import com.mediatica.vouchain.entities.User;
import com.mediatica.vouchain.exceptions.EmailYetInTheSystemException;
import com.mediatica.vouchain.export.PdfServicesImpl;
import com.mediatica.vouchain.mail.Mail;
import com.mediatica.vouchain.mail.MailSenderServiceImpl;
import com.mediatica.vouchain.rest.client.BlockChainRestClient;
import com.mediatica.vouchain.servicesInterface.MerchantServicesInterface;
import com.mediatica.vouchain.servicesInterface.QrCodeServicesInterface;
import com.mediatica.vouchain.utilities.Utils;

/**
 * 
 * @author Pietro Napolitano
 *
 */
@Service
@Transactional
@PropertySource("file:${vouchain_home}/configurations/config.properties")
@Scope( proxyMode = ScopedProxyMode.TARGET_CLASS )
public class QrCodeServiceImpl implements QrCodeServicesInterface{

	@Autowired
	UserDaoImpl userDao;
	
	@Autowired
	MerchantDaoImpl merchantDao;
	
	@Autowired
	QrCodeDaoImpl qrDao;
	
	@Value("${qrcode_base}")
	private String qrCodeBase;
	
	
	private static org.slf4j.Logger log = LoggerFactory.getLogger(QrCodeServiceImpl.class);

	@Override
	public QrCodeDTO saveQrCode(QrCodeDTO qrCodeDTO, User usr) {
		///////Inserting in database
		QrCode qrCode = (QrCode) qrCodeDTO.unwrap(qrCodeDTO, usr);
		
		User user = new User();
		user = userDao.findUserByEmail(usr.getUsrEmail());
		
		qrCode.setUser(user);
		qrCode.setQrId(1);
		
		qrCode = qrDao.save(qrCode);
		qrCodeDTO = (QrCodeDTO) qrCodeDTO.wrap(qrCode);
		
		return qrCodeDTO;
	}
	
	@Override
	public QrCodeDTO modQrCode(QrCodeDTO qrCodeDTO, User usr) {
		///////Inserting in database
		QrCode qrCode = (QrCode) qrCodeDTO.unwrap(qrCodeDTO, usr);
		
		User user = new User();
		user = userDao.findUserByEmail(usr.getUsrEmail());
		
		qrCode.setUser(user);
		
		qrDao.update(qrCode);
		
		return qrCodeDTO;
	}
	
	@Override
	public void cancQrCode(QrCodeDTO qrCodeDTO, User usr) {
		///////Inserting in database
		QrCode qrCode = (QrCode) qrCodeDTO.unwrap(qrCodeDTO, usr);
		
		User user = new User();
		user = userDao.findUserByEmail(usr.getUsrEmail());
		
		qrCode.setUser(user);
		
		qrDao.delete(qrCode);
		
	}

	public List<QrCodeDTO> getQrCodeList(Integer id) {
		// TODO Controllare query in QrCodeDaoImpl e vedere se effettivamente vengono presi tutti i qr relativi ad un dato merchant
		List<QrCode> qrList = qrDao.findQrCodeByMerchant(id);
		List<QrCodeDTO> result = null;
		if(qrList!=null) {
			result=new ArrayList<QrCodeDTO>();
			for(QrCode item:qrList) {
				QrCodeDTO dto = new QrCodeDTO();
				dto=(QrCodeDTO)dto.wrap(item);
				result.add(dto);
			}
		}
		return result;
	}

	@Override
	public QrCode findByMrcCash(Integer mrc, String cash) {
		QrCode qrCode = qrDao.findQrCodeByMrcCash(mrc, cash);
		return qrCode;
	}

	@Override
	public QrCode findByValue(String value) {
		QrCode qrCode = qrDao.findQrCodeByValue(value);
		return qrCode;
	}

	@Override
	public byte[] produceQrCode(MerchantDTO merchantDTO) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		byte[] imageInByte = null;
		try {
			ImageIO.write(generateQRCodeImage(generateQRCodeString(merchantDTO.getUsrId(),
					Integer.parseInt(merchantDTO.getMrcCash()), qrCodeBase)), "jpg", baos);
			baos.flush();
			imageInByte = baos.toByteArray();
			baos.close();
		} catch (Exception e) {
			// TODO: handle exception
		}
		return imageInByte;
	}

	public String generateQRCodeString(String id, Integer cassa, String base) throws Exception {
		String barcodeText;
		barcodeText = base + "dst=" + id;
		if (cassa != null) {
			barcodeText = barcodeText + ";regnum=" + cassa;
			barcodeText = barcodeText.trim();
		}
		return barcodeText;
	}
	
	public BufferedImage generateQRCodeImage(String barcodeText) throws Exception {
		QRCodeWriter barcodeWriter = new QRCodeWriter();
		BitMatrix bitMatrix = barcodeWriter.encode(barcodeText, BarcodeFormat.QR_CODE, 200, 200);
		return MatrixToImageWriter.toBufferedImage(bitMatrix);
	}
	
	public boolean validatorQRCode(QrCodeDTO qrCode, String base) throws NumberFormatException, Exception {
		String value = qrCode.getQrValue();

		String trueValue = generateQRCodeString(qrCode.getQrMrc(), Integer.parseInt(qrCode.getQrCash()), base);

		if (value.equals(trueValue)) {
			return true;
		}
		return false;
	}
	
}
