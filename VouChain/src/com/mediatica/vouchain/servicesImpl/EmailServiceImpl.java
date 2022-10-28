package com.mediatica.vouchain.servicesImpl;

import java.io.File;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mediatica.vouchain.config.Constants;
import com.mediatica.vouchain.config.enumerations.TransactionType;
import com.mediatica.vouchain.dao.TransactionDaoImpl;
import com.mediatica.vouchain.dao.TransactionDetailDaoImpl;
import com.mediatica.vouchain.dao.UserDaoImpl;
import com.mediatica.vouchain.dao.VoucherDaoImpl;
import com.mediatica.vouchain.dto.EmployeeDTO;
import com.mediatica.vouchain.entities.Transaction;
import com.mediatica.vouchain.entities.TransactionDetail;
import com.mediatica.vouchain.entities.User;
import com.mediatica.vouchain.export.PdfServicesImpl;
import com.mediatica.vouchain.mail.Mail;
import com.mediatica.vouchain.mail.MailSenderServiceImpl;
import com.mediatica.vouchain.rest.client.BlockChainRestClient;
import com.mediatica.vouchain.servicesInterface.EmailServiceInterface;
import com.mediatica.vouchain.utilities.Utils;
import com.mysql.cj.util.Util;

@Service
@Transactional

@PropertySources({ @PropertySource("file:${vouchain_home}/configurations/config.properties"),
		@PropertySource("file:${vouchain_home}/configurations/email.properties") })

@Scope(proxyMode = ScopedProxyMode.TARGET_CLASS)
public class EmailServiceImpl implements EmailServiceInterface {

	@Autowired
	VoucherDaoImpl dao;

	@Autowired
	TransactionDaoImpl transactionDao;

	@Autowired
	TransactionDetailDaoImpl transactionDetailDao;

	@Autowired
	UserDaoImpl userDao;

	@Autowired
	BlockChainRestClient blockChainClient;

	@Autowired
	MailSenderServiceImpl mailSenderService;

	@Autowired
	PdfServicesImpl pdfService;

	@Value("${invitation_link_path}")
	private String invitationLinkPath;
	@Value("${download_app_android}")
	private String downloadAndroidApp;
	@Value("${download_app_ios}")
	private String downloadIOSApp;
	

	private static org.slf4j.Logger log = LoggerFactory.getLogger(VoucherServiceImpl.class);

	@Override
	public void voucherOrdereMail(String trcId) {

		Transaction transaction = new Transaction();
		transaction = transactionDao.findByPrimaryKey(transaction, Integer.parseInt(trcId));

		if (transaction != null && (transaction.getTransactionDetailCollection() == null
				|| transaction.getTransactionDetailCollection().size() == 0)) {
			List<TransactionDetail> transactionDetailList = new ArrayList<TransactionDetail>();
			transactionDetailList = transactionDetailDao.findByTrcId(transaction.getTrcId().toString());
			transaction.setTransactionDetailCollection(transactionDetailList);
		}

		if (transaction != null && transaction.getUsrIdA() != null && transaction.getUsrIdA().getUsrEmail() != null) {
			///// managing mail
			try {
				log.info("Sending a confirmation mail to: {}", transaction.getUsrIdA().getUsrEmail());
				Map<String, Object> model = prepareModel(transaction);

				Mail mail = mailSenderService.prepareMail(model, transaction.getUsrIdA().getUsrEmail(),
						mailSenderService.getMailOrderList(), null);

				mailSenderService.sendEmail(mail, Constants.TRANSACTION_DETAIL_TEMPLATE);

				transaction.setTrcMailSent(true);
				transactionDao.update(transaction);

			} catch (Exception e) {
				log.error("Error sending email", e);
				log.warn("There was an error sending email let's continue anyway the process ");
			}

		}

	}

	@Override
	public void voucherConfeMail(String trcId) throws Exception {

		String transactionType = "";

		if (trcId != null) {
			Transaction transaction = new Transaction();
			transaction = transactionDao.findByPrimaryKey(transaction, Integer.parseInt(trcId));

			if (transaction != null) {
				if (transaction.getTransactionDetailCollection() == null
						|| transaction.getTransactionDetailCollection().size() == 0) {
					List<TransactionDetail> transactionDetailList = new ArrayList<TransactionDetail>();
					transactionDetailList = transactionDetailDao.findByTrcId(transaction.getTrcId().toString());
					transaction.setTransactionDetailCollection(transactionDetailList);
				}
				if (transaction.getTrcType() != null && transaction.getTrcType()
						.equalsIgnoreCase(TransactionType.COMPANY_GIVE_VOUCHER_TO_EMPLOYEE.getDescription())) {
					transactionType = TransactionType.COMPANY_GIVE_VOUCHER_TO_EMPLOYEE.getDescription();
				}

				else if (transaction.getTrcType() != null && transaction.getTrcType()
						.equalsIgnoreCase(TransactionType.EMPLOYEE_GIVE_VOUCHER_TO_MERCHANT.getDescription())) {
					transactionType = TransactionType.EMPLOYEE_GIVE_VOUCHER_TO_MERCHANT.getDescription();
				}

				else if (transaction.getTrcType() != null
						&& transaction.getTrcType().equalsIgnoreCase(TransactionType.REDEEM_VOUCHER.getDescription())) {
					transactionType = TransactionType.REDEEM_VOUCHER.getDescription();
				} else {
					throw new Exception(
							"transaction with id " + trcId + " is neither an allocation nor an expense nor a redeem");
				}
			} else {
				throw new Exception("transaction does not exists");
			}

			sendMailConfirmationMail(transaction, transactionType);

		}

	}

	@Override
	public void voucherConfeMail2(Transaction transaction) throws Exception {
		try {
			log.info("voucherConfeMail START");
			String transactionType = "";
			if (transaction != null) {
				if (transaction.getTransactionDetailCollection() == null
						|| transaction.getTransactionDetailCollection().size() == 0) {
					List<TransactionDetail> transactionDetailList = new ArrayList<TransactionDetail>();
					transactionDetailList = transactionDetailDao.findByTrcId(transaction.getTrcId().toString());
					transaction.setTransactionDetailCollection(transactionDetailList);
				}
				if (transaction.getTrcType() != null && transaction.getTrcType()
						.equalsIgnoreCase(TransactionType.COMPANY_GIVE_VOUCHER_TO_EMPLOYEE.getDescription())) {
					transactionType = TransactionType.COMPANY_GIVE_VOUCHER_TO_EMPLOYEE.getDescription();
				}

				else if (transaction.getTrcType() != null && transaction.getTrcType()
						.equalsIgnoreCase(TransactionType.EMPLOYEE_GIVE_VOUCHER_TO_MERCHANT.getDescription())) {
					transactionType = TransactionType.EMPLOYEE_GIVE_VOUCHER_TO_MERCHANT.getDescription();
				}

				else if (transaction.getTrcType() != null
						&& transaction.getTrcType().equalsIgnoreCase(TransactionType.REDEEM_VOUCHER.getDescription())) {
					transactionType = TransactionType.REDEEM_VOUCHER.getDescription();
				} else {
					throw new Exception("transaction with id  is neither an allocation nor an expense nor a redeem");
				}
			} else {
				throw new Exception("transaction does not exists");
			}
			sendMailConfirmationMail(transaction, transactionType);
			log.info("voucherConfeMail STOP");
		} catch (Exception e) {
			log.error("Error sending email: {}", e.getMessage());

		}

	}

	@Override
	public void paymentOrderConfeMail(String trcId) throws Exception {

		String transactionType = "";

		if (trcId != null) {
			Transaction transaction = new Transaction();
			transaction = transactionDao.findByPrimaryKey(transaction, Integer.parseInt(trcId));

			if (transaction != null) {
				if (transaction.getTransactionDetailCollection() == null
						|| transaction.getTransactionDetailCollection().size() == 0) {
					List<TransactionDetail> transactionDetailList = new ArrayList<TransactionDetail>();
					transactionDetailList = transactionDetailDao.findByTrcId(transaction.getTrcId().toString());
					transaction.setTransactionDetailCollection(transactionDetailList);
				}
				if (transaction.getTrcType() != null && transaction.getTrcType()
						.equalsIgnoreCase(TransactionType.COMPANY_BUY_VOUCHER.getDescription())) {
					sendPaymentOrderConfeMail(transaction);
				} else {
					throw new Exception("transaction with id " + trcId + " is not a company buy transaction");
				}
			} else {
				throw new Exception("transaction does not exists");
			}
		}
	}

	@Override
	public void paymentRedeemConfeMail(String trcId) throws Exception {

		String transactionType = "";

		if (trcId != null) {
			Transaction transaction = new Transaction();
			transaction = transactionDao.findByPrimaryKey(transaction, Integer.parseInt(trcId));

			if (transaction != null) {
				if (transaction.getTransactionDetailCollection() == null
						|| transaction.getTransactionDetailCollection().size() == 0) {
					List<TransactionDetail> transactionDetailList = new ArrayList<TransactionDetail>();
					transactionDetailList = transactionDetailDao.findByTrcId(transaction.getTrcId().toString());
					transaction.setTransactionDetailCollection(transactionDetailList);
				}
				if (transaction.getTrcType() != null
						&& transaction.getTrcType().equalsIgnoreCase(TransactionType.REDEEM_VOUCHER.getDescription())) {
					sendPaymentRedeemConfeMail(transaction);
				} else {
					throw new Exception("transaction with id " + trcId + " is not a redeem transaction");
				}
			} else {
				throw new Exception("transaction does not exists");
			}
		}
	}

	public void manageCompanyConfirmationMail(User user, Transaction trx) {
		try {
			log.info("Sending a confirmation mail to: {}", user.getCompany().getCpyPec());
			String attachmentFileName = pdfService.exportCompanyContract(user);
			Map<String, Object> model = prepareCompanyModel(user);

			List<File> attachments = new ArrayList<File>();
			attachments.add(new File(attachmentFileName));
			// attachments.add(new File(imageFileName));

			Mail mail = mailSenderService.prepareMail(model, user.getCompany().getCpyPec(),
					mailSenderService.getMailCompanyConfirmationSubject(), attachments);
			mailSenderService.sendEmail(mail, Constants.COMPANY_CONFIRM_TEMPLATE);
			trx.setTrcMailSent(true);
			transactionDao.update(trx);
		} catch (Exception e) {
			log.error("Error sending email", e);
			log.warn("There was an error sending email let's continue anyway the process ");
		}
	}

	public void manageMerchantConfirmationMail(User user, Transaction trx) {
		try {
			log.info("Sending a confirmation mail to: {}", user.getUsrEmail());
			Map<String, Object> model = prepareMerchantModel(user);
			Mail mail = mailSenderService.prepareMail(model, user.getUsrEmail(),
					mailSenderService.getMailMerchantConfirmationSubject(), null);
			mailSenderService.sendEmail(mail, Constants.MERCHANT_CONFIRM_TEMPLATE);
			trx.setTrcMailSent(true);
			transactionDao.update(trx);
		} catch (Exception e) {
			log.error("Error sending email", e);
			log.warn("There was an error sending email let's continue anyway the process ");
		}
	}

	public void manageEmployeeConfirmationMail(User usr, Transaction trx) {
		try {
			log.info("Sending a confirmation mail to: {}", usr.getUsrEmail());
			Map<String, Object> model = prepareEmployeeModel(usr);
			Mail mail = mailSenderService.prepareMail(model, usr.getUsrEmail(),
					mailSenderService.getMailEmployeeConfirmationSubject(), null);
			mailSenderService.sendEmail(mail, Constants.EMPLOY_CONFIRM_TEMPLATE);
			trx.setTrcMailSent(true);
			transactionDao.update(trx);
		} catch (Exception e) {
			log.error("Error sending email", e);
			log.warn("There was an error sending email let's continue anyway the process ");
		}
	}

	public void manageSignUpEmployeeMail(List<EmployeeDTO> list) {
		for (EmployeeDTO employDTO : list) {
			try {
				log.info("Sending a confirmation mail to: {}", employDTO.getUsrEmail());
				Mail mail = prepareInvitationMail(employDTO.getEmpInvitationCode(), employDTO.getUsrEmail());

				mailSenderService.sendEmail(mail, Constants.EMPLOY_INVITATION_TEMPLATE);
				log.info("Sent a confirmation mail to: {}", employDTO.getUsrEmail());
			} catch (Exception e) {
				log.error("Error sending email", e);
				log.warn("There was an error sending email let's continue anyway the process ");
			}
		}
	}

	// BE_44 Invia mail di invito ai beneficiari indicati in input da parte
	// dell’azienda;
	// la mail conterrà un codice univoco di invito e il link per connettersi alla
	// piattaforma alla funzione [FE_13].
	// Modificata per inserire nella mail anche un link utile a scaricare l’App (IOS e Android),
	// e le modalità di registrazione, comprensive del codice di invito in esplicito
	// da inserire nella funzione di SignUp dell’app.

	private Mail prepareInvitationMail(String invitationCode, String email) {
		Mail mail = new Mail();
		mail.setMailFrom(mailSenderService.getMailFrom());
		mail.setMailSubject(mailSenderService.getMailEmployeeInvitationSubject());
		mail.setMailTo(email);
		Map<String, Object> model = new HashMap<String, Object>();
		String invitationLink = invitationLinkPath + invitationCode;
		model.put("invitationLink", invitationLink);
		model.put("downloadAndroidApp", downloadAndroidApp);
		model.put("downloadIOSApp", downloadIOSApp);
		model.put("invitationCode",invitationCode);
		mail.setModel(model);
		return mail;
	}

	private Map<String, Object> prepareModel(Transaction transaction) {

		Map<String, Object> model = new HashMap<String, Object>();
		List<TransactionDetail> transactionDetailList = new ArrayList<TransactionDetail>();

		int totalVoucherNumber = 0;
		double totalVoucherValue = 0;

		if (transaction != null && transaction.getTransactionDetailCollection() != null
				&& transaction.getTransactionDetailCollection().size() > 0) {
			for (TransactionDetail voucher : transaction.getTransactionDetailCollection()) {
				if (voucher.getTrdQuantity() != null && voucher.getVoucher() != null
						&& voucher.getVoucher().getVchValue() != null) {
					totalVoucherNumber += voucher.getTrdQuantity();
					totalVoucherValue += voucher.getTrdQuantity() * voucher.getVoucher().getVchValue().doubleValue();
				}

				transactionDetailList.add(voucher);
			}

			model.put("transactionDetailList", transactionDetailList);
			model.put("trcId", transaction.getTrcId() != null ? transaction.getTrcId().toString() : "");
			model.put("voucherNumber", String.valueOf(totalVoucherNumber));
			String totalVoucher = "";

			totalVoucher = Utils.formatNumeberTotwoDecimalDigitsAndThousandSeparator(totalVoucherValue);

			model.put("totalVoucherValue", totalVoucher);
			if (transaction.getUsrIdA() != null) {
				model.put("ragioneSociale",
						(transaction.getUsrIdA().getCompany() != null
								&& transaction.getUsrIdA().getCompany().getCpyRagioneSociale() != null)
										? transaction.getUsrIdA().getCompany().getCpyRagioneSociale()
										: "");
			}
			if (transaction.getUsrIdDa() != null) {
				model.put("ragioneSociale",
						(transaction.getUsrIdDa().getCompany() != null
								&& transaction.getUsrIdDa().getCompany().getCpyRagioneSociale() != null)
										? transaction.getUsrIdDa().getCompany().getCpyRagioneSociale()
										: "");
			}
			model.put("trcDate",
					transaction.getTrcDate() != null ? Constants.FORMATTER_DD_MM_YYYY.format(transaction.getTrcDate())
							: "");

		}

		else {
			model.put("transactionDetailList", new ArrayList<TransactionDetail>());
			model.put("trcId", "");
			model.put("voucherNumber", "");
			model.put("totalVoucherValue", "");
			model.put("ragioneSociale", "");
			model.put("trcDate", "");
		}

		return model;
	}

	private Map<String, Object> prepareEmployeeModel(User user) {
		Map<String, Object> model = new HashMap<String, Object>();
		model.put("usrEmail", user.getUsrEmail() != null ? user.getUsrEmail() : "");
		model.put("empFirstName",
				user.getEmployee().getEmpFirstName() != null ? user.getEmployee().getEmpFirstName() : "");
		model.put("empLastName",
				user.getEmployee().getEmpLastName() != null ? user.getEmployee().getEmpLastName() : "");
		model.put("empMatricola",
				user.getEmployee().getEmpMatricola() != null ? user.getEmployee().getEmpMatricola() : "");
		return model;
	}

	private Map<String, Object> prepareCompanyModel(User user) {
		Map<String, Object> model = new HashMap<String, Object>();
		model.put("usrEmail", user.getUsrEmail() != null ? user.getUsrEmail() : "");
		model.put("cpyCodiceFiscale",
				user.getCompany().getCpyCodiceFiscale() != null ? user.getCompany().getCpyCodiceFiscale() : "");
		model.put("cpyPartitaIva",
				user.getCompany().getCpyPartitaIva() != null ? user.getCompany().getCpyPartitaIva() : "");
		model.put("cpyRagioneSociale",
				user.getCompany().getCpyRagioneSociale() != null ? user.getCompany().getCpyRagioneSociale() : "");

		model.put("cpyPec", user.getCompany().getCpyPec() != null ? user.getCompany().getCpyPec() : "");
		model.put("cpyAddress", user.getCompany().getCpyAddress() != null ? user.getCompany().getCpyAddress() : "");
		model.put("cpyCity", user.getCompany().getCpyCity() != null ? user.getCompany().getCpyCity() : "");
		model.put("cpyProv", user.getCompany().getCpyProv() != null ? user.getCompany().getCpyProv() : "");

		model.put("cpyZip", user.getCompany().getCpyZip() != null ? user.getCompany().getCpyZip() : "");
		model.put("cpyFirstName",
				user.getCompany().getCpyFirstNameRef() != null ? user.getCompany().getCpyFirstNameRef() : "");
		model.put("cpyLastName",
				user.getCompany().getCpyLastNameRef() != null ? user.getCompany().getCpyLastNameRef() : "");
		model.put("cpyNoPhoneRef",
				user.getCompany().getCpyPhoneNoRef() != null ? user.getCompany().getCpyPhoneNoRef() : "");

		model.put("cpyCuu", user.getCompany().getCpyCuu() != null ? user.getCompany().getCpyCuu() : "");
		return model;
	}

	private Map<String, Object> prepareMerchantModel(User user) {

		Map<String, Object> model = new HashMap<String, Object>();

		model.put("mrcRoleReq", user.getMerchant().getMrcRoleReq() != null ? user.getMerchant().getMrcRoleReq() : "");
		model.put("mrcLastNameReq",
				user.getMerchant().getMrcLastNameReq() != null ? user.getMerchant().getMrcLastNameReq() : "");
		model.put("mrcFirstNameReq",
				user.getMerchant().getMrcFirstNameReq() != null ? user.getMerchant().getMrcFirstNameReq() : "");
		model.put("mrcLastNameRef",
				user.getMerchant().getMrcLastNameRef() != null ? user.getMerchant().getMrcLastNameRef() : "");
		model.put("mrcFirstNameRef",
				user.getMerchant().getMrcFirstNameRef() != null ? user.getMerchant().getMrcFirstNameRef() : "");

		model.put("mrcRagioneSociale",
				user.getMerchant().getMrcRagioneSociale() != null ? user.getMerchant().getMrcRagioneSociale() : "");
		model.put("mrcCodiceFiscale",
				user.getMerchant().getMrcCodiceFiscale() != null ? user.getMerchant().getMrcCodiceFiscale() : "");
		model.put("mrcPartitaIva",
				user.getMerchant().getMrcPartitaIva() != null ? user.getMerchant().getMrcPartitaIva() : "");
		model.put("usrEmail", user.getUsrEmail() != null ? user.getUsrEmail() : "");
		model.put("mrcPhoneNo", user.getMerchant().getMrcPhoneNo() != null ? user.getMerchant().getMrcPhoneNo() : "");
		model.put("mrcCity", user.getMerchant().getMrcCity() != null ? user.getMerchant().getMrcCity() : "");
		model.put("mrcProv", user.getMerchant().getMrcProv() != null ? user.getMerchant().getMrcProv() : "");
		model.put("mrcAddress", user.getMerchant().getMrcAddress());
		model.put("mrcZip", user.getMerchant().getMrcZip() != null ? user.getMerchant().getMrcZip() : "");
		model.put("mrcIban", user.getMerchant().getMrcIban() != null ? user.getMerchant().getMrcIban() : "");
		model.put("mrcBank", user.getMerchant().getMrcBank() != null ? user.getMerchant().getMrcBank() : "");

		model.put("mrcOfficeName",
				user.getMerchant().getMrcOfficeName() != null ? user.getMerchant().getMrcOfficeName() : "");
		model.put("mrcPhoneNoOffice",
				user.getMerchant().getMrcPhoneNoOffice() != null ? user.getMerchant().getMrcPhoneNoOffice() : "");
		model.put("mrcProvOffice",
				user.getMerchant().getMrcProvOffice() != null ? user.getMerchant().getMrcProvOffice() : "");
		model.put("mrcAddressOffice",
				user.getMerchant().getMrcAddressOffice() != null ? user.getMerchant().getMrcAddressOffice() : "");
		model.put("mrcCityOffice",
				user.getMerchant().getMrcCityOffice() != null ? user.getMerchant().getMrcCityOffice() : "");

		return model;

	}

	private Map<String, Object> prepareModelForConfeMail(Transaction transaction, String transactionType) {
		log.info("Starting preparing model for email");
		Map<String, Object> model = new HashMap<String, Object>();
		List<TransactionDetail> transactionDetailList = new ArrayList<TransactionDetail>();

		int totalVoucherNumber = 0;
		double totalVoucherValue = 0;

		if (transaction != null && transaction.getTransactionDetailCollection() != null
				&& transaction.getTransactionDetailCollection().size() > 0) {
			for (TransactionDetail voucher : transaction.getTransactionDetailCollection()) {
				if (voucher.getTrdQuantity() != null && voucher.getVoucher() != null
						&& voucher.getVoucher().getVchValue() != null) {
					totalVoucherNumber += voucher.getTrdQuantity();
					totalVoucherValue += voucher.getTrdQuantity() * voucher.getVoucher().getVchValue().doubleValue();
				}

				transactionDetailList.add(voucher);
			}

			model.put("transactionDetailList", transactionDetailList);
			model.put("trcId", transaction.getTrcId() != null ? transaction.getTrcId().toString() : "");
			model.put("voucherNumber", String.valueOf(totalVoucherNumber));

			String totalVoucher = Utils.formatNumeberTotwoDecimalDigitsAndThousandSeparator(totalVoucherValue);

			model.put("totalVoucherValue", totalVoucher);

			if (transaction.getUsrIdA() != null
					&& transactionType.equalsIgnoreCase(TransactionType.COMPANY_BUY_VOUCHER.getDescription())) {
				model.put("ragioneSociale",
						(transaction.getUsrIdA().getCompany() != null
								&& transaction.getUsrIdA().getCompany().getCpyRagioneSociale() != null)
										? transaction.getUsrIdA().getCompany().getCpyRagioneSociale()
										: "");
			}

			if (transaction.getUsrIdDa() != null && transactionType
					.equalsIgnoreCase(TransactionType.COMPANY_GIVE_VOUCHER_TO_EMPLOYEE.getDescription())) {
				model.put("ragioneSociale",
						(transaction.getUsrIdDa().getCompany() != null
								&& transaction.getUsrIdDa().getCompany().getCpyRagioneSociale() != null)
										? transaction.getUsrIdDa().getCompany().getCpyRagioneSociale()
										: "");
			}
			if (transaction.getUsrIdDa() != null && transactionType
					.equalsIgnoreCase(TransactionType.EMPLOYEE_GIVE_VOUCHER_TO_MERCHANT.getDescription())) {
				model.put("nomeCognomeUtente",
						(transaction.getUsrIdDa().getEmployee() != null
								&& transaction.getUsrIdDa().getEmployee().getEmpFirstName() != null)
										? transaction.getUsrIdDa().getEmployee().getEmpFirstName() + " "
												+ transaction.getUsrIdDa().getEmployee().getEmpLastName()
										: "");
			}
			if (transaction.getUsrIdDa() != null
					&& transactionType.equalsIgnoreCase(TransactionType.REDEEM_VOUCHER.getDescription())) {
				model.put("ragioneSociale",
						(transaction.getUsrIdDa().getMerchant() != null
								&& transaction.getUsrIdDa().getMerchant().getMrcRagioneSociale() != null)
										? transaction.getUsrIdDa().getMerchant().getMrcRagioneSociale()
										: "");
				model.put("IBAN",
						(transaction.getUsrIdDa().getMerchant() != null
								&& transaction.getUsrIdDa().getMerchant().getMrcIban() != null)
										? transaction.getUsrIdDa().getMerchant().getMrcIban()
										: "");

				if (transaction.getUsrIdDa().getMerchant() != null
						&& transaction.getUsrIdDa().getMerchant().getMrcFirstNameRef() != null
						&& transaction.getUsrIdDa().getMerchant().getMrcLastNameRef() != null) {
					model.put("intestatario", transaction.getUsrIdDa().getMerchant().getMrcFirstNameRef() + " "
							+ transaction.getUsrIdDa().getMerchant().getMrcLastNameRef());
				} else {
					model.put("intestatario", " ");
				}

			}

			model.put("trcDate",
					transaction.getTrcDate() != null ? Constants.FORMATTER_DD_MM_YYYY.format(transaction.getTrcDate())
							: "");

		}

		else {
			model.put("transactionDetailList", new ArrayList<TransactionDetail>());
			model.put("trcId", "");
			model.put("voucherNumber", "");
			model.put("totalVoucherValue", "");
			model.put("ragioneSociale", "");
			model.put("trcDate", "");
		}
		log.info("END preparing model for email");
		return model;
	}

	private void sendPaymentOrderConfeMail(Transaction transaction) {

		if (transaction != null && transaction.getUsrIdA() != null && transaction.getUsrIdA().getUsrEmail() != null) {
			try {
				log.info("Sending a confirmation mail to: {}", transaction.getUsrIdA().getUsrEmail());

				Map<String, Object> model = prepareModelForConfeMail(transaction, transaction.getTrcType());

				Mail mail = mailSenderService.prepareMail(model, transaction.getUsrIdA().getUsrEmail(),
						mailSenderService.getMailAllocationConfirm(), null);
				mailSenderService.sendEmail(mail, Constants.CONFIRM_EMAIL_PAYMENT_ORDER_TEMPLATE);
				transaction.setTrcMailSent(true);
				transactionDao.update(transaction);

			} catch (Exception e) {
				log.error("Error sending email", e);
				log.warn("There was an error sending email let's continue anyway the process ");
			}

		}
	}

	private void sendPaymentRedeemConfeMail(Transaction transaction) {

		if (transaction != null && transaction.getUsrIdDa() != null && transaction.getUsrIdDa().getUsrEmail() != null) {
			try {
				log.info("Sending a confirmation mail to: {}", transaction.getUsrIdDa().getUsrEmail());

				Map<String, Object> model = prepareModelForConfeMail(transaction, transaction.getTrcType());

				Mail mail = mailSenderService.prepareMail(model, transaction.getUsrIdDa().getUsrEmail(),
						mailSenderService.getMailAllocationConfirm(), null);
				mailSenderService.sendEmail(mail, Constants.CONFIRM_EMAIL_PAYMENT_REDEEM_TEMPLATE);
				transaction.setTrcMailSent(true);
				transactionDao.update(transaction);

			} catch (Exception e) {
				log.error("Error sending email", e);
				log.warn("There was an error sending email let's continue anyway the process ");
			}

		}
	}

	private void sendMailConfirmationMail(Transaction transaction, String transactionType) {
		if (transaction != null && transaction.getUsrIdDa() != null && transaction.getUsrIdDa().getUsrEmail() != null) {
			try {
				log.info("Sending a confirmation mail to: {}", transaction.getUsrIdDa().getUsrEmail());

				Map<String, Object> model = prepareModelForConfeMail(transaction, transactionType);

				if (transactionType
						.equalsIgnoreCase(TransactionType.COMPANY_GIVE_VOUCHER_TO_EMPLOYEE.getDescription())) {
					log.info("Merging into template");
					Mail mail = mailSenderService.prepareMail(model, transaction.getUsrIdDa().getUsrEmail(),
							mailSenderService.getMailAllocationConfirm(), null);
					log.info("Contacting SMTP server");
					mailSenderService.sendEmail(mail, Constants.CONFIRM_EMAIL_ALLOCATION_TEMPLATE);
				} else if (transactionType
						.equalsIgnoreCase(TransactionType.EMPLOYEE_GIVE_VOUCHER_TO_MERCHANT.getDescription())) {
					log.info("Merging into template");
					Mail mail = mailSenderService.prepareMail(model, transaction.getUsrIdDa().getUsrEmail(),
							mailSenderService.getMailExpenseConfirm(), null);
					log.info("Contacting SMTP server");
					mailSenderService.sendEmail(mail, Constants.CONFIRM_EMAIL_EXPEND_TEMPLATE);
				}

				else if (transactionType.equalsIgnoreCase(TransactionType.REDEEM_VOUCHER.getDescription())) {
					log.info("Merging into template");
					Mail mail = mailSenderService.prepareMail(model, transaction.getUsrIdDa().getUsrEmail(),
							mailSenderService.getMailRedeemConfirm(), null);
					log.info("Contacting SMTP server");
					mailSenderService.sendEmail(mail, Constants.CONFIRM_EMAIL_REDEEM_TEMPLATE);
				}

				transaction.setTrcMailSent(true);
				transactionDao.update(transaction);
				log.info("Database updated email process FINISHED");

			} catch (Exception e) {
				log.error("Error sending email", e);
				log.warn("There was an error sending email let's continue anyway the process ");
			}

		}
	}

}
