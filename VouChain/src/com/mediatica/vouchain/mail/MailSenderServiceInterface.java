package com.mediatica.vouchain.mail;

import java.io.File;
import java.util.List;
import java.util.Map;

/**
 * 
 * @author Pietro Napolitano
 *
 */
public interface MailSenderServiceInterface {
	/**
	 * Send an email with the given ftl template
	 * 
	 * @param mail
	 * @param templateName
	 */
	public void sendEmail(Mail mail, String templateName);

	/**
	 * 
	 * prepares the mail to send
	 * 
	 * @param model
	 * @param to
	 * @param subject
	 * @param attachment
	 * @return
	 */
	public Mail prepareMail(Map<String, Object> model, String to, String subject, List<File> attachments);

	/**
	 * sends a simple email
	 * 
	 * @param to
	 * @param subject
	 * @param text
	 */
	public void sendSimpleMail(String to, String subject, String text);

}
