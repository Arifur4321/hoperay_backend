package com.mediatica.vouchain.config;

import org.apache.log4j.PropertyConfigurator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 
 * @author Pietro Napolitano
 *
 */
@Configuration
public class Log4jConfigurator {

	
	
	@Bean
	public void configureLog4j() {
		String vouchainHome= System.getProperty("vouchain_home");
		PropertyConfigurator.configure(vouchainHome+"/configurations/log4j.properties");
	}
	
}
