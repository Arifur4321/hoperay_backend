package com.mediatica.vouchain.mail;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;

import com.mediatica.vouchain.config.Constants;

import freemarker.template.Configuration;

@Service
@Scope( proxyMode = ScopedProxyMode.TARGET_CLASS )
@PropertySource("file:${vouchain_home}/configurations/email.properties")
public class MailSenderServiceImpl implements MailSenderServiceInterface {

	 
	    @Autowired
	    JavaMailSender mailSender;
	 
	    @Autowired
	    Configuration fmConfiguration;
	    
	    @Value("${mail_from}")
	    private String mailFrom;
	    @Value("${mail_company_confirmation_subject}")
	    private String mailCompanyConfirmationSubject;
	    
	    @Value("${mail_employee_confirmation_subject}")
	    private String mailEmployeeConfirmationSubject;
	    
	    @Value("${mail_merchant_confirmation_subject}")
	    private String mailMerchantConfirmationSubject;
	    
	    @Value("${mail_reset_password_subject}")
	    private String mailResetPaswordSubject;

		@Value("${mail_employee_invitation_subject}")
	    private String mailEmployeeInvitationSubject;
		
	    @Value("${mail_allocation_confirm}")
	    private String mailAllocationConfirm;

		@Value("${mail_expense_confirm}")
	    private String mailExpenseConfirm;
		
		@Value("${mail_order_list}")
	    private String mailOrderList;
		
		@Value("${mail_redeem_confirm}")
	    private String mailRedeemConfirm;


		private static org.slf4j.Logger log = LoggerFactory.getLogger(MailSenderServiceImpl.class);
		
		@Override
	    public void sendEmail(Mail mail,String templateName) {
	        MimeMessage mimeMessage = mailSender.createMimeMessage(); 	        
	 
	        try {	           

	            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true);
//	            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage,
//	                    MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED,
//	                    StandardCharsets.UTF_8.name());
	        	mimeMessageHelper.setSubject(mail.getMailSubject());
	            mimeMessageHelper.setFrom(mail.getMailFrom());
	            mimeMessageHelper.setTo(mail.getMailTo());
	            mail.setMailContent(geContentFromTemplate(mail.getModel(),templateName));
	            mimeMessageHelper.setText(mail.getMailContent(), true);   
	            
	    		File img = new File(Constants.VOUCHAIN_HOME + Constants.IMAGES_SUBDIR_PATH + Constants.LOGO_FILE_NAME);
	            mimeMessageHelper.addInline("image", img);
                	            
	            log.info("logo read");
	            
	            if(mail.getAttachments()!=null && !mail.getAttachments().isEmpty()) {
	            	for(Object attach:mail.getAttachments()) {
	            		File file = (File)attach;
	            		mimeMessageHelper.addAttachment(file.getName(), file);	
	            	}
	            		
	            }
	            log.info("Sending email");
//	            mailSender.send(mimeMessageHelper.getMimeMessage());
	            Thread t = new Thread(() -> {
                    if(mailSender==null) {
                        log.warn("mail sendere is null");
                    }
                     mailSender.send(mimeMessageHelper.getMimeMessage());
                     log.info("Email sent with thread");
                });
                t.start();
	        } catch (MessagingException e) {
	            e.printStackTrace();
	        }
	    }
	    
	    @Override
		public Mail prepareMail(Map<String, Object> model,String to, String subject,List<File> attachmentsToSend) {	    	    	
			Mail mail  = new Mail();
			mail.setMailFrom(mailFrom);
			mail.setMailSubject(subject);
			mail.setMailTo(to);
	        		
			mail.setModel(model);
			if(attachmentsToSend!=null && !attachmentsToSend.isEmpty()) {
				List<Object> attachments = new ArrayList<Object>();
				for(File item:attachmentsToSend) {
					log.debug("attaching file: {}",item.getName());
					attachments.add(item);
				}
				mail.setAttachments(attachments);
			}
			return mail;
		}

	    @Override
	    public void sendSimpleMail(String to, String subject, String text) {
	    	SimpleMailMessage message = new SimpleMailMessage();
	    	message.setTo(to);
	    	message.setSubject(subject);
	    	message.setText(text);
	    	mailSender.send(message);
	    }
	    
	    
	    private String geContentFromTemplate(Map < String, Object > model,String templateName) {
	        StringBuffer content = new StringBuffer();
	 
	        try {
	            content.append(FreeMarkerTemplateUtils
	                .processTemplateIntoString(fmConfiguration.getTemplate(templateName), model));
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	        return content.toString();
	    }
  
	    

		public String getMailFrom() {
			return mailFrom;
		}

		public String getMailCompanyConfirmationSubject() {
			return mailCompanyConfirmationSubject;
		}

		public String getMailEmployeeConfirmationSubject() {
			return mailEmployeeConfirmationSubject;
		}

		public String getMailEmployeeInvitationSubject() {
			return mailEmployeeInvitationSubject;
		}

	    public String getMailMerchantConfirmationSubject() {
			return mailMerchantConfirmationSubject;
		}


		public String getMailResetPaswordSubject() {
			return mailResetPaswordSubject;
		}
		
	    
	    
		public String getMailExpenseConfirm() {
			return mailExpenseConfirm;
		}

		public void setMailExpenseConfirm(String mailExpenseConfirm) {
			this.mailExpenseConfirm = mailExpenseConfirm;
		}
		
	    
	    
	    public String getMailAllocationConfirm() {
			return mailAllocationConfirm;
		}

		public void setMailAllocationConfirm(String mailAllocationConfirm) {
			this.mailAllocationConfirm = mailAllocationConfirm;
		}



		public String getMailOrderList() {
			return mailOrderList;
		}

		public void setMailOrderList(String mailOrderList) {
			this.mailOrderList = mailOrderList;
		}
	    

		public String getMailRedeemConfirm() {
			return mailRedeemConfirm;
		}

		public void setMailRedeemConfirm(String mailRedeemConfirm) {
			this.mailRedeemConfirm = mailRedeemConfirm;
		}
	    
	 
	}	