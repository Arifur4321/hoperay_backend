package com.mediatica.vouchain.scheduler;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.mediatica.vouchain.dao.UserDaoImpl;

@Transactional
@Component
@PropertySource("file:${vouchain_home}/configurations/config.properties")
public class SessionCleanerScheduler {

	private static org.slf4j.Logger log = LoggerFactory.getLogger(SessionCleanerScheduler.class);

	@Value("${session_cleaner_enabled}")
	private String sessionCleanerEnabled;
	
	@Value("${clean_session_time_in_minutes}")
	private String cleanSessionTimeInMinutes;
	
	@Autowired
	UserDaoImpl userDaoImpl;
	
	
	@Scheduled(fixedRateString =  "${session_cleaner_scheduler_elapse_time}")
	public void sessionCleanerScheduler() {
		if(sessionCleanerEnabled.equalsIgnoreCase("true")) {	
			userDaoImpl.cleanSessions(cleanSessionTimeInMinutes);
		}
	}
	
}
