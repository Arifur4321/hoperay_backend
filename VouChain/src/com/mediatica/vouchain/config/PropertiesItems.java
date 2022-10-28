package com.mediatica.vouchain.config;

import org.springframework.context.annotation.PropertySource;

/**
 * 
 * @author Pietro Napolitano
 *
 */
@PropertySource("file:${vouchain_home}/configurations/config.properties")
public class PropertiesItems {

	private static PropertiesItems instance;
	

	
	private PropertiesItems() {};
	
	public static PropertiesItems getInstance() {
		if(instance==null) {
			instance = new PropertiesItems();
		}
		return instance;
	}
	
	




	
	
	
}
