package com.mediatica.vouchain.servicesImpl;

import java.util.Date;
import java.util.concurrent.TimeUnit;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mediatica.vouchain.config.Constants;
import com.mediatica.vouchain.dao.UserDaoImpl;
import com.mediatica.vouchain.dto.SessionTokenDTO;
import com.mediatica.vouchain.entities.User;
import com.mediatica.vouchain.exceptions.SessionException;



@Service
@Transactional
@PropertySource("file:${vouchain_home}/configurations/config.properties")
@Scope( proxyMode = ScopedProxyMode.TARGET_CLASS )
public class SessionServiceImpl {

	@Autowired
	UserDaoImpl userDaoImpl;
	
	private static org.slf4j.Logger log = LoggerFactory.getLogger(SessionServiceImpl.class);
	
	@Value("${session_expire_time_in_second}")
	private String sessionExpireTimeInSecond;
	
	@Value("${enable_session_management}")
	private String enableSessionManagement;
	
	public void sessionChecker(SessionTokenDTO st) throws SessionException{


		if(enableSessionManagement!=null && enableSessionManagement.equals("true")) {
			try {
				if(st!=null) {
					String jSessionId = st.getSessionId();
					String username= st.getUsername();
					String forceUpdate=st.getForceUpdate();
					
					
					log.info("Parameters in: ");
					log.info("Parameters jSessionId	: {}",jSessionId);
					log.info("Parameters username	: {}",username);
					log.info("Parameters forceUpdate: {}",forceUpdate);
					Date now = new Date();
	
					
					
					if(username!=null && !username.isEmpty() && forceUpdate!=null && !forceUpdate.isEmpty() && jSessionId!=null) {
						
						log.info("find user by email");
						User user = userDaoImpl.findUserByEmail(username);

						if(user!=null) {
							log.info("managing session time");
							//Manage session time
							Date lastInvocationDate = user.getLastInvocationDate();	
							if(lastInvocationDate!=null) {
								log.info("Managing check for expired session, lastInvocationDate is: {}",lastInvocationDate);
								long diffInMillies = Math.abs(now.getTime() - lastInvocationDate.getTime());
								long diff = TimeUnit.SECONDS.convert(diffInMillies, TimeUnit.MILLISECONDS);
								//if last invocation was made more than 30' ago
								if(sessionExpireTimeInSecond!=null && diff>Long.parseLong(sessionExpireTimeInSecond)) {	
									log.info("The Session is EXPIRED blanking lastInvocationDate");
									user.setUserSession(null);
									user.setLastInvocationDate(null);
									userDaoImpl.update(user);
									throw new Exception(""+Constants.SESSION_EXPIRED_CODE+"_"+Constants.SESSION_EXPIRED);
								}
							}				
			
			
							if(user.getUserSession()==null && jSessionId!=null && forceUpdate.equals("false")) {
								//LOGIN --> insert session id
								log.info("user login no session is saved ");
								user.setUserSession(jSessionId);
								user.setLastInvocationDate(now);
								userDaoImpl.update(user);
								log.info("Saved session: {} for user {} ",jSessionId,user.getUsrId());
			
							}			
			
							else if(user.getUserSession()!=null 
									&& user.getUserSession().equals(jSessionId)  
									&& forceUpdate.equals("false")) {
								log.info("application invocation let's continue");
								//NAVIGATION IN SAME BROWSER WINDOW --> ok
								user.setLastInvocationDate(now);
								userDaoImpl.update(user);
							}
			
							else if(user.getUserSession()!=null 
									&& !user.getUserSession().equals(jSessionId)
									&& !jSessionId.equals("")
									&& jSessionId!=null
									&& forceUpdate.equals("false")) {
			
								//CHANGE BROWSER WINDOWS
								log.error("user opened new session");
								log.info("Breaking the flow");
								throw new Exception(""+Constants.NON_CORRESPONDING_SESSION_CODE+"_"+Constants.NON_CORRESPONDING_SESSION);
			
			
							}
							
							else if(user.getUserSession()!=null && forceUpdate.equals("true") 								
									&& (jSessionId.equals(null) || jSessionId.equals(""))) {
								//NULLING SESSION ID FOR LOGOUT
								log.info("logout");
								user.setUserSession(null);
								user.setLastInvocationDate(null);
								userDaoImpl.update(user);
							}
							
							else if(user.getUserSession()!=null 
									&& !user.getUserSession().equals(jSessionId)
									&& forceUpdate.equals("true")) {
								//FORCE SESSION UPDATE
								log.info("forcing update session");
								user.setUserSession(jSessionId);
								user.setLastInvocationDate(now);
								userDaoImpl.update(user);
							}
			
							log.info("closing session");
						}else {
							throw new Exception(""+Constants.GENERIC_ERROR_CODE+"_"+"not_allowed_username"); 
						}
					}else {
						throw new Exception(""+Constants.MISSING_PARAMETERS_CODE+"_"+Constants.MISSING_PARAMETERS_); 
					}
				}else {
					throw new Exception(""+Constants.MISSING_PARAMETERS_CODE+"_"+Constants.MISSING_PARAMETERS_);
				}
			} catch (Exception e) {
				log.error("generic error",e);
				throw new SessionException(e.getMessage());
			}	
			
		}//end if enableSessionManagement
		
	}//end method
}

	

