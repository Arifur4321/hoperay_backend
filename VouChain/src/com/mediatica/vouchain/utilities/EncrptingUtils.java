package com.mediatica.vouchain.utilities;

import java.security.Key;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.mediatica.vouchain.config.Constants;

@Component
@Scope("singleton")
@PropertySource("file:${vouchain_home}/configurations/config.properties")
public class EncrptingUtils {	
   
	@Value("${encrypting_key}")
	private String keyStr;
    
    private Key aesKey = null;
    private Cipher cipher = null;

    synchronized private void init() throws Exception {     	
  	
    	if(keyStr!=null) {
    		keyStr=keyStr.trim();
    	}
        if (keyStr == null || keyStr.length() != Constants.ENCRYPTING_KEY_LENGTH) {
            throw new Exception("bad aes key configured");
        }
        if (aesKey == null) {
            aesKey = new SecretKeySpec(keyStr.getBytes(), "AES");
            cipher = Cipher.getInstance("AES");
        }
    }

    synchronized public String encrypt(String text) throws Exception {
        init();
        cipher.init(Cipher.ENCRYPT_MODE, aesKey);
        return toHexString(cipher.doFinal(text.getBytes()));
    }

    synchronized public String decrypt(String text) throws Exception {
        init();
        cipher.init(Cipher.DECRYPT_MODE, aesKey);
        return new String(cipher.doFinal(toByteArray(text)));
    }

    public static String toHexString(byte[] array) {
        return DatatypeConverter.printHexBinary(array);
    }

    public static byte[] toByteArray(String s) {
        return DatatypeConverter.parseHexBinary(s);
    }

}
