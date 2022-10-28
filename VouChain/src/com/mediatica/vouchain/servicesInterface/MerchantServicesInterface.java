package com.mediatica.vouchain.servicesInterface;

import java.util.HashMap;
import java.util.List;

import com.mediatica.vouchain.dto.LocalizationDTO;
import com.mediatica.vouchain.dto.MerchantDTO;
import com.mediatica.vouchain.entities.Merchant;

/**
 * 
 * it manages the services about a merchant entity
 *
 */
public interface MerchantServicesInterface {

	/**
	 * 
	 * it retrieves all the merchant entities stored in the database
	 * 
	 * @return
	 */
	public List<MerchantDTO> getMerchantsList();
	
	public Merchant getFromId(Integer id);
	
	public HashMap<MerchantDTO, String> searchMerchantGIS(LocalizationDTO localizationDTO);

}
