package com.mediatica.vouchain.rest.controller;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.sql.Blob;
import java.text.DateFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import javax.imageio.ImageIO;
import javax.sql.rowset.serial.SerialBlob;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.mediatica.vouchain.config.Constants;
import com.mediatica.vouchain.dao.QrCodeDaoImpl;
import com.mediatica.vouchain.dto.DTOList;
import com.mediatica.vouchain.dto.InvoiceDTO;
import com.mediatica.vouchain.dto.MerchantDTO;
import com.mediatica.vouchain.dto.QrCodeDTO;
import com.mediatica.vouchain.dto.SimpleResponseDTO;
import com.mediatica.vouchain.dto.TransactionDTO;
import com.mediatica.vouchain.dto.TransactionRequestDTO;
import com.mediatica.vouchain.dto.VoucherDTO;
import com.mediatica.vouchain.entities.Invoice;
import com.mediatica.vouchain.entities.Merchant;
import com.mediatica.vouchain.entities.QrCode;
import com.mediatica.vouchain.entities.User;
import com.mediatica.vouchain.export.PdfServicesImpl;
import com.mediatica.vouchain.servicesImpl.MerchantServiceImpl;
import com.mediatica.vouchain.servicesImpl.QrCodeServiceImpl;
import com.sun.mail.util.QEncoderStream;

// TODO: Auto-generated Javadoc
/**
 * The Class QrCodeRestController.
 */
@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
@PropertySource("file:${vouchain_home}/configurations/config.properties")
@Scope(proxyMode = ScopedProxyMode.TARGET_CLASS)
public class QrCodeRestController {

	/** The QrCode service. */
	@Autowired
	QrCodeServiceImpl qrService;

	@Autowired
	QrCodeDaoImpl qrDao;

	/** The Merchant service. */
	@Autowired
	MerchantServiceImpl mrcService;

	@Autowired
	PdfServicesImpl pdfServImpl;

	@Value("${qrcode_base}")
	private String qrCodeBase;

	/** The log. */
	private static org.slf4j.Logger log = LoggerFactory.getLogger(QrCodeRestController.class);

	// BE_58 Il modulo riceve un QR Code
	// ricava la causale della transazione,
	// sulla base dell’algoritmo concordato.
	
	@PostMapping("/api/qrcode/qrcodeGenerate")
	public DTOList<QrCodeDTO> GenerateCasual(@RequestBody MerchantDTO merchantDTO) throws Exception {

		DTOList<QrCodeDTO> response = new DTOList<QrCodeDTO>();
		QrCode qrCode = new QrCode();
		List<QrCodeDTO> list = null;

		User usr = (User) merchantDTO.unwrap(mrcService.showProfile(merchantDTO.getUsrId()));

		try {
			if (merchantDTO.getMrcCash() == null) {
				response.setStatus(Constants.RESPONSE_STATUS.KO.toString());
				response.setErrorDescription("Numero di cassa richiesto");
				return response;
			}
			qrCode.setQrValue(qrService.generateQRCodeString(merchantDTO.getUsrId(),
					Integer.parseInt(merchantDTO.getMrcCash()), qrCodeBase));
			qrCode.setQrMrcId(Integer.parseInt(merchantDTO.getUsrId()));
			qrCode.setQrCash(Integer.parseInt(merchantDTO.getMrcCash()));
			qrCode.setUser(usr);

			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			ImageIO.write(qrService.generateQRCodeImage(qrService.generateQRCodeString(merchantDTO.getUsrId(),
					Integer.parseInt(merchantDTO.getMrcCash()), qrCodeBase)), "jpg", baos);
			baos.flush();
			byte[] imageInByte = baos.toByteArray();
			baos.close();

			qrCode.setQrImgCode(imageInByte);

			QrCodeDTO qrCodeDTO = new QrCodeDTO();
			qrCodeDTO = (QrCodeDTO) qrCodeDTO.wrap(qrCode);
			list = new ArrayList<QrCodeDTO>();
			list.add(qrCodeDTO);

			qrService.saveQrCode(qrCodeDTO, usr);

			response.setList(list);
			response.setStatus(Constants.RESPONSE_STATUS.OK.toString());

			return response;

		} catch (Exception e) {
			response.setStatus(Constants.RESPONSE_STATUS.KO.toString());
			response.setErrorDescription(e.getMessage());
			e.printStackTrace();
			return response;
		}
	}
	
	// BE_57 Analizza il QRCode ricevuto dall’applicazione,
	// verifica la validità per vouchain,
	// controlla che il QR non sia stato cancellato o modificato,
	// ed eventualmente identifica il beneficiario,
	// fornendo un errore negli altri casi.

	@PostMapping("/api/qrcode/inspectQRCode")
	public MerchantDTO inspectQrCode(@RequestBody QrCodeDTO qrCodeDTO) throws Exception {

		MerchantDTO response = new MerchantDTO();
		QrCode qrCodeIN = new QrCode();

		try {

			qrCodeIN = qrService.findByValue(qrCodeDTO.getQrValue());

			if (qrCodeIN == null) {
				response.setStatus(Constants.RESPONSE_STATUS.KO.toString());
				response.setErrorDescription("QR Code non valido");
				return response;
			}

			Merchant mrc = mrcService.getFromId(qrCodeIN.getQrMrcId());
			if (mrc == null) {
				response.setStatus(Constants.RESPONSE_STATUS.KO.toString());
				response.setErrorDescription("merchant non trovato");
				return response;
			}
			if (!mrc.getUser().getUsrActive() == true) {
				response.setStatus(Constants.RESPONSE_STATUS.KO.toString());
				response.setErrorDescription("merchant non attivo");
				return response;
			}

			response = (MerchantDTO) response.wrap(mrc.getUser());
			response.setMrcCash(qrCodeIN.getQrCash().toString());

		} catch (Exception e) {
			response.setStatus(Constants.RESPONSE_STATUS.KO.toString());
			response.setErrorDescription(e.getMessage());
			e.printStackTrace();
			return response;
		}

		response.setStatus(Constants.RESPONSE_STATUS.OK.toString());

		return response;
	}

	//BE_61 Fornisce l’elenco dei QR Code che l’affiliato ha già generato,
	//accetta in input l'id dell'utente affiliato
	
	@GetMapping("/api/qrcode/listQRCode/{usrId}")
	public DTOList<QrCodeDTO> listQrCode(@PathVariable("usrId") String usrId) throws Exception {

		DTOList<QrCodeDTO> response = new DTOList<QrCodeDTO>();
		try {
			
			List<QrCodeDTO> listaQrCode = new ArrayList<QrCodeDTO>();
			listaQrCode = qrService.getQrCodeList(Integer.parseInt(usrId));
			
			if(listaQrCode == null || listaQrCode.isEmpty()) {
				response.setStatus(Constants.RESPONSE_STATUS.KO.toString());
				response.setErrorDescription("no_qrcode_found");
				return response;
			}
			
			response.setList(listaQrCode);

		} catch (Exception e) {
			response.setStatus(Constants.RESPONSE_STATUS.KO.toString());
			response.setErrorDescription(e.getMessage());
			e.printStackTrace();
			return response;
		}

		response.setStatus(Constants.RESPONSE_STATUS.OK.toString());

		return response;
	}

	//BE_55 Riceve in input l’identificativo dell’affiliato ed il numero della cassa,
	//e l’operazione richiesta (ins/mod/can) e genera modifica o cancella il QR Code
	
	@PostMapping("/api/qrcode/manageQRCode/{operation}")
	public QrCodeDTO manageQRCode(@RequestBody MerchantDTO merchantDTO, @PathVariable String operation)
			throws Exception {

		QrCodeDTO response = new QrCodeDTO();
		User usr = (User) merchantDTO.unwrap(mrcService.showProfile(merchantDTO.getUsrId()));
		QrCode qrCode = null;

		switch (operation) {

		case "mod":
			try {
				if (merchantDTO.getMrcCash() == null) {
					response.setStatus(Constants.RESPONSE_STATUS.KO.toString());
					response.setErrorDescription("Numero di cassa richiesto");
					return response;
				}
				qrCode = qrService.findByMrcCash(Integer.parseInt(merchantDTO.getUsrId()), merchantDTO.getMrcCash());

				if (qrCode == null) {
					response.setStatus(Constants.RESPONSE_STATUS.KO.toString());
					response.setErrorDescription("Qr Code non esistente");
					return response;
				}
				qrCode.setQrValue(qrService.generateQRCodeString(merchantDTO.getUsrId(),
						Integer.parseInt(merchantDTO.getMrcCash()), qrCodeBase));
				qrCode.setQrMrcId(Integer.parseInt(merchantDTO.getUsrId()));
				qrCode.setQrCash(Integer.parseInt(merchantDTO.getMrcCash()));
				qrCode.setUser(usr);

				byte[] imageInByte = qrService.produceQrCode(merchantDTO);
				qrCode.setQrImgCode(imageInByte);

				QrCodeDTO qrCodeDTO = new QrCodeDTO();
				qrCodeDTO = (QrCodeDTO) qrCodeDTO.wrap(qrCode);
				//qrCodeDTO.setQrCodePDF(pdfServImpl.exportQrCode(qrCodeDTO.getQrImage()));

				response = qrService.modQrCode(qrCodeDTO, usr);
				response.setStatus(Constants.RESPONSE_STATUS.OK.toString());

				return response;

			} catch (Exception e) {
				response.setStatus(Constants.RESPONSE_STATUS.KO.toString());
				response.setErrorDescription(e.getMessage());
				e.printStackTrace();
				return response;
			}

		case "can":
			try {
				if (merchantDTO.getMrcCash() == null) {
					response.setStatus(Constants.RESPONSE_STATUS.KO.toString());
					response.setErrorDescription("Numero_cassa_richiesto");
					return response;
				}

				qrCode = qrService.findByMrcCash(Integer.parseInt(merchantDTO.getUsrId()), merchantDTO.getMrcCash());

				if (qrCode == null) {
					response.setStatus(Constants.RESPONSE_STATUS.KO.toString());
					response.setErrorDescription("qr_non_esistente");
					return response;
				}

				QrCodeDTO qrCodeDTO = new QrCodeDTO();
				qrCodeDTO = (QrCodeDTO) qrCodeDTO.wrap(qrCode);

				qrService.cancQrCode(qrCodeDTO, usr);

				response = qrCodeDTO;
				response.setStatus(Constants.RESPONSE_STATUS.OK.toString());

				return response;

			} catch (Exception e) {
				response.setStatus(Constants.RESPONSE_STATUS.KO.toString());
				response.setErrorDescription(e.getMessage());
				e.printStackTrace();
				return response;
			}

		default:
			try {
				if (merchantDTO.getMrcCash() == null) {
					response.setStatus(Constants.RESPONSE_STATUS.KO.toString());
					response.setErrorDescription("Numero_cassa_richiesto");
					return response;
				}

				qrCode = qrService.findByMrcCash(Integer.parseInt(merchantDTO.getUsrId()), merchantDTO.getMrcCash());

				if (qrCode != null) {
					response.setStatus(Constants.RESPONSE_STATUS.KO.toString());
					response.setErrorDescription("qr_esistente");
					return response;
				}

				qrCode = new QrCode();
				qrCode.setQrId(1);
				qrCode.setQrValue(qrService.generateQRCodeString(merchantDTO.getUsrId(),
						Integer.parseInt(merchantDTO.getMrcCash()), qrCodeBase));
				qrCode.setQrMrcId(Integer.parseInt(merchantDTO.getUsrId()));
				qrCode.setQrCash(Integer.parseInt(merchantDTO.getMrcCash()));
				qrCode.setUser(usr);

				byte[] imageInByte = qrService.produceQrCode(merchantDTO);

				qrCode.setQrImgCode(imageInByte);

				QrCodeDTO qrCodeDTO = new QrCodeDTO();
				qrCodeDTO = (QrCodeDTO) qrCodeDTO.wrap(qrCode);
				//qrCodeDTO.setQrCodePDF(pdfServImpl.exportQrCode(qrCodeDTO.getQrImage()));

				response = qrService.saveQrCode(qrCodeDTO, usr);

				response.setStatus(Constants.RESPONSE_STATUS.OK.toString());

				return response;

			} catch (Exception e) {
				response.setStatus(Constants.RESPONSE_STATUS.KO.toString());
				response.setErrorDescription(e.getMessage());
				e.printStackTrace();
				return response;
			}
		}
	}
	
	
}
