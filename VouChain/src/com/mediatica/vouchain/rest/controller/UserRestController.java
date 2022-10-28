package com.mediatica.vouchain.rest.controller;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.mediatica.vouchain.config.Constants;
import com.mediatica.vouchain.dto.AssistanceDTO;
import com.mediatica.vouchain.dto.DTOList;
import com.mediatica.vouchain.dto.FaqDTO;
import com.mediatica.vouchain.dto.PasswordDTO;
import com.mediatica.vouchain.dto.SessionTokenDTO;
import com.mediatica.vouchain.dto.SimpleResponseDTO;
import com.mediatica.vouchain.dto.UserSimpleDTO;
import com.mediatica.vouchain.entities.User;
import com.mediatica.vouchain.exceptions.PasswordNotCorrectException;
import com.mediatica.vouchain.servicesImpl.UserServiceImpl;
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
public class UserRestController {

	@Autowired
	UserServiceImpl service;

	@Value("${enable_session_management}")
	private String enableSessionManagement;

	@Value("${session_expire_time_in_second}")
	private String sessionExpireTimeInSecond;

	private static org.slf4j.Logger log = LoggerFactory.getLogger(UserRestController.class);

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

	@Value("${faq_employee}")
	private String faqEmp;

	@Value("${faq_merchant}")
	private String faqMrc;

	@Value("${assistance}")
	private String assistance;
	
	@PostMapping("/api/users/changePassword/{profile}")
	public SimpleResponseDTO userForgottenPasswordMail(@RequestBody UserSimpleDTO inputDTO,
			@PathVariable("profile") String profile) {
		String userEmail = inputDTO.getUsrEmail();
		log.info("userForgottenPasswordMail userEmail: {} profile: {}", userEmail, profile);
		SimpleResponseDTO dto = new SimpleResponseDTO();
		try {
			boolean isEmailValid = Utils.isValidEmail(userEmail);
			if (!isEmailValid) {
				log.debug("usrEmail or password not valid: " + userEmail);
				dto.setStatus(Constants.RESPONSE_STATUS.KO.toString());
				dto.setErrorDescription(msgLoginNotValid);
			} else {
				service.sendRecoveryLink(userEmail, profile);
				dto.setStatus(Constants.RESPONSE_STATUS.OK.toString());
			}
		} catch (Exception e) {
			log.error("Error in userForgottenPasswordMail", e);
			dto.setStatus(Constants.RESPONSE_STATUS.KO.toString());
			if (e.getMessage().equals(msgNoUSerFound)) {
				dto.setErrorDescription(msgNoUSerFound);
			} else {
				dto.setErrorDescription(genericError);
			}
		}

		return dto;
	}

	@PostMapping("/api/users/modifyPassword/{resetCode}")
	public SimpleResponseDTO modifyPassword(@RequestBody UserSimpleDTO inputDTO,
			@PathVariable("resetCode") String resetCode) {
		String userPassword = inputDTO.getUsrPassword();
		SimpleResponseDTO dto = new SimpleResponseDTO();
		try {
			boolean isPasswordValid = Utils.isValidPassword(userPassword);
			if (!isPasswordValid) {

				dto.setStatus(Constants.RESPONSE_STATUS.KO.toString());
				dto.setErrorDescription(msgLoginNotValid);
			} else {
				service.modifyPassword(userPassword, resetCode);
				dto.setStatus(Constants.RESPONSE_STATUS.OK.toString());
			}
		} catch (Exception e) {
			log.error("Error in userForgottenPasswordMail", e);
			dto.setStatus(Constants.RESPONSE_STATUS.KO.toString());
			if (e.getMessage().equals(msgNoUSerFound)) {
				dto.setErrorDescription(msgNoUSerFound);
			} else {
				dto.setErrorDescription(genericError);
			}
		}

		return dto;
	}

	@PostMapping("api/users/replacePassword")
	public SimpleResponseDTO replacePassword(@RequestBody PasswordDTO pwdDTO) {
		SimpleResponseDTO response = new SimpleResponseDTO();
		try {
			service.changePassword(pwdDTO);
			response.setStatus(Constants.RESPONSE_STATUS.OK.toString());
		} catch (PasswordNotCorrectException ex) {
			log.error("Error in replacePassword", ex);
			response.setStatus(Constants.RESPONSE_STATUS.KO.toString());
			response.setErrorDescription("password_not_correct");
		} catch (Exception e) {
			log.error("Error in replacePassword", e);
			response.setStatus(Constants.RESPONSE_STATUS.KO.toString());
			response.setErrorDescription(genericError);
		}

		return response;
	}

	@PostMapping("/api/session/resetSession")
	public SimpleResponseDTO resetSession(@RequestBody SessionTokenDTO token) {
		token.setForceUpdate("true");
		return manageSession(token);
	}

	@PostMapping("/api/session/verifySession")
	public SimpleResponseDTO verifySession(@RequestBody SessionTokenDTO token) {
		token.setForceUpdate("false");
		return manageSession(token);
	}

	private SimpleResponseDTO manageSession(SessionTokenDTO token) {
		SimpleResponseDTO response = new SimpleResponseDTO();
		response.setStatus(Constants.RESPONSE_STATUS.OK.toString());

		long diff = 0;
		Date now = new Date();

		if (enableSessionManagement != null && enableSessionManagement.equals("true")) {
			try {

				// retrive header parameters fo managing session
				log.info("retrive header parameters...");
				String jSessionId = token.getSessionId();
				String username = token.getUsername();
				String forceUpdate = token.getForceUpdate();

				log.info("Parameters in: ");
				log.info("Parameters jSessionId	: {}", jSessionId);
				log.info("Parameters username	: {}", username);
				log.info("Parameters forceUpdate: {}", forceUpdate);

				if (username != null && forceUpdate != null) {

					log.info("find user by email");
					User user = service.findUserByEmail(username);

					if (user != null) {
						log.info("managing session time");
						// Manage session time
						Date lastInvocationDate = user.getLastInvocationDate();
						if (lastInvocationDate != null) {
							log.info("Managing check for expired session, lastInvocationDate is: {}",
									lastInvocationDate);
							long diffInMillies = Math.abs(now.getTime() - lastInvocationDate.getTime());
							diff = TimeUnit.SECONDS.convert(diffInMillies, TimeUnit.MILLISECONDS);
							// if last invocation was made more than 30' ago
							if (sessionExpireTimeInSecond != null && diff > Long.parseLong(sessionExpireTimeInSecond)) {
								log.info("The Session is EXPIRED blanking lastInvocationDate");
								user.setUserSession(null);
								user.setLastInvocationDate(null);
								service.update(user);
								response.setErrorDescription(Constants.SESSION_EXPIRED);
								response.setStatus(Constants.RESPONSE_STATUS.KO.toString());
							}
						}

						if (user.getUserSession() == null && jSessionId != null && forceUpdate.equals("false")) {
							// LOGIN --> insert session id
							log.info("user login no session is saved ");
							user.setUserSession(jSessionId);
							user.setLastInvocationDate(now);
							service.update(user);
							log.info("Saved session: {} for user {} ", jSessionId, user.getUsrId());

						}

						else if (user.getUserSession() != null && user.getUserSession().equals(jSessionId)
								&& forceUpdate.equals("false")) {
							log.info("application invocation let's continue");
							// NAVIGATION IN SAME BROWSER WINDOW --> ok
							user.setLastInvocationDate(now);
							service.update(user);

						}

						else if (user.getUserSession() != null && !user.getUserSession().equals(jSessionId)
								&& !jSessionId.equals("") && jSessionId != null && forceUpdate.equals("false")) {

							// CHANGE BROWSER WINDOWS
							log.error("user opened new session");
							response.setErrorDescription(Constants.NON_CORRESPONDING_SESSION);
							response.setStatus(Constants.RESPONSE_STATUS.KO.toString());
							log.info("Breaking the flow");
						}

						else if (user.getUserSession() != null && forceUpdate.equals("true")
								&& (jSessionId.equals(null) || jSessionId.equals(""))) {
							// NULLING SESSION ID FOR LOGOUT
							log.info("logout");
							user.setUserSession(null);
							user.setLastInvocationDate(null);

							service.update(user);
						}

						else if (user.getUserSession() != null && !user.getUserSession().equals(jSessionId)
								&& forceUpdate.equals("true")) {
							// FORCE SESSION UPDATE
							log.info("forcing update session");
							user.setUserSession(jSessionId);
							user.setLastInvocationDate(now);
							service.update(user);

						}
						log.info("closing session");
					}
					log.debug("here");
				} else {
					response.setErrorDescription("missing_parameters_in_header");
					response.setStatus(Constants.RESPONSE_STATUS.KO.toString());
				}

			} catch (Exception e) {
				log.error("generic error", e);
				response.setErrorDescription(Constants.GENERIC_ERROR);
				response.setStatus(Constants.RESPONSE_STATUS.KO.toString());
			}
		}

		return response;

	}
	
	@PostMapping("/api/session/checkSession")
	public SimpleResponseDTO checkSession(@RequestBody SessionTokenDTO token) {
		token.setForceUpdate("false");
		return manageCheckSession(token);
	}
	
	private SimpleResponseDTO manageCheckSession(SessionTokenDTO token) {
		SimpleResponseDTO response = new SimpleResponseDTO();
		response.setStatus(Constants.RESPONSE_STATUS.OK.toString());

		long diff = 0;
		Date now = new Date();

		if (enableSessionManagement != null && enableSessionManagement.equals("true")) {
			try {

				// retrive header parameters fo managing session
				log.info("retrive header parameters...");
				String jSessionId = token.getSessionId();
				String username = token.getUsername();
				String forceUpdate = token.getForceUpdate();

				log.info("Parameters in: ");
				log.info("Parameters jSessionId	: {}", jSessionId);
				log.info("Parameters username	: {}", username);
				log.info("Parameters forceUpdate: {}", forceUpdate);

				if (username != null && forceUpdate != null) {

					log.info("find user by email");
					User user = service.findUserByEmail(username);

					if (user != null) {
						log.info("managing session time");
						// Manage session time
						Date lastInvocationDate = user.getLastInvocationDate();
						if (lastInvocationDate != null) {
							log.info("Managing check for expired session, lastInvocationDate is: {}",
									lastInvocationDate);
							long diffInMillies = Math.abs(now.getTime() - lastInvocationDate.getTime());
							diff = TimeUnit.SECONDS.convert(diffInMillies, TimeUnit.MILLISECONDS);
							// if last invocation was made more than 30' ago
							if (sessionExpireTimeInSecond != null && diff > Long.parseLong(sessionExpireTimeInSecond)) {
								log.info("The Session is EXPIRED blanking lastInvocationDate");
								response.setErrorDescription(Constants.SESSION_EXPIRED);
								response.setStatus(Constants.RESPONSE_STATUS.KO.toString());
							}
						}

						if (user.getUserSession() == null && jSessionId != null && forceUpdate.equals("false")) {
							// LOGIN --> insert session id
							response.setStatus(Constants.RESPONSE_STATUS.KO.toString());
							log.info("user login no session is saved ");
							log.info("Saved session: {} for user {} ", jSessionId, user.getUsrId());

						}

						else if (user.getUserSession() != null && user.getUserSession().equals(jSessionId)
								&& forceUpdate.equals("false")) {
							log.info("application invocation let's continue");
							// NAVIGATION IN SAME BROWSER WINDOW --> ok
						}

						else if (user.getUserSession() != null && !user.getUserSession().equals(jSessionId)
								&& !jSessionId.equals("") && jSessionId != null && forceUpdate.equals("false")) {

							// CHANGE BROWSER WINDOWS
							log.error("user opened new session");
							response.setErrorDescription(Constants.NON_CORRESPONDING_SESSION);
							response.setStatus(Constants.RESPONSE_STATUS.KO.toString());
							log.info("Breaking the flow");
						}

						else if (user.getUserSession() != null && forceUpdate.equals("true")
								&& (jSessionId.equals(null) || jSessionId.equals(""))) {
							// NULLING SESSION ID FOR LOGOUT
							log.info("logout");
						}

						else if (user.getUserSession() != null && !user.getUserSession().equals(jSessionId)
								&& forceUpdate.equals("true")) {
							// FORCE SESSION UPDATE
							log.info("forcing update session");
						}
						log.info("closing session");
					}
					log.debug("here");
				} else {
					response.setErrorDescription("missing_parameters_in_header");
					response.setStatus(Constants.RESPONSE_STATUS.KO.toString());
				}

			} catch (Exception e) {
				log.error("generic error", e);
				response.setErrorDescription(Constants.GENERIC_ERROR);
				response.setStatus(Constants.RESPONSE_STATUS.KO.toString());
			}
		}

		return response;

	}
	//Riceve in input il tipo di profilo scelto e ritorna le Faq legate a tale profilo,
	//lette dal json contenuto nella configurazione
	
	@GetMapping("/api/user/faq/{profile}")
	public DTOList<FaqDTO> faqText(@PathVariable("profile") String profile) {

		DTOList<FaqDTO> response = new DTOList<>();
		ArrayList<FaqDTO> list = new ArrayList<>();

		if (profile.equals("employee")) {
			JSONParser jsonParser = new JSONParser();

			try (FileReader reader = new FileReader(
					Constants.VOUCHAIN_HOME + Constants.JSON_TEMPLATE_SUBDIR_PATH + faqEmp,StandardCharsets.UTF_8)) {
				System.out.println(reader);
				// Read JSON file
				Object obj = jsonParser.parse(reader);

				JSONArray faqList = (JSONArray) obj;

				System.out.println(faqList);

				// Iterate over employee array
				faqList.forEach(faq -> list.add(service.parseFaqObject((JSONObject) faq)));

			} catch (FileNotFoundException e) {
				e.printStackTrace();
				response.setStatus(Constants.RESPONSE_STATUS.KO.toString());
				return response;
			} catch (IOException e) {
				e.printStackTrace();
				response.setStatus(Constants.RESPONSE_STATUS.KO.toString());
				return response;
			} catch (ParseException e) {
				e.printStackTrace();
				response.setStatus(Constants.RESPONSE_STATUS.KO.toString());
				return response;
			}

		} else {
			JSONParser jsonParser = new JSONParser();

			try (FileReader reader = new FileReader(
					Constants.VOUCHAIN_HOME + Constants.JSON_TEMPLATE_SUBDIR_PATH + faqMrc,StandardCharsets.UTF_8)) {
				// Read JSON file
				Object obj = jsonParser.parse(reader);

				JSONArray faqList = (JSONArray) obj;

				System.out.println(faqList);

				// Iterate over employee array
				faqList.forEach(faq -> list.add(service.parseFaqObject((JSONObject) faq)));

			} catch (FileNotFoundException e) {
				e.printStackTrace();
				response.setStatus(Constants.RESPONSE_STATUS.KO.toString());
				return response;
			} catch (IOException e) {
				e.printStackTrace();
				response.setStatus(Constants.RESPONSE_STATUS.KO.toString());
				return response;
			} catch (ParseException e) {
				e.printStackTrace();
				response.setStatus(Constants.RESPONSE_STATUS.KO.toString());
				return response;
			}

		}

		response.setList(list);
		response.setStatus(Constants.RESPONSE_STATUS.OK.toString());
		return response;

	}
	
	//Ritorna il testo dell'assistenza letto dal json contenuto nella configurazione
	
	@GetMapping("/api/user/assistance")
	public DTOList<AssistanceDTO> assistanceText() {
		
		DTOList<AssistanceDTO> response = new DTOList<>();
		
		ArrayList<AssistanceDTO> list = new ArrayList<>();

		JSONParser jsonParser = new JSONParser();

		try (FileReader reader = new FileReader(
				Constants.VOUCHAIN_HOME + Constants.JSON_TEMPLATE_SUBDIR_PATH + assistance,StandardCharsets.UTF_8)) {
			// Read JSON file
			Object obj = jsonParser.parse(reader);

			JSONArray assistanceList = (JSONArray) obj;

			System.out.println("STO STAMPANDO LA LISTA ----------------------------------> " + assistanceList);

			// Iterate over employee array
			assistanceList.forEach(assistance -> list.add(service.parseAssistanceObject((JSONObject) assistance)));

		} catch (FileNotFoundException e) {
			e.printStackTrace();
			response.setStatus(Constants.RESPONSE_STATUS.KO.toString());
			return response;
		} catch (IOException e) {
			e.printStackTrace();
			response.setStatus(Constants.RESPONSE_STATUS.KO.toString());
			return response;
		} catch (ParseException e) {
			e.printStackTrace();
			response.setStatus(Constants.RESPONSE_STATUS.KO.toString());
			return response;
		}
		response.setList(list);
		response.setStatus(Constants.RESPONSE_STATUS.OK.toString());
		return response;

	}
}
