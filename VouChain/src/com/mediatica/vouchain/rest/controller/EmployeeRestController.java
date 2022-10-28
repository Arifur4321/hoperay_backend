package com.mediatica.vouchain.rest.controller;

import java.util.ArrayList;
import java.util.List;

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
import org.springframework.web.bind.annotation.RestController;

import com.mediatica.vouchain.config.Constants;
import com.mediatica.vouchain.dto.DTOList;
import com.mediatica.vouchain.dto.EmployeeDTO;
import com.mediatica.vouchain.exceptions.EmailYetInTheSystemException;
import com.mediatica.vouchain.servicesImpl.EmployeeServiceImpl;
import com.mediatica.vouchain.utilities.Utils;

/**
 * 
 * @author Pietro Napolitano
 *
 */
@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
@PropertySource("file:${vouchain_home}/configurations/config.properties")
@Scope(proxyMode = ScopedProxyMode.TARGET_CLASS)
public class EmployeeRestController {

	@Autowired
	private EmployeeServiceImpl service;

	private static org.slf4j.Logger log = LoggerFactory.getLogger(EmployeeRestController.class);

	@Value("${no_user_found}")
	private String msgNoUSerFound;

	@Value("${user_not_active}")
	private String msgUserNotActive;

	@Value("${login_not_validated}")
	private String msgLoginNotValid;

	@Value("${generic_error}")
	private String genericError;

	// BE_13 Effettua il login all’utente Beneficiario,
	// sulla base delle credenziali fornite, confrontandole con quelle memorizzate
	// nel DB.
	// Modificata per gestire le modalità prescelte di accesso all' App,
	// ed eventualmente il PIN.

	@PostMapping("/api/employees/employeeLogin")
	public EmployeeDTO employeeLogin(@RequestBody EmployeeDTO employeeDTO) {
		String usrEmail = employeeDTO.getUsrEmail();
		String usrPassword = employeeDTO.getUsrPassword();
		log.info("employeeLogin parmeters IN: usrEmail: {}", usrEmail);

		EmployeeDTO dto = new EmployeeDTO();
		try {
			dto = (EmployeeDTO) service.login(usrEmail);
			if (dto == null || dto.getUsrId() == null) {
				log.debug("No results found for usrEmail: {}", usrEmail);
				dto = new EmployeeDTO();
				dto.setStatus(Constants.RESPONSE_STATUS.KO.toString());
				dto.setErrorDescription(msgNoUSerFound);
			} else if (dto.getUsrActive() != null && dto.getUsrActive().contentEquals(("false"))) {
				log.debug("usrEmail: {} is not active", usrEmail);
				dto = new EmployeeDTO();
				dto.setStatus(Constants.RESPONSE_STATUS.KO.toString());
				dto.setErrorDescription(msgUserNotActive);
			} else if (!BCrypt.checkpw(usrPassword, dto.getUsrPassword())) {
				log.debug("inserted password is not correct for the email: {}", usrEmail);
				dto = new EmployeeDTO();
				dto.setStatus(Constants.RESPONSE_STATUS.KO.toString());
				dto.setErrorDescription(msgNoUSerFound);
			} else {
				dto.setStatus(Constants.RESPONSE_STATUS.OK.toString());
				dto.setUsrPassword("");
			}
		} catch (ClassCastException ex) {
			log.error("No employee found for these credentials");
			dto = new EmployeeDTO();
			dto.setStatus(Constants.RESPONSE_STATUS.KO.toString());
			dto.setErrorDescription(msgNoUSerFound);
		} catch (Exception e) {
			log.error("Error in employeeLogin", e);
			dto = new EmployeeDTO();
			dto.setStatus(Constants.RESPONSE_STATUS.KO.toString());
			dto.setErrorDescription(genericError);
		}
		log.debug("end employeeLogin");
		return dto;

	}

	// BE_07 mostra le informazioni del profilo del beneficiario cercandole nel DB.
	// Modificata per fornire le modalità di accesso all' App

	@GetMapping("/api/employees/showEmployeeProfile/{usrId}")
	public EmployeeDTO showEmployeeProfile(@PathVariable("usrId") String usrId) {
		EmployeeDTO dto = new EmployeeDTO();
		log.info("Getting employee information about employee with Id: {}", usrId);
		try {
			dto = (EmployeeDTO) service.showProfile(usrId);
			if (dto != null) {
				dto.setStatus(Constants.RESPONSE_STATUS.OK.toString());
			} else {
				dto = new EmployeeDTO();
				dto.setStatus(Constants.RESPONSE_STATUS.KO.toString());
				dto.setErrorDescription(msgNoUSerFound);
				dto.setUsrId(usrId);
			}
		} catch (ClassCastException ex) {
			log.error("No employee found for these credentials", ex);
			dto = new EmployeeDTO();
			dto.setStatus(Constants.RESPONSE_STATUS.KO.toString());
			dto.setErrorDescription(msgNoUSerFound);
		} catch (Exception e) {
			log.error("Error in showEmployeeProfile", e);
			dto = new EmployeeDTO();
			dto.setStatus(Constants.RESPONSE_STATUS.KO.toString());
			dto.setErrorDescription(genericError);
		}
		return dto;
	}

	@PostMapping("/api/employees/employSignUp")
	public DTOList<EmployeeDTO> employeeSignUp(@RequestBody List<EmployeeDTO> employeeList) {
		DTOList<EmployeeDTO> dto = new DTOList<EmployeeDTO>();
		List<EmployeeDTO> list = new ArrayList<EmployeeDTO>();
		log.info("employeeSignUp parameters IN: employeeList size: {}", employeeList.size());
		try {

			list = service.insertEmployeesList(employeeList);
			service.sendMail(list);
			dto.setList(list);
			dto.setStatus(Constants.RESPONSE_STATUS.OK.toString());
		} catch (EmailYetInTheSystemException ex) {
			log.warn("One or more of the inserted email is yet in the system");
			dto = new DTOList<EmployeeDTO>();
			dto.setStatus(Constants.RESPONSE_STATUS.KO.toString());
			dto.setErrorDescription(ex.getMessage());
		} catch (Exception e) {
			log.error("Error in employeeSignUp", e);
			dto = new DTOList<EmployeeDTO>();
			dto.setStatus(Constants.RESPONSE_STATUS.KO.toString());
			dto.setErrorDescription(genericError);
		}
		return dto;
	}

	@GetMapping("/api/employees/showEmployeesList/{companyId}")
	public DTOList<EmployeeDTO> showEmployeesList(@PathVariable("companyId") String companyId) {
		DTOList<EmployeeDTO> dto = new DTOList<EmployeeDTO>();
		List<EmployeeDTO> list = null;
		log.info("Extracting employeesList for company: {}", companyId);
		try {
			list = service.getEmployeeList(companyId);
			if (list != null && !list.isEmpty()) {
				dto.setList(list);
				dto.setStatus(Constants.RESPONSE_STATUS.OK.toString());
			} else {
				dto.setStatus(Constants.RESPONSE_STATUS.OK.toString());
				dto.setList(list);
			}

		} catch (Exception e) {
			log.error("Error in showEmployeesList", e);
			dto = new DTOList<EmployeeDTO>();
			dto.setStatus(Constants.RESPONSE_STATUS.KO.toString());
			dto.setErrorDescription(genericError);
		}
		return dto;
	}

	@GetMapping("/api/employees/checkInvitationCode/{invitationCode}")
	public EmployeeDTO checkInvitationCode(@PathVariable("invitationCode") String invitationCode) {
		EmployeeDTO dto = new EmployeeDTO();

		log.info("checkInvitationCode: {}", invitationCode);
		try {
			dto = service.checkInvitationCode(invitationCode);
			if (dto != null) {
				dto.setStatus(Constants.RESPONSE_STATUS.OK.toString());
			} else {
				dto = new EmployeeDTO();
				dto.setStatus(Constants.RESPONSE_STATUS.KO.toString());
				dto.setErrorDescription(msgNoUSerFound);
			}
		} catch (Exception e) {
			log.error("Error in checkInvitationCode", e);
			dto = new EmployeeDTO();
			dto.setStatus(Constants.RESPONSE_STATUS.KO.toString());
			dto.setErrorDescription(genericError);
		}
		return dto;
	}

	// BE_08 modifica le informazioni del beneficiario aggiornandole nel DB
	// modificata per aggiungere le modalità di accesso all' App.

	@PostMapping("/api/employees/modEmployeeProfile")
	public EmployeeDTO modEmployeeProfile(@RequestBody EmployeeDTO employeeDTO) {
		EmployeeDTO dto = new EmployeeDTO();
		try {

			boolean isEmailValid = Utils.isValidEmail(employeeDTO.getUsrEmail());
			if (!isEmailValid) {
				log.debug("usrEmail not valid: {}", employeeDTO.getUsrEmail());
				dto = new EmployeeDTO();
				dto.setStatus(Constants.RESPONSE_STATUS.KO.toString());
				dto.setErrorDescription(msgLoginNotValid);
			} else {
				dto = (EmployeeDTO) service.modProfile(employeeDTO);
				dto.setStatus(Constants.RESPONSE_STATUS.OK.toString());
			}
		} catch (Exception e) {
			log.error("Error in modEmployeeProfile", e);
			dto = new EmployeeDTO();
			dto.setStatus(Constants.RESPONSE_STATUS.KO.toString());
			if (e.getMessage().equals(msgNoUSerFound)) {
				dto.setErrorDescription(msgNoUSerFound);
			} else {
				dto.setErrorDescription(genericError);
			}
		}
		return dto;
	}

	@PostMapping("/api/employees/employeeConfirmation")
	public EmployeeDTO employeeConfirmation(@RequestBody EmployeeDTO employeeDTO) {
		EmployeeDTO dto = new EmployeeDTO();
		try {
			boolean areLoginFieldsValid = Utils.validateLogin(employeeDTO.getUsrEmail(), employeeDTO.getUsrPassword());
			boolean isEmailValid = Utils.isValidEmail(employeeDTO.getUsrEmail());
			if (!areLoginFieldsValid || !isEmailValid) {
				log.debug("usrEmail or password not valid: {}", employeeDTO.getUsrEmail());
				dto = new EmployeeDTO();
				dto.setStatus(Constants.RESPONSE_STATUS.KO.toString());
				dto.setErrorDescription(msgLoginNotValid);
			} else {
				dto = (EmployeeDTO) service.confirm(employeeDTO);
				dto.setStatus(Constants.RESPONSE_STATUS.OK.toString());
			}
		} catch (ClassCastException ex) {
			log.error("No employee found for these credentials", ex);
			dto = new EmployeeDTO();
			dto.setStatus(Constants.RESPONSE_STATUS.KO.toString());
			dto.setErrorDescription(msgNoUSerFound);
		} catch (Exception e) {
			log.error("Error in modEmployeeProfile", e);
			dto = new EmployeeDTO();
			dto.setStatus(Constants.RESPONSE_STATUS.KO.toString());
			if (e.getMessage().equals(msgNoUSerFound)) {
				dto.setErrorDescription(msgNoUSerFound);
			} else {
				dto.setErrorDescription(genericError);
			}
		}
		return dto;
	}

}
