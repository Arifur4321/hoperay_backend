package com.mediatica.vouchain.export;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;

import org.apache.pdfbox.pdmodel.PDDocument;

import org.apache.pdfbox.pdmodel.interactive.form.PDAcroForm;
import org.apache.pdfbox.pdmodel.interactive.form.PDField;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;

import com.mediatica.vouchain.config.Constants;
import com.mediatica.vouchain.entities.User;
import com.mediatica.vouchain.servicesImpl.UserServiceAbstract;
import com.mediatica.vouchain.utilities.Utils;

/**
 * 
 * @author Pietro Napolitano
 *
 */

@Service
@PropertySource("file:${vouchain_home}/configurations/config.properties")
@Scope(proxyMode = ScopedProxyMode.TARGET_CLASS)
public class PdfServicesImpl implements PdfServicesInterface {

	private static org.slf4j.Logger log = LoggerFactory.getLogger(PdfServicesImpl.class);
	
	@Value("${qr_code_format}")
	private String qrString;

	@Override
	public String exportCompanyContract(User user) throws Exception {
		log.info("Exporting contract for user:{},{} ", user.getUsrId(), user.getUsrEmail());
		PDDocument pDDocument = PDDocument.load(new File(
				Constants.VOUCHAIN_HOME + Constants.PDF_TEMPLATE_SUBDIR_PATH + Constants.PDF_TEMPLATE_FILE_NAME));
		PDAcroForm pDAcroForm = pDDocument.getDocumentCatalog().getAcroForm();
		Date today = new Date();

		PDField field = pDAcroForm.getField("clientCode");
		field.setValue("" + user.getUsrId());
		field.setReadOnly(true);

		field = pDAcroForm.getField("companyName");
		field.setValue(
				user.getCompany().getCpyRagioneSociale() != null ? user.getCompany().getCpyRagioneSociale() : "");
		field.setReadOnly(true);

		field = pDAcroForm.getField("VAT");
		field.setValue(user.getCompany().getCpyCodiceFiscale() != null ? user.getCompany().getCpyCodiceFiscale() : "");
		field.setReadOnly(true);

		String address = user.getCompany().getCpyAddress() != null ? user.getCompany().getCpyAddress()
				: "" + "," + user.getCompany().getCpyCity() != null ? user.getCompany().getCpyCity()
						: "" + "," + user.getCompany().getCpyProv() != null ? user.getCompany().getCpyProv()
								: "" + " " + user.getCompany().getCpyZip() != null ? user.getCompany().getCpyZip() : "";
		field = pDAcroForm.getField("address");
		field.setValue(address);
		field.setReadOnly(true);

		String lastname = user.getCompany().getCpyLastNameRef() != null ? user.getCompany().getCpyLastNameRef() : "";
		String firstname = user.getCompany().getCpyFirstNameRef() != null ? user.getCompany().getCpyFirstNameRef() : "";
		String representative = lastname + " " + firstname;

		field = pDAcroForm.getField("representative");
		field.setValue(representative);
		field.setReadOnly(true);

		field = pDAcroForm.getField("phoneNumber");
		field.setValue(user.getCompany().getCpyPhoneNoRef() != null ? user.getCompany().getCpyPhoneNoRef() : "");
		field.setReadOnly(true);

		field = pDAcroForm.getField("email");
		field.setValue(user.getUsrEmail());
		field.setReadOnly(true);

		field = pDAcroForm.getField("PEC");
		field.setValue(user.getCompany().getCpyPec());
		field.setReadOnly(true);

		field = pDAcroForm.getField("invoiceCode");
		field.setValue(user.getCompany().getCpyCuu() != null ? user.getCompany().getCpyCuu() : "");
		field.setReadOnly(true);

		field = pDAcroForm.getField("date");
		field.setValue(Constants.FORMATTER_DD_MM_YYYY.format(today));
		field.setReadOnly(true);

		String contractFile = Constants.VOUCHAIN_HOME + Constants.PDF_EXPORT_SUBDIR_PATH + (new Date()).getTime() + "_"
				+ user.getUsrEmail().substring(0, user.getUsrEmail().indexOf("@")) + Constants.PDF_SUFFIX;
		log.info("saving contract for userId: {} in {}", user.getUsrId(), contractFile);
		pDDocument.save(contractFile);
		pDDocument.close();

		File contract = new File(contractFile);
		user.getCompany().setCpyHashedContract(Utils.hashFile(contract, Constants.HASHING_ALGORITHM));

		return contractFile;

	}

	@Override
	public byte[] exportQrCode(byte[] qrCode) throws IOException {

		String pdfFile = Constants.VOUCHAIN_HOME + Constants.PDF_EXPORT_SUBDIR_PATH;
		OutputStream out = new FileOutputStream(pdfFile+qrString);
		out.write(qrCode);
		out.close();
		Path pdfPath = Paths.get(pdfFile+qrString);
		byte[] pdf = Files.readAllBytes(pdfPath);
		return pdf;
	}

}
