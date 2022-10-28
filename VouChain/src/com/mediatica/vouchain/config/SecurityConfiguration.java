package com.mediatica.vouchain.config;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.context.SecurityContextPersistenceFilter;

import com.mediatica.vouchain.dao.UserDaoImpl;
import com.mediatica.vouchain.utilities.EncrptingUtils;

/**
 * 
 * @author Pietro Napolitano
 *
 */
@Configuration
@EnableWebSecurity
@PropertySource("file:${vouchain_home}/configurations/config.properties")
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {
 
    
	
	@Autowired
    PasswordEncoder passwordEncoder;
    
//    @Autowired
//    CustomBasicAuthenticationEntryPoint entryPoint;
    
    @Autowired
    MyAccessDeniedHandler accessHandler;
    
    @Autowired
    CustomAuthenticationFailureHandler authHandler;
    
	@Autowired
    private EncrptingUtils encrptingUtils;
	
	@Autowired
	UserDaoImpl userDaoImpl;
    
//    @Autowired
//    MyRequestFilter myReqFilter;
    
	@Value("${company_auth_username}")
	private String companyAuthUsername;
	
	@Value("${company_auth_password}")
	private String companyAuthPassword;

 	@Value("${employee_auth_username}")
	private String employeeAuthUsername;
	
	@Value("${employee_auth_password}")
	private String employeeAuthPassword;

	@Value("${merchant_auth_username}")
	private String merchantAuthUsername;
	
	@Value("${merchant_auth_password}")
	private String merchantAuthPassword;
	
	@Value("${generic_user_auth_username}")
	private String genericUserAuthUsername;

	@Value("${generic_user_auth_password}")
	private String genericUserAuthPassword;
	
	@Value("${qrcode_auth_username}")
	private String qrcodeAuthUsername;
	
	@Value("${qrcode_auth_password}")
	private String qrcodeAuthPassword;

	@Value("${voucher_auth_username}")
	private String voucherAuthUsername;

	@Value("${voucher_auth_password}")
	private String voucherAuthPassword;
	
	@Value("${transaction_auth_username}")
	private String transactionAuthUsername;

	@Value("${transaction_auth_password}")
	private String transactionAuthPassword;	
	
//	@Value("${enable_session_management}")
//	private String enableSessionManagement;
//	
//	@Value("${session_expire_time_in_second}")
//	private String sessionExpireTimeInSecond;

	

   
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
    	return super.authenticationManagerBean();
    }

    
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {

        auth.inMemoryAuthentication()
        .passwordEncoder(passwordEncoder)
        .withUser(companyAuthUsername).password(passwordEncoder.encode(encrptingUtils.decrypt(companyAuthPassword.trim()))).roles("COMPANY")
        .and()
        .withUser(employeeAuthUsername).password(passwordEncoder.encode(encrptingUtils.decrypt(employeeAuthPassword.trim()))).roles("EMPLOYEE")
        .and()
        .withUser(merchantAuthUsername).password(passwordEncoder.encode(encrptingUtils.decrypt(merchantAuthPassword.trim()))).roles("MERCHANT")
        .and()
        .withUser(genericUserAuthUsername).password(passwordEncoder.encode(encrptingUtils.decrypt(genericUserAuthPassword.trim()))).roles("USER")
        .and()
        .withUser(voucherAuthUsername).password(passwordEncoder.encode(encrptingUtils.decrypt(voucherAuthPassword.trim()))).roles("VOUCHER")
        .and()
        .withUser(transactionAuthUsername).password(passwordEncoder.encode(encrptingUtils.decrypt(transactionAuthPassword.trim()))).roles("TRANSACTION")
        .and()
        .withUser(qrcodeAuthUsername).password(passwordEncoder.encode(encrptingUtils.decrypt(qrcodeAuthPassword.trim()))).roles("QRCODE")
        ;
    }
 
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
 
    @Override
    protected void configure(HttpSecurity http) throws Exception {
    	http.cors(); 
    	http.headers().httpStrictTransportSecurity().disable();
    	http
    	//.addFilterBefore(
    	  //        new SessionFilter(userDaoImpl, enableSessionManagement, sessionExpireTimeInSecond), SecurityContextPersistenceFilter.class) //**** changed for session
        //.addFilterBefore(basicFilter(), GenericFilterBean.class)
        .authorizeRequests()
        
        .antMatchers("/login").permitAll()
        .antMatchers("/api/transactions/**").hasRole("TRANSACTION") 
        .antMatchers("/api/companies/**").hasRole("COMPANY")
        .antMatchers("/api/employees/**").hasRole("EMPLOYEE")
        .antMatchers("/api/merchants/**").hasRole("MERCHANT")
        .antMatchers("/api/vouchers/**").hasRole("VOUCHER")
        .antMatchers("/api/users/**").hasRole("USER")
        .antMatchers("/api/qrcode/**").hasRole("QRCODE")
        //.antMatchers("/**").hasAnyRole("COMPANY", "EMPLOYEE","MERCHANT","USER","VOUCHER")
        .antMatchers("/**").hasAnyRole("COMPANY", "EMPLOYEE","MERCHANT","USER","VOUCHER", "TRANSACTION","QRCODE")
        					.and().exceptionHandling()
      //  					.authenticationEntryPoint(entryPoint)
        					.accessDeniedHandler(accessHandler)
								
    //    .and().addFilterBefore(basicFilter(), GenericFilterBean.class)
        .and().formLogin()
        .failureHandler(authHandler)
        .and().httpBasic() //********
        .and().logout().logoutSuccessUrl("/login").permitAll()
        .and().csrf().disable();
        
       // http.addFilterBefore(myReqFilter, UsernamePasswordAuthenticationFilter.class)
        ;
       
    }
}