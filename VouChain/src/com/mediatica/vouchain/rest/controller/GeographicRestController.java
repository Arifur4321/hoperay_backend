package com.mediatica.vouchain.rest.controller;

import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.mediatica.vouchain.dto.CityDTO;
import com.mediatica.vouchain.dto.ProvinceDTO;
import com.mediatica.vouchain.servicesImpl.GeographicServiceImpl;

/**
 * 
 * @author Pietro Napolitano
 *
 */
@RestController
@CrossOrigin(origins="*",allowedHeaders = "*")  
@PropertySource("file:${vouchain_home}/configurations/config.properties")
@Scope( proxyMode = ScopedProxyMode.TARGET_CLASS )
public class GeographicRestController {
	
	private static org.slf4j.Logger log = LoggerFactory.getLogger(GeographicRestController.class);
	
	@Autowired
	GeographicServiceImpl geographicService;
	
	@GetMapping("/api/geographic/getAllProvinces")
	public List<ProvinceDTO> getAllProvinces(){
		List<ProvinceDTO> list=null;
		log.debug("Calling getAllProvinces rest service");
		try {
			list = geographicService.listAllProvinces();
		}catch(Exception e) {
			log.error(e.getMessage(),e);
		}
		return list;
	}
	
	@GetMapping("/api/geographic/getAllCitiesByProvince/{idProvince}")
	public List<CityDTO> getAllCitiesByProvince(@PathVariable("idProvince") String idProvince){
		List<CityDTO> list=null;
		log.debug("Calling getAllCitiesByProvince rest service idProvince: {}",idProvince);
		try {
			list = geographicService.getCitiesByProvinceId(idProvince);
		}catch(Exception e) {
			log.error(e.getMessage(),e);
		}
		return list;
	}
	
	@GetMapping("/api/geographic/getAllCities")
	public List<CityDTO> getAllCities(){
		List<CityDTO> list=null;
		log.debug("Calling getAllCities rest service");
		try {
			list = geographicService.listAllCities();
		}catch(Exception e) {
			log.error(e.getMessage(),e);
		}
		return list;
	}
	
	
}
