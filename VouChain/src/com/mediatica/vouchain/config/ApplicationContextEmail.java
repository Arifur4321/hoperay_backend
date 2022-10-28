package com.mediatica.vouchain.config;

import java.util.Properties;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.ui.freemarker.FreeMarkerConfigurationFactoryBean;

import com.mediatica.vouchain.utilities.EncrptingUtils;

/**
 * 
 * @author Pietro Napolitano
 *
 */
@Configuration
@PropertySource("file:${vouchain_home}/configurations/email.properties")
public class ApplicationContextEmail {

	@Autowired
	private Environment environment;

	@Autowired
	private EncrptingUtils encrptingUtils;

	@Bean
	public JavaMailSender getJavaMailSender() {
		JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
		//System.setProperty("mail.mime.charset", "utf8");
		//System.setProperty("file.encoding","UTF-8");
		try {
			mailSender.setHost(environment.getRequiredProperty("host"));
			Integer port = Constants.MAIL_SENDER_PORT;
			try {
				port=Integer.valueOf(environment.getRequiredProperty("mail.smtp.port"));
			}catch(Exception e) {}
			mailSender.setPort(port);
			mailSender.setUsername(environment.getRequiredProperty("usrname"));
			mailSender.setPassword(encrptingUtils.decrypt(environment.getRequiredProperty("password").trim()));
			mailSender.setDefaultEncoding("UTF-8");
			Properties props = mailSender.getJavaMailProperties();
			props.put("mail.smtp.port", environment.getRequiredProperty("mail.smtp.port"));
			props.put("mail.smtp.auth", environment.getRequiredProperty("mail.smtp.auth"));
			if (port.equals(465)) {
				props.put("mail.smtp.ssl.enable", environment.getRequiredProperty("mail.smtp.ssl.enable"));
			} else if (port.equals(587)) {
				props.put("mail.smtp.starttls.enable", environment.getRequiredProperty("mail.smtp.starttls.enable"));
			}
//			props.put("mail.smtp.starttls.enable", environment.getRequiredProperty("mail.smtp.starttls.enable"));
			props.put("mail.smtp.ssl.trust",environment.getRequiredProperty("mail.smtp.ssl.trust"));
			//props.put("mail.transport.protocol", environment.getRequiredProperty("mail.transport.protocol"));
			props.put("mail.debug",environment.getRequiredProperty("mail.debug"));

		} catch (IllegalStateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}


		return mailSender;
	}

	@Bean
	public FreeMarkerConfigurationFactoryBean getFreeMarkerConfiguration() {
		FreeMarkerConfigurationFactoryBean fmConfigFactoryBean = new FreeMarkerConfigurationFactoryBean();
		fmConfigFactoryBean
				.setTemplateLoaderPath("file:" + Constants.VOUCHAIN_HOME + Constants.MAIL_TEMPLATE_SUBDIR_PATH);
		return fmConfigFactoryBean;
	}

}
