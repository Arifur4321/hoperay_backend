package com.mediatica.vouchain.servicesImpl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mediatica.vouchain.config.Constants;
import com.mediatica.vouchain.dto.AssistanceDTO;
import com.mediatica.vouchain.dto.ContactDTO;
import com.mediatica.vouchain.dto.DTO;
import com.mediatica.vouchain.dto.FaqDTO;
import com.mediatica.vouchain.entities.User;
import com.mediatica.vouchain.exceptions.EmailYetInTheSystemException;
import com.mediatica.vouchain.mail.Mail;
import com.mediatica.vouchain.mail.MailSenderServiceImpl;
import com.mediatica.vouchain.utilities.Utils;


/**
 * 
 * @author Pietro Napolitano
 *
 */


@Service
@Transactional
@PropertySource("file:${vouchain_home}/configurations/email.properties")
@Scope( proxyMode = ScopedProxyMode.TARGET_CLASS )
public class UserServiceImpl extends UserServiceAbstract{

	@Autowired 
	MailSenderServiceImpl mailSenderService;
	
	@Value("${reset_password_link_path}")
	private String resetPasswordLink;
	
	private static org.slf4j.Logger log = LoggerFactory.getLogger(UserServiceImpl.class);
	
	public void sendRecoveryLink(String userEmail, String profile) throws Exception {
		log.debug("sending recovery link for user: {} with profile {}",userEmail,profile);
		User user=userDao.findUserByEmail(userEmail);
		boolean checkProfile = checkProfile(user,profile);
		if(user!=null && profile!=null && !profile.isEmpty() && checkProfile) {
			String recoverEmailCode=Utils.generateAlphaNumericString();
			user.setUsrRecoverEmailCode(recoverEmailCode);
			userDao.update(user);
			log.debug("updated user: {} with profile {}",userEmail,profile);
			Map<String, Object> model = new HashMap<String, Object>();
			model.put("recoverEmailCode", resetPasswordLink+recoverEmailCode);
			Mail mail  = mailSenderService.prepareMail(model,userEmail,mailSenderService.getMailResetPaswordSubject(),null);
			mailSenderService.sendEmail(mail, Constants.RESET_PASSWORD_TEMPLATE);
		}else {
			throw new Exception("no_user_found");
		}
	}
	
	public void modifyPassword(String userPassword, String resetCode) throws Exception {
		log.debug("modifying password for reset code: {}",resetCode);
		User user=userDao.findUserByRecoveryCode(resetCode);
		if(user!=null) {
			String salt = user.getUsrSalt();
			String pw_hash = BCrypt.hashpw(userPassword, salt);
			user.setUsrPassword(pw_hash);
			user.setUsrRecoverEmailCode(null);
			userDao.update(user);
		}else{
			throw new Exception("no_user_found");
		}
	}

	
	private boolean checkProfile(User user, String profile) {
		boolean flag=false;
		if(user!=null) {
			if(profile.equalsIgnoreCase(Constants.ENTITIES_TYPE.COMPANY.name()) && user.getCompany()!=null || 
					profile.equalsIgnoreCase(Constants.ENTITIES_TYPE.EMPLOYEE.name()) && user.getEmployee()!=null||
					profile.equalsIgnoreCase(Constants.ENTITIES_TYPE.MERCHANT.name()) && user.getMerchant()!=null) {
				flag=true;
			}
		}
		return flag;
	}


	@Override
	public DTO signUp(DTO usr) throws EmailYetInTheSystemException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public DTO modProfile(DTO usr) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}
	
	public void update(User user) {
		userDao.update(user);
	}
	
	public User findUserByEmail(String username) {
		return userDao.findUserByEmail(username);
	}
	
	public FaqDTO parseFaqObject(JSONObject faq) 
    {
		FaqDTO faqDTO = new FaqDTO();
        //Get employee object within list
        JSONObject faqObject = (JSONObject) faq.get("faq");

        //Get employee first name
        String question = (String) faqObject.get("question");
        System.out.println(question);

        //Get employee last name
        String answer = (String) faqObject.get("answer");
        System.out.println(answer);
        
        faqDTO.setQuestion(question);
        faqDTO.setAnswer(answer);
		return faqDTO;

    }
	public AssistanceDTO parseAssistanceObject(JSONObject assistance) {
		AssistanceDTO assistanceDto= new AssistanceDTO();
       
        JSONObject assistanceObject = (JSONObject) assistance.get("assistenza");
       
        
        System.out.println("STO MOSTRANDO L'ARRAY --------------------------------------------------------> " + assistanceObject.get("contatti").toString());
        
        JSONArray contactList = (JSONArray) assistanceObject.get("contatti");
        
        List<ContactDTO> list = new ArrayList<>();
        
		System.out.println("STO MOSTRANDO LA CONTACT LIST -----------------------------------------------------------------> " + contactList);

		// Iterate over contacts array
		contactList.forEach(contact -> list.add(parseContacObject((JSONObject) contact)));
		
		

        
		 String title = (String) assistanceObject.get("titolo");
	        System.out.println(title);

	        
	        String content = (String) assistanceObject.get("testo");
	        System.out.println(content);
        

        
        assistanceDto.setTitle(title);
        assistanceDto.setContent(content);
        assistanceDto.setContacts(list);
		return assistanceDto;
	}
	
	public ContactDTO parseContacObject(JSONObject contact) {
		
		ContactDTO contacts = new ContactDTO();
		
		String type = (String) contact.get("tipo");
		System.out.println("type");
		
		String value = (String) contact.get("valore");
		
		contacts.setType(type);
		contacts.setValue(value);
		
		return contacts;
		
		
	}

}
