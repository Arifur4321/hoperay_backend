package com.mediatica.vouchain.utilities;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.validator.routines.EmailValidator;
import org.slf4j.LoggerFactory;

import com.mediatica.vouchain.config.Constants;

/**
 * 
 * @author Pietro Napolitano
 * @return
 */

public class Utils {
	private static org.slf4j.Logger log = LoggerFactory.getLogger(Utils.class);


	  private static final String PASSWORD_PATTERN =
	            "((?=.*\\d)(?=.*[a-z])(?=.*[A-Z]).{8,20})";
	  private static Pattern pattern=Pattern.compile(PASSWORD_PATTERN);



	/**
	 * 
	 *   
	 * @param email
	 * @return
	 */
	public static boolean isValidEmail(String email) {
	   log.debug("Validating email: {}",email);
	   boolean result = false;
	   try {
		   if(email!=null) {
			   email = email.trim();
			   EmailValidator eValidator = EmailValidator.getInstance();
			   result = eValidator.isValid(email);
		   }
	   } catch (Exception ex) {
	      result = false;
	      log.error("error validating email",ex);
	   }
	   return result;
	}
	
	/**
	 * 
	 * @param password
	 * @return
	 */
	public static boolean isValidPassword(String password) {
	   boolean result = false;
	   try {
		   if(password!=null) {
			   Matcher matcher=pattern.matcher(password);
			   result= matcher.matches();
		   }
	   } catch (Exception ex) {
	      result = false;
	      log.error("error validating password",ex);
	   }
	   return result;
	}

	/**
	 * 
	 * @param usrEmail
	 * @param usrPassword
	 * @return
	 */
	public static boolean validateLogin(String usrEmail, String usrPassword) {
		   boolean result = false;
		   try {
			   if(isValidEmail(usrEmail) && isValidPassword(usrPassword)) {
				   result= true;
			   }
		   } catch (Exception ex) {
		      result = false;
		      log.error("error validating password",ex);
		   }
		   return result;
		
	}

	/**
	 * 
	 * @param file
	 * @param algorithm
	 * @return
	 * @throws Exception
	 */
	public static byte[] hashFile(File file, String algorithm) throws Exception {
		byte[] hashedBytes = null;
		try (FileInputStream inputStream = new FileInputStream(file)) {
	        MessageDigest digest = MessageDigest.getInstance(algorithm);
	 
	        byte[] bytesBuffer = new byte[Constants.BYTE_1024_LENGTH];
	        int bytesRead = -1;
	 
	        while ((bytesRead = inputStream.read(bytesBuffer)) != -1) {
	            digest.update(bytesBuffer, 0, bytesRead);
	        }
	 
	        hashedBytes = digest.digest();
	 

	    } catch (NoSuchAlgorithmException | IOException ex) {
	        throw new Exception("Could not generate hash from file", ex);
	    }
        return hashedBytes;
	}
	
    private static String convertByteArrayToHexString(byte[] arrayBytes) {
        StringBuffer stringBuffer = new StringBuffer();
        for (int i = 0; i < arrayBytes.length; i++) {
            stringBuffer.append(Integer.toString((arrayBytes[i] & 0xff) + 0x100, 16)
                    .substring(1));
        }
        return stringBuffer.toString();
    }

    /**
     * 
     * @param signedContract
     * @param originalHash
     * @return
     * @throws Exception
     */
	public static boolean validateHash(File signedContract, byte[] originalHash) throws Exception  {
		byte[] hashContract = hashFile(signedContract,Constants.HASHING_ALGORITHM);
		return java.util.Arrays.equals(hashContract,originalHash);
	}
	
	/**
	 * 
	 * @param bytes
	 * @param filePath
	 * @return
	 * @throws IOException
	 */
    public static File writeByte(byte[] bytes,String filePath) throws IOException 
    { 
    	File file= new File(filePath);
        OutputStream os  = new FileOutputStream(file);
        os.write(bytes); 
        os.close(); 
        return file;
    }

    /**
     * 
     * @return
     */
	public static String generateAlphaNumericString() {
		int count=10;
		String ALPHA_NUMERIC_STRING = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
		StringBuilder builder = new StringBuilder();
		while (count-- != 0) {
			int character = (int)(Math.random()*ALPHA_NUMERIC_STRING.length());
			builder.append(ALPHA_NUMERIC_STRING.charAt(character));
		}
		return builder.toString();

	} 
	
	
	public static String formatNumeberTotwoDecimalDigitsAndThousandSeparator(double input) {
		DecimalFormat formatter = (DecimalFormat) NumberFormat.getInstance(Locale.ITALIAN);
		DecimalFormatSymbols symbols = formatter.getDecimalFormatSymbols();
	
		symbols.setGroupingSeparator('.');
		formatter.setDecimalFormatSymbols(symbols);
		formatter.setMaximumFractionDigits(2);
		formatter.setMinimumFractionDigits(2);
	
		return formatter.format(input);
	}
	
	

	
}
