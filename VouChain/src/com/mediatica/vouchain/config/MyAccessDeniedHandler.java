package com.mediatica.vouchain.config;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mediatica.vouchain.dto.ErrorResponseDTO;

/**
 * 
 * @author Pietro Napolitano
 *
 */
@Component
public class MyAccessDeniedHandler implements AccessDeniedHandler {

	@Override
	public void handle(HttpServletRequest request, HttpServletResponse response,
			AccessDeniedException accessDeniedException) throws IOException, ServletException {
    	ObjectMapper om = new ObjectMapper();
    	ErrorResponseDTO errorDTO = new ErrorResponseDTO();
    	errorDTO.setCode(Constants.FORBIDDEN_CODE_403);
    	errorDTO.setStatus("KO");
    	errorDTO.setErrorDescription("Forbidden");
        System.out.println("**************************  403 FORBIDDEN ********************************");
        PrintWriter writer = response.getWriter();
		writer.println(om.writeValueAsString( errorDTO)) ;
	}

}
