package test.com.mediatica.vouchain.servicesImpl;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.bouncycastle.util.test.TestFailedException;
import org.hibernate.SessionFactory;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.transaction.annotation.Transactional;

import com.mediatica.vouchain.config.Constants;
import com.mediatica.vouchain.exceptions.ContractNotVaidException;
import com.mediatica.vouchain.exceptions.EmailYetInTheSystemException;
import com.mediatica.vouchain.mail.Mail;
import com.mediatica.vouchain.mail.MailSenderServiceImpl;
import com.mediatica.vouchain.mail.MailSenderServiceInterface;
import com.mediatica.vouchain.utilities.Utils;

@Transactional
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("file:WebContent/WEB-INF/vouchain-servlet.xml")
@WebAppConfiguration
public class MailSenderServiceTest {

	private static org.slf4j.Logger log = LoggerFactory.getLogger(MailSenderServiceImpl.class);

	@Autowired
	SessionFactory sessionFactory;

	@Autowired
	MailSenderServiceInterface mailSenderServiceInterface;

	@Value("${reset_password_link_path}")
	private String resetPasswordLink;


//------------SEND SIMPLE EMAIL----------------------------------------

	@Test
	public void sendSimpleEmailTest() {
		
		try {
			log.info("send a simple mail...");
			mailSenderServiceInterface.sendSimpleMail("testMail@libero.it", "testSubject", "HelloWorld test text");
			log.info("mail sended");
		} catch (Exception e) {
			e.printStackTrace();
		}

		
	}
	
	//TEST KO: try to send mail without receiver
	@Test
	public void sendSimpleEmailTest_nullReceiver_KO() {
		
		try {
			log.info("send a simple mail...");
			mailSenderServiceInterface.sendSimpleMail(null, "testSubject", "HelloWorld test text");
			log.info("mail sended");

		} catch (TestFailedException ex) {
			ex.printStackTrace();
			log.error("Test Exception");
			fail();
		} catch (Exception ex) {
			ex.printStackTrace();
			log.error("Generic Exception");
			fail();
		}
		
	}
	
//------------SEND EMAIL----------------------------------------

	@Test
	public void sendEmailTest() {

		try {
			String recoverEmailCode=Utils.generateAlphaNumericString();		
			Map<String, Object> model = new HashMap<String, Object>();
			model.put("recoverEmailCode", resetPasswordLink+recoverEmailCode);
			
			log.info("preparing mail...");
			Mail mail  = mailSenderServiceInterface.prepareMail(model, "testMail@libero.it", "oggetto", null);
			log.info("mail prepared");

			log.info("sending mail...");
			mailSenderServiceInterface.sendEmail(mail, Constants.RESET_PASSWORD_TEMPLATE);			
			log.info("mail sended");

		} catch (TestFailedException ex) {
			ex.printStackTrace();
			log.error("Test Exception");
			fail();
		} catch (Exception ex) {
			ex.printStackTrace();
			log.error("Generic Exception");
			fail();
		}

	}

	//TEST KO: try to send mail without receiver
	@Test
	public void sendEmailTest_nullReceiver_KO() {

		try {
			String recoverEmailCode=Utils.generateAlphaNumericString();		
			Map<String, Object> model = new HashMap<String, Object>();
			model.put("recoverEmailCode", resetPasswordLink+recoverEmailCode);
			
			//try to send mail without receiver
			log.info("preparing mail...");
			Mail mail  = mailSenderServiceInterface.prepareMail(model, null, "oggetto", null);
			log.info("mail prepared");

			log.info("sending mail...");
			mailSenderServiceInterface.sendEmail(mail, Constants.RESET_PASSWORD_TEMPLATE);	
			log.info("mail sended");


		} catch (TestFailedException ex) {
			ex.printStackTrace();
			log.error("Test Exception");
			fail();
		} catch (Exception ex) {
			ex.printStackTrace();
			log.error("Generic Exception");
			fail();
		}

	}

}
