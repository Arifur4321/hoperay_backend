package com.mediatica.vouchain.rest.controller;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import javax.ws.rs.Produces;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.mediatica.vouchain.config.Constants;
import com.mediatica.vouchain.dto.CompanyDTO;
import com.mediatica.vouchain.exceptions.ContractNotVaidException;
import com.mediatica.vouchain.exceptions.EmailYetInTheSystemException;
import com.mediatica.vouchain.servicesImpl.CompanyServiceImpl;
import com.mediatica.vouchain.utilities.Utils;

/**
 * 
 * @author Pietro Napolitano
 *
 */
@RestController
@CrossOrigin(origins="*",allowedHeaders = "*")  
@PropertySource("file:${vouchain_home}/configurations/config.properties")
@Scope( proxyMode = ScopedProxyMode.TARGET_CLASS )
public class CompanyRestController {

	
	@Autowired
	private CompanyServiceImpl service;
	
	private static org.slf4j.Logger log = LoggerFactory.getLogger(CompanyRestController.class);

	
	@Value("${no_user_found}")
	private String msgNoUSerFound;
	
	@Value("${user_not_active}")
	private String msgUserNotActive;
	
	@Value("${login_not_validated}")
	private String msgLoginNotValid;
	
	@Value("${generic_error}")
	private String genericError;
	
	@Value("${contract_not_valid}")
	private String contractNotValid;

	
	@PostMapping("/api/companies/companyLogin")
	public CompanyDTO companyLogin(@RequestBody CompanyDTO company) {
		String usrEmail= company.getUsrEmail();
		String usrPassword= company.getUsrPassword();
		log.info("companyLogin parmeters IN: usrEmail: {}",usrEmail);
		
		CompanyDTO dto = new CompanyDTO();
		try {
			dto = (CompanyDTO) service.login(usrEmail);
			log.info("MAX MEMORY LOGIN COMPANY______"+Runtime.getRuntime().maxMemory());
			log.info("FREE MEMORY LOGIN COMPANY______"+Runtime.getRuntime().freeMemory());
			log.info("TOTAL MEMORY LOGIN COMPANY______"+Runtime.getRuntime().totalMemory());
			if(dto==null || dto.getUsrId()==null) {
				log.debug("No results found for usrEmail: {}",usrEmail );
				dto = new CompanyDTO();
				dto.setStatus(Constants.RESPONSE_STATUS.KO.toString());
				dto.setErrorDescription(msgNoUSerFound);
			}else if(dto.getUsrActive()!=null && dto.getUsrActive().contentEquals(("false"))){
				log.debug("usrEmail: {} is not active {}",usrEmail );
				dto = new CompanyDTO();
				dto.setStatus(Constants.RESPONSE_STATUS.KO.toString());
				dto.setErrorDescription(msgUserNotActive);
			}else if(!BCrypt.checkpw(usrPassword,dto.getUsrPassword())){
				log.debug("inserted password is not correct for the email: {}",usrEmail );
				dto = new CompanyDTO();
				dto.setStatus(Constants.RESPONSE_STATUS.KO.toString());
				dto.setErrorDescription(msgNoUSerFound);
				
			}else {
				dto.setStatus(Constants.RESPONSE_STATUS.OK.toString());
				dto.setUsrPassword("");
			}
		}catch(ClassCastException ex) {
			log.error("No company found for these credentials",ex);
			dto = new CompanyDTO();
			dto.setStatus(Constants.RESPONSE_STATUS.KO.toString());
			dto.setErrorDescription(msgNoUSerFound);
		}catch(Exception e) {
			log.error("Error in companyLogin",e);
			dto = new CompanyDTO();
			dto.setStatus(Constants.RESPONSE_STATUS.KO.toString());
			dto.setErrorDescription(genericError);
		}
		log.debug("end companyLogin");
		return dto;
	}
	
	
	@PostMapping("/api/companies/checkLoadSign/{usrId}")
	public CompanyDTO checkLoadSign(@PathVariable("usrId") String usrId, @RequestParam("cpyContract") MultipartFile cpyContract) {
		CompanyDTO companyDTO = new CompanyDTO();
		log.info("checkLoadSign usrId: "+usrId);
		try {
			log.debug("Analyzing the contract: "+cpyContract.getOriginalFilename());
			if(!cpyContract.getOriginalFilename().toLowerCase().endsWith(Constants.PDF_SUFFIX)&&!cpyContract.getOriginalFilename().toLowerCase().endsWith(Constants.P7M_SUFFIX)) {
				log.error("The uploaded contract file is not valid for user: {} it is not of allowed type",usrId);
				companyDTO.setStatus(Constants.RESPONSE_STATUS.KO.toString());
				companyDTO.setErrorDescription(contractNotValid);
			}else {
				companyDTO=service.checkLoadSign(usrId, cpyContract);
				companyDTO.setStatus(Constants.RESPONSE_STATUS.OK.toString());
			}
		}catch(ContractNotVaidException ex) {
			log.error("Error uploading file: ",ex);
			companyDTO.setStatus(Constants.RESPONSE_STATUS.KO.toString());
			companyDTO.setErrorDescription(contractNotValid);
		}catch(Exception e) {
			log.error("Error uploading file: ",e);
			companyDTO.setStatus(Constants.RESPONSE_STATUS.KO.toString());
			companyDTO.setErrorDescription(genericError);
		}
		return companyDTO;
	}


	@PostMapping("/api/companies/companySignUp")
	public CompanyDTO companySignUp(@RequestBody CompanyDTO company) {
		CompanyDTO dto = new CompanyDTO();
		Calendar cal = Calendar.getInstance();
		String timeString = new SimpleDateFormat("EEE yyyy.MM.dd HH:mm:ss.SSS z").format(cal.getTime());
		log.info("INIZIO REST SIGN UP COMPANY"+timeString + "ID: "+ company.getUsrId());
		log.info("companySignUp parameters IN: company: {}",company);
		try {
			boolean areLoginFieldsValid = Utils.validateLogin(company.getUsrEmail(),company.getUsrPassword());
			boolean isPecValid = Utils.isValidEmail(company.getCpyPec());
			if(!areLoginFieldsValid||!isPecValid) {
				log.debug("usrEmail or password not valid: {}",company.getUsrEmail());
				dto.setStatus(Constants.RESPONSE_STATUS.KO.toString());
				dto.setErrorDescription(msgLoginNotValid);
			}else {
				dto = (CompanyDTO) service.signUp(company);
				dto.setStatus(Constants.RESPONSE_STATUS.OK.toString());
			}
		}catch(ClassCastException ex) {
			log.error("No employee found for these credentials");
			dto.setStatus(Constants.RESPONSE_STATUS.KO.toString());
			dto.setErrorDescription(msgNoUSerFound);
		}catch(EmailYetInTheSystemException ex) {
			log.error("The inserted email is yet in the system: {}",company.getUsrEmail());
			dto.setStatus(Constants.RESPONSE_STATUS.KO.toString());
			dto.setErrorDescription(ex.getMessage());
		}catch(Exception e) {
			log.error("Error in companySignUp",e);
			dto.setStatus(Constants.RESPONSE_STATUS.KO.toString());
			dto.setErrorDescription(genericError);
		}
		cal = Calendar.getInstance();
		timeString = new SimpleDateFormat("EEE yyyy.MM.dd HH:mm:ss.SSS z").format(cal.getTime());
		log.info("FINE REST SIGN UP COMPANY"+timeString + "ID: "+ company.getUsrId());
		
		return dto;
	}
	
	@Produces("application/json")
	@GetMapping("/api/companies/showCompanyProfile/{usrId}")
	public CompanyDTO showCompanyProfile(@PathVariable("usrId") String usrIid) {
		CompanyDTO dto = new CompanyDTO();
		log.info("Getting company information about company with Id: {}",usrIid);
		try {
			dto = (CompanyDTO) service.showProfile(usrIid);
			if(dto!=null) {
				dto.setStatus(Constants.RESPONSE_STATUS.OK.toString());
			}else {
				dto=new CompanyDTO();
				dto.setStatus(Constants.RESPONSE_STATUS.KO.toString());
				dto.setErrorDescription(msgNoUSerFound);
				dto.setUsrId(usrIid);
			}
		}catch(ClassCastException ex) {
			log.error("No company found for these credentials",ex);
			dto = new CompanyDTO();
			dto.setStatus(Constants.RESPONSE_STATUS.KO.toString());
			dto.setErrorDescription(msgNoUSerFound);
		}catch(Exception e) {
			log.error("Error in showCompanyProfile",e);
			dto = new CompanyDTO();
			dto.setStatus(Constants.RESPONSE_STATUS.KO.toString());
			dto.setErrorDescription(genericError);
		}
		return dto;
	}


	@PostMapping("/api/companies/modCompanyProfile")
	public CompanyDTO modCompanyProfile(@RequestBody CompanyDTO companyDTO) {
		CompanyDTO dto = new CompanyDTO();
		try {
			//boolean areLoginFieldsValid = Utils.validateLogin(companyDTO.getUsrEmail(),companyDTO.getUsrPassword());
			boolean isPecValid = Utils.isValidEmail(companyDTO.getCpyPec());
			boolean isUserNameValid=Utils.isValidEmail(companyDTO.getUsrEmail());
			if(!isUserNameValid||!isPecValid) {
				log.debug("usrEmail or password not valid: {}",companyDTO.getUsrEmail());
				dto = new CompanyDTO();
				dto.setStatus(Constants.RESPONSE_STATUS.KO.toString());
				dto.setErrorDescription(msgLoginNotValid);
			}else {
				dto=(CompanyDTO) service.modProfile(companyDTO);
				dto.setStatus(Constants.RESPONSE_STATUS.OK.toString());
			}
		}catch(Exception e) {
			log.error("Error in modCompanyProfile",e);
			dto = new CompanyDTO();
			dto.setStatus(Constants.RESPONSE_STATUS.KO.toString());
			dto.setErrorDescription(genericError);
		}
		return dto;
	}
	
	
	
}
