package com.mediatica.vouchain.servicesImpl;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import com.mediatica.vouchain.dao.CitiesDaoImpl;
import com.mediatica.vouchain.dao.ProvincesDaoImpl;
import com.mediatica.vouchain.dto.CityDTO;
import com.mediatica.vouchain.dto.LocalizationDTO;
import com.mediatica.vouchain.dto.ProvinceDTO;
import com.mediatica.vouchain.entities.Tbcomuni;
import com.mediatica.vouchain.entities.Tbprovince;
import com.mediatica.vouchain.rest.controller.GeographicRestController;
import com.mediatica.vouchain.servicesInterface.GeographicServicesInterface;

/**
 * 
 * @author Pietro Napolitano
 *
 */

@Service
@Transactional
@PropertySource("file:${vouchain_home}/configurations/config.properties")
@Scope(proxyMode = ScopedProxyMode.TARGET_CLASS)
public class GeographicServiceImpl implements GeographicServicesInterface {

	@Autowired
	ProvincesDaoImpl provinceDAO;

	@Autowired
	CitiesDaoImpl cityDAO;

	@Value("${maps_url_in}")
	private String baseMapIn;

	@Value("${maps_url_out}")
	private String baseMapOut;

	@Value("${maps_api_key}")
	private String mapKey;

	@Override
	public List<CityDTO> getCitiesByProvinceId(String idProvince) {
		List<CityDTO> resultList = new ArrayList<CityDTO>();
		List<Tbcomuni> comuniList = cityDAO.getCitiesByProvince(idProvince);
		for (Tbcomuni item : comuniList) {
			CityDTO dto = new CityDTO();
			dto.setId("" + item.getId());
			dto.setName(item.getNomeComune());
			dto.setIdProvince(item.getSiglaprovinciaComune());
			resultList.add(dto);
		}
		return resultList;
	}

	@Override
	public List<ProvinceDTO> listAllProvinces() {
		List<ProvinceDTO> resultList = new ArrayList<ProvinceDTO>();
		List<Tbprovince> provinceList = provinceDAO.selectAll();
		for (Tbprovince item : provinceList) {
			ProvinceDTO dto = new ProvinceDTO();
			dto.setCod(item.getSiglaProvincia());
			dto.setName(item.getNomeProvincia());
			resultList.add(dto);
		}
		return resultList;
	}

	@Override
	public List<CityDTO> listAllCities() {
		List<CityDTO> resultList = new ArrayList<CityDTO>();
		List<Tbcomuni> comuniList = cityDAO.selectAll();
		for (Tbcomuni item : comuniList) {
			CityDTO dto = new CityDTO();
			dto.setId("" + item.getId());
			dto.setName(item.getNomeComune());
			dto.setIdProvince(item.getSiglaprovinciaComune());
			resultList.add(dto);
		}
		return resultList;
	}

	@Override
	public LocalizationDTO getMapsLongLat(String address, String prov, String city) {

		LocalizationDTO localization = new LocalizationDTO();
		RestTemplate restTemplate = new RestTemplate();
		String inputAddress = baseMapIn + address + "+" + city + "+" + prov + mapKey;
		String outPutLink;
		String lon;
		String lat;
		String result = restTemplate.getForObject(inputAddress, String.class);
		JSONObject mapsInfo = new JSONObject(result);

		JSONArray results = mapsInfo.getJSONArray("results");
		if (results.getJSONObject(0) != null || !results.getJSONObject(0).isEmpty()) {
			JSONObject inResults = results.getJSONObject(0);
			JSONObject geometry = inResults.optJSONObject("geometry");
			JSONObject location = geometry.optJSONObject("location");

			lon = location.optString("lng");
			lat = location.optString("lat");
			localization.setLongitude(lon);
			localization.setLatitude(lat);

			outPutLink = baseMapOut + lat + ",+" + lon + mapKey;

			localization.setLink(outPutLink);
		}

		return localization;
	}

}
