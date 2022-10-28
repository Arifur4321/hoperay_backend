package com.mediatica.vouchain.dto;


import java.text.ParseException;

import com.mediatica.vouchain.entities.Company;
import com.mediatica.vouchain.entities.Employee;
import com.mediatica.vouchain.entities.User;
/**
 * 
 * @author Pietro Napolitano
 *
 */

public class LocalizationDTO extends DTO{

	private String longitude;
    private String latitude;
    private String raggio;
    private String link;
    
    
	public String getLink() {
		return link;
	}
	public void setLink(String link) {
		this.link = link;
	}
	public String getLongitude() {
		return longitude;
	}
	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}
	public String getLatitude() {
		return latitude;
	}
	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}
	public String getRaggio() {
		return raggio;
	}
	public void setRaggio(String raggio) {
		this.raggio = raggio;
	}
	@Override
	public Object wrap(Object entity, boolean includePassword) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public Object unwrap(Object entity) throws ParseException {
		// TODO Auto-generated method stub
		return null;
	}
}
