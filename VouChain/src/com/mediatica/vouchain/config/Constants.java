package com.mediatica.vouchain.config;

import java.text.SimpleDateFormat;

/**
 * 
 * @author Pietro Napolitano
 *
 */
public class Constants {

	public static String ENTITY_BASE_PACKAGE="com.mediatica.vouchain.entities";
	
	public enum RESPONSE_STATUS{
		  OK,
		  KO
		}
	
	public enum OK_KO{
		  OK,
		  KO
		}
	
	public enum ENTITIES_TYPE{
		  MERCHANT,
		  EMPLOYEE,
		  COMPANY
		}


	
	public final static String MAIL_TEMPLATE_SUBDIR_PATH= "/templates/ftl/";
	
	public final static String PDF_TEMPLATE_SUBDIR_PATH= "/templates/pdf/";
	
	public final static String XSL_TEMPLATE_SUBDIR_PATH= "/templates/xsl/";
	
	public final static String EXCEL_TEMPLATE_SUBDIR_PATH= "/templates/excel/";
	
	public final static String JSON_TEMPLATE_SUBDIR_PATH= "/templates/json/";
	
	public final static String PDF_EXPORT_SUBDIR_PATH= "/export/pdf/";
	
	public final static String XML_EXPORT_SUBDIR_PATH= "/export/xml/";

	public static final String IMAGES_SUBDIR_PATH = "/images/";
	
	public final static String COMPANY_CONFIRM_TEMPLATE= "companyConfirmationRegistrationEmail.ftl";
	
	public final static String EMPLOY_CONFIRM_TEMPLATE= "employeeConfirmationRegistrationEmail.ftl";
	
	public static final String MERCHANT_CONFIRM_TEMPLATE = "merchantConfirmationRegistrationEmail.ftl";
	
	public final static String EMPLOY_INVITATION_TEMPLATE= "employInvitationEmail.ftl";
	
	public static final String RESET_PASSWORD_TEMPLATE = "resetPassword.ftl";
	
	public final static String TRANSACTION_DETAIL_TEMPLATE= "transactionDetailEmail.ftl";
	
	public final static String CONFIRM_EMAIL_ALLOCATION_TEMPLATE= "confeAllocationMail.ftl";
	
	public final static String CONFIRM_EMAIL_EXPEND_TEMPLATE= "confeExpendMail.ftl";
	
	public final static String CONFIRM_EMAIL_REDEEM_TEMPLATE= "confeRedeemMail.ftl";
	
	public final static String CONFIRM_EMAIL_PAYMENT_ORDER_TEMPLATE= "confePaymentOrderMail.ftl";
	
	public final static String CONFIRM_EMAIL_PAYMENT_REDEEM_TEMPLATE= "confePaymentRedeemMail.ftl";



	



	
	public final static String VOUCHAIN_HOME  = System.getProperty("vouchain_home");
	
	public final static String PDF_TEMPLATE_FILE_NAME="ContractTemplate.pdf";
	
	public final static String EXCEL_TEMPLATE_FILE_NAME="excel.xlsx";

	public static final String PDF_SUFFIX = ".pdf";

	public static final String XML_SUFFIX = ".xml";
	
	public static final String P7M_SUFFIX = ".p7m";
	
	public static final String HTML_SUFFIX = ".html";
	
	public final static String LOGO_FILE_NAME="vouchain_logo.png";
	
	public static final String HASHING_ALGORITHM = "MD5";
	
	public static final SimpleDateFormat FORMATTER_DD_MM_YYYY = new SimpleDateFormat("dd-MM-yyyy");
	
	public static final SimpleDateFormat FORMATTER_YYYY_MM_DD = new SimpleDateFormat("yyyy-MM-dd");
	
	public static final SimpleDateFormat FORMATTER_YYYY_MM_DD_UNDERSCORE = new SimpleDateFormat("yyyy_MM_dd");

	public static final String TEMP_SUBDIR = "/temp";

	public static final String HASH_VIOLATION = "hash_violation";

	public static final String SIGN_VIOLATION = "sign_violation";

	public static final String VOUCHAIN_LOGO_NAME = "vouchain_logo.png";
	
	public static final String TRANSACTION_STATUS_CONFIRMED_STRING = "confirmed";

	public static final String TRANSACTION_STATUS_PENDING_STRING = "pending";
	
	public static final String TRANSACTION_PAYED_STRING="payed";
	
	public static final String TRANSACTION_NOT_PAYED_STRING="not_payed";
	
	public static final boolean TRANSACTION_PAYED=true;
	
	public static final boolean TRANSACTION_NOT_PAYED=false;
	
	public static final boolean TRANSACTION_STATUS_PENDING = false;
	
	public static final boolean TRANSACTION_STATUS_CONFIRMED = true;
	
	public static final short ROW_HEIGHT_IN_POINT = 25;
	
	public static final short TEXT_HEIGHT_IN_POINT = 25;
	
	//FILTER ERROR
	public static final String SESSION_EXPIRED="session_expired";
	
	public static final String NON_CORRESPONDING_SESSION="not_corresponding_session";
	
	public static final String MISSING_PARAMETERS_="missing_session_parameters";
	
	
	public static final String GENERIC_ERROR="generic_server_error";
	
	public static final int FORBIDDEN_CODE_401 = 401;
	
	public static final int FORBIDDEN_CODE_403 = 403;
	
	public static final int SESSION_EXPIRED_CODE = 440;
	
	public static final int NON_CORRESPONDING_SESSION_CODE = 441;
	
	public static final int MISSING_PARAMETERS_CODE = 442;
	
	
	public static final int GENERIC_ERROR_CODE = 500;

	public static final long maxUploadsSize = 100000;

	public static final long maxAge = 3600;
	
	
	public static final int ENCRYPTING_KEY_LENGTH = 16;
	
	public static final int BYTE_1024_LENGTH = 1024;
	
	public static final int MAIL_SENDER_PORT = 587;
	
	//EXCEL PARAMETERS
	public static final int INITIAL_ROW_POSTION_FOR_TABLE = 6;
	
	public static final int INITIAL_CELL_POSTION_FOR_TABLE = 1;

	public static final String ITA = "IT";

	public static final String FPR12 = "FPR12";

	public static final String EUR = "EUR";

	public static final String TD01 = "TD01";

	public static final String TP02 = "TP02";

	public static final String XSL_TEMPLATE = "fatturapa_v1.2.1.xsl";

	public static final String EURO_SYMBOL = "  €";







	


	
	

	

	


	
	
}
