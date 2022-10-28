package com.mediatica.vouchain.config;

import java.io.IOException;
import java.io.PrintWriter;
 
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
 
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.www.BasicAuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

//@Component
public class CustomBasicAuthenticationEntryPoint extends BasicAuthenticationEntryPoint {
 
    @Override
    public void commence(final HttpServletRequest request, 
            final HttpServletResponse response, 
            final AuthenticationException authException) throws IOException {
        //Authentication failed, send error response.
        //response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        //response.addHeader("WWW-Authenticate", "Basic realm=" + getRealmName() + "");
    	System.out.println("**************************  VALIDAZIONE ********************************");
    //	response.sendError(401, "ptttttt");

        PrintWriter writer = response.getWriter();
        writer.println("HTTP Status 401 : " + "ciao");
    }
     
//    @Override
//    public void afterPropertiesSet() {
//        setRealmName("MY_REALM");
//        super.afterPropertiesSet();
//    }
}