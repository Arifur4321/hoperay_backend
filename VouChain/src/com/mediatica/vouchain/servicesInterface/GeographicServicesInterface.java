package com.mediatica.vouchain.servicesInterface;

import java.util.List;

import com.mediatica.vouchain.dto.CityDTO;
import com.mediatica.vouchain.dto.LocalizationDTO;
import com.mediatica.vouchain.dto.ProvinceDTO;

public interface  GeographicServicesInterface {

	public List<CityDTO> getCitiesByProvinceId(String idProvince); 
	
	public List<ProvinceDTO> listAllProvinces(); 
	
	public List<CityDTO> listAllCities();

	public LocalizationDTO getMapsLongLat(String address,String prov, String city);
}
