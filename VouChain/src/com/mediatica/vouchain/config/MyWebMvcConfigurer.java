package com.mediatica.vouchain.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * 
 * @author Pietro Napolitano
 *
 */
@Configuration
@EnableWebMvc
public class MyWebMvcConfigurer implements WebMvcConfigurer{
	 @Override
	    public void addCorsMappings(CorsRegistry registry) {
	        registry.addMapping("/api/**")
	            .allowedOrigins("*")
	            .allowedMethods("PUT", "DELETE","POST","GET")
	            .allowedHeaders("*")
	          //  .exposedHeaders("")
	            .allowCredentials(false).maxAge(Constants.maxAge);
	    }
}
