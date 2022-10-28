package com.mediatica.vouchain.export;

import java.io.IOException;

import org.apache.pdfbox.pdmodel.encryption.InvalidPasswordException;

import com.mediatica.vouchain.entities.User;
/**
 * 
 * @author Pietro Napolitano
 *
 */

public interface PdfServicesInterface {
	
	public String exportCompanyContract(User user)throws InvalidPasswordException, IOException,Exception;

	byte[] exportQrCode(byte[] qrCode) throws IOException;
	

}
