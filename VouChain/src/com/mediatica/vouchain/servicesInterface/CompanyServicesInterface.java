package com.mediatica.vouchain.servicesInterface;

import java.io.IOException;

import org.springframework.web.multipart.MultipartFile;

import com.mediatica.vouchain.dto.CompanyDTO;
import com.mediatica.vouchain.exceptions.ContractNotVaidException;
/**
 * 
 * @author Pietro Napolitano
 *
 */
public interface CompanyServicesInterface extends UserServicesInterface {
	
	/**
	 * It verifies the sign , the hash of the contract and save it in the database
	 * 
	 * @param userId
	 * @param contract
	 * @return
	 */
	public CompanyDTO checkLoadSign(String userId,MultipartFile contract)throws IOException,ContractNotVaidException,Exception ;

	
	
}
