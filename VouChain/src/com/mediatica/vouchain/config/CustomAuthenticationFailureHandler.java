package com.mediatica.vouchain.config;

import java.io.IOException;
import java.util.Calendar;
import java.util.HashMap;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * 
 * @author Pietro Napolitano
 *
 */
@Component
public class CustomAuthenticationFailureHandler 
extends SimpleUrlAuthenticationFailureHandler  {

  private ObjectMapper objectMapper = new ObjectMapper();

  @Override
  public void onAuthenticationFailure(
    HttpServletRequest request,
    HttpServletResponse response,
    AuthenticationException exception) 
    throws IOException, ServletException {
	  System.out.println("CUSTOM AUTHENTICATION FAILURE");
      response.setStatus(HttpStatus.UNAUTHORIZED.value());
      response.getOutputStream()
        .println(objectMapper.writeValueAsString("401 UNAUTHORIZED"));
  }
}