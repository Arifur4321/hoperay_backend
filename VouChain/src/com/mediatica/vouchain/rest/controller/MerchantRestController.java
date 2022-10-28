package com.mediatica.vouchain.rest.controller;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

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
import com.mediatica.vouchain.dto.LocalizationDTO;
import com.mediatica.vouchain.dto.MerchantDTO;
import com.mediatica.vouchain.exceptions.EmailYetInTheSystemException;
import com.mediatica.vouchain.servicesImpl.GeographicServiceImpl;
import com.mediatica.vouchain.servicesImpl.MerchantServiceImpl;
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
public class MerchantRestController {

	@Autowired
	private MerchantServiceImpl service;

	private static org.slf4j.Logger log = LoggerFactory.getLogger(MerchantRestController.class);

	@Value("${no_user_found}")
	private String msgNoUSerFound;

	@Value("${user_not_active}")
	private String msgUserNotActive;

	@Value("${login_not_validated}")
	private String msgLoginNotValid;

	@Value("${generic_error}")
	private String genericError;

	@Autowired
	GeographicServiceImpl geographicServiceImpl;

	@PostMapping("/api/merchants/merchantLogin")
	public MerchantDTO merchantLogin(@RequestBody MerchantDTO merchantDTO) {
		String usrEmail = merchantDTO.getUsrEmail();
		String usrPassword = merchantDTO.getUsrPassword();
		log.info("merchantLogin parmeters IN: usrEmail: {}", usrEmail);

		MerchantDTO dto = new MerchantDTO();
		try {
			dto = (MerchantDTO) service.login(usrEmail);
			if (dto == null || dto.getUsrId() == null) {
				log.debug("No results found for usrEmail: {}", usrEmail);
				dto = new MerchantDTO();
				dto.setStatus(Constants.RESPONSE_STATUS.KO.toString());
				dto.setErrorDescription(msgNoUSerFound);
			} else if (dto.getUsrActive() != null && dto.getUsrActive().contentEquals(("false"))) {
				log.debug("usrEmail: {} is not active", usrEmail);
				dto = new MerchantDTO();
				dto.setStatus(Constants.RESPONSE_STATUS.KO.toString());
				dto.setErrorDescription(msgUserNotActive);
			} else if (!BCrypt.checkpw(usrPassword, dto.getUsrPassword())) {
				log.debug("inserted password is not correct for the email: {}", usrEmail);
				dto = new MerchantDTO();
				dto.setStatus(Constants.RESPONSE_STATUS.KO.toString());
				dto.setErrorDescription(msgNoUSerFound);
			} else {
				dto.setStatus(Constants.RESPONSE_STATUS.OK.toString());
				dto.setUsrPassword("");
			}
		} catch (ClassCastException ex) {
			log.error("No merchant found for these credentials");
			dto = new MerchantDTO();
			dto.setStatus(Constants.RESPONSE_STATUS.KO.toString());
			dto.setErrorDescription(msgNoUSerFound);
		} catch (Exception e) {
			log.error("Error in merchantLogin", e);
			dto = new MerchantDTO();
			dto.setStatus(Constants.RESPONSE_STATUS.KO.toString());
			dto.setErrorDescription(genericError);
		}
		log.debug("end merchantLogin");
		return dto;

	}

	// BE_09 mostra le informazioni del profilo dell’Affiliato cercandole nel DB.
	// Modificata per fornire le informazioni relative al metodo di autenticazione,
	// l’immagine di profilo ed il link alla mappa di georeferenziazione

	@GetMapping("/api/merchants/showMerchantProfile/{usrId}")
	public MerchantDTO showMerchantProfile(@PathVariable("usrId") String usrId) {
		MerchantDTO dto = new MerchantDTO();
		log.info("Getting merchant information about merchant with Id: {}", usrId);
		try {
			dto = (MerchantDTO) service.showProfile(usrId);

			if (dto != null) {
				dto.setStatus(Constants.RESPONSE_STATUS.OK.toString());
			} else {
				dto = new MerchantDTO();
				dto.setStatus(Constants.RESPONSE_STATUS.KO.toString());
				dto.setErrorDescription(msgNoUSerFound);
				dto.setUsrId(usrId);
			}
		} catch (ClassCastException ex) {
			log.error("No merchant found for these credentials", ex);
			dto = new MerchantDTO();
			dto.setStatus(Constants.RESPONSE_STATUS.KO.toString());
			dto.setErrorDescription(msgNoUSerFound);
		} catch (Exception e) {
			log.error("Error in showMerchantProfile", e);
			dto = new MerchantDTO();
			dto.setStatus(Constants.RESPONSE_STATUS.KO.toString());
			dto.setErrorDescription(genericError);
		}
		return dto;
	}

	@PostMapping("/api/merchants/merchantSignUp")
	public MerchantDTO merchantSignUp(@RequestBody MerchantDTO merchant) {
		MerchantDTO dto = new MerchantDTO();
		log.info("merchantSignUp parameters IN: merchant: {}", merchant);
		try {
			boolean areLoginFieldsValid = Utils.validateLogin(merchant.getUsrEmail(), merchant.getUsrPassword());

			if (!areLoginFieldsValid) {
				log.debug("usrEmail or password not valid: {}", merchant.getUsrEmail());
				dto = new MerchantDTO();
				dto.setStatus(Constants.RESPONSE_STATUS.KO.toString());
				dto.setErrorDescription(msgLoginNotValid);
			} else {
				dto = (MerchantDTO) service.signUp(merchant);
				dto.setStatus(Constants.RESPONSE_STATUS.OK.toString());
			}
		} catch (EmailYetInTheSystemException ex) {
			log.error("The inserted email is yet in the system: {}", merchant.getUsrEmail());
			dto = new MerchantDTO();
			dto.setStatus(Constants.RESPONSE_STATUS.KO.toString());
			dto.setErrorDescription(ex.getMessage());
		} catch (Exception e) {
			log.error("Error in merchantSignUp", e);
			dto = new MerchantDTO();
			dto.setStatus(Constants.RESPONSE_STATUS.KO.toString());
			dto.setErrorDescription(genericError);
		}
		return dto;
	}

	// BE_15 Mostra la lista degli affiliati censiti.
	// modificata per rendere disponibile l’immagine del profilo ed il link alla
	// mappa di georeferenziazione

	@GetMapping("/api/merchants/showMerchantsList")
	public DTOList<MerchantDTO> showMerchantsList() {
		DTOList<MerchantDTO> dto = new DTOList<MerchantDTO>();
		List<MerchantDTO> list = null;
		log.info("Extracting merchantsList ");
		try {
			list = service.getMerchantsList();
			if (list != null && !list.isEmpty()) {

				dto.setList(list);
				dto.setStatus(Constants.RESPONSE_STATUS.OK.toString());
			} else {
				dto.setStatus(Constants.RESPONSE_STATUS.KO.toString());
				dto.setErrorDescription(msgNoUSerFound);
			}

		} catch (Exception e) {
			log.error("Error in showEmployeesList", e);
			dto = new DTOList<MerchantDTO>();
			dto.setStatus(Constants.RESPONSE_STATUS.KO.toString());
			dto.setErrorDescription(genericError);
		}
		return dto;
	}

	@PostMapping("/api/merchants/modMerchantProfile")
	public MerchantDTO modMerchantProfile(@RequestBody MerchantDTO merchantDTO) {
		MerchantDTO dto = new MerchantDTO();
		try {
			boolean isEmailValid = Utils.isValidEmail(merchantDTO.getUsrEmail());
			if (!isEmailValid) {
				log.debug("usrEmail or password not valid: {}", merchantDTO.getUsrEmail());
				dto = new MerchantDTO();
				dto.setStatus(Constants.RESPONSE_STATUS.KO.toString());
				dto.setErrorDescription(msgLoginNotValid);
			} else {
				dto = (MerchantDTO) service.modProfile(merchantDTO);
				dto.setStatus(Constants.RESPONSE_STATUS.OK.toString());
			}
		} catch (Exception e) {
			log.error("Error in modMerchantProfile", e);
			dto = new MerchantDTO();
			dto.setStatus(Constants.RESPONSE_STATUS.KO.toString());
			if (e.getMessage().equals(msgNoUSerFound)) {
				dto.setErrorDescription(msgNoUSerFound);
			} else {
				dto.setErrorDescription(genericError);
			}
		}
		return dto;
	}

	// BE_59 Partendo dalle coordinate fornite dall’applicazione
	// e dal raggio prescelto dall’utente,
	// esegue una ricerca di prossimità sul DB

	@PostMapping("/api/merchants/searchMerchantGIS")
	public DTOList<MerchantDTO> searchMerchantsGIS(@RequestBody LocalizationDTO localizationDTO) {
		DTOList<MerchantDTO> dto = new DTOList<MerchantDTO>();
		HashMap<MerchantDTO, String> map = null;
		List<MerchantDTO> list = new ArrayList<MerchantDTO>();
		log.info("Extracting merchantsList ");
		try {
			map = service.searchMerchantGIS(localizationDTO);
			if (map != null && !map.isEmpty()) {
				for (MerchantDTO merchant : map.keySet()) {
					merchant.setMrcDistanza(map.get(merchant));
					list.add(merchant);
					Comparator<MerchantDTO> compareByDistance = (MerchantDTO o1, MerchantDTO o2) -> Double.valueOf(o1.getMrcDistanza())
							.compareTo(Double.valueOf(o2.getMrcDistanza()));
					Collections.sort(list, compareByDistance);
				}
				if (list != null && !list.isEmpty()) {

					dto.setList(list);
					dto.setStatus(Constants.RESPONSE_STATUS.OK.toString());
				} else {
					dto.setStatus(Constants.RESPONSE_STATUS.KO.toString());
					dto.setErrorDescription(msgNoUSerFound);
				}
			} else {
				dto.setStatus(Constants.RESPONSE_STATUS.KO.toString());
				dto.setErrorDescription(msgNoUSerFound);
			}
		} catch (Exception e) {
			log.error("Error in searchMerchantGIS", e);
			dto = new DTOList<MerchantDTO>();
			dto.setStatus(Constants.RESPONSE_STATUS.KO.toString());
			dto.setErrorDescription(genericError);
		}
		return dto;
	}
}
