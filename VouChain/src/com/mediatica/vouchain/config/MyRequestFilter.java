package com.mediatica.vouchain.config;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mediatica.vouchain.dto.ErrorResponseDTO;

//@Component
public class MyRequestFilter extends OncePerRequestFilter{

	//@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
			filterChain.doFilter(request, response);
			System.out.println("My request filter: "+response.getStatus());
			if(response.getStatus()==Constants.FORBIDDEN_CODE_401) {
		    	ObjectMapper om = new ObjectMapper();
				ErrorResponseDTO errorDTO = new ErrorResponseDTO();
		    	errorDTO.setCode(Constants.FORBIDDEN_CODE_401);
		    	errorDTO.setStatus("KO");
		    	errorDTO.setErrorDescription("Forbidden");
		        System.out.println("**************************  401 UNATHORIZED ********************************");
		       //response.sendError(Constants.FORBIDDEN_CODE_401);
		        PrintWriter writer = response.getWriter();
				writer.println(om.writeValueAsString( errorDTO)) ;
			}
	}

}
