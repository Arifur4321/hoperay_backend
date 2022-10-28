package com.mediatica.vouchain.rest.controller;

import java.text.DateFormat;
import java.text.ParseException;
import java.util.Date;
import java.util.List;

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
import com.mediatica.vouchain.config.Constants.ENTITIES_TYPE;
import com.mediatica.vouchain.dto.DTOList;
import com.mediatica.vouchain.dto.InvoiceDTO;
import com.mediatica.vouchain.dto.SimpleResponseDTO;
import com.mediatica.vouchain.dto.TransactionDTO;
import com.mediatica.vouchain.dto.TransactionRequestDTO;
import com.mediatica.vouchain.dto.VoucherDTO;
import com.mediatica.vouchain.entities.Invoice;
import com.mediatica.vouchain.export.ExcelServiceInterface;
import com.mediatica.vouchain.servicesInterface.TransactionServiceInterface;

// TODO: Auto-generated Javadoc
/**
 * The Class TransactionRestController.
 */
@RestController
@CrossOrigin(origins="*",allowedHeaders = "*")  
@PropertySource("file:${vouchain_home}/configurations/config.properties")
@Scope( proxyMode = ScopedProxyMode.TARGET_CLASS )
public class TransactionRestController {

	/** The transaction service. */
	@Autowired
	TransactionServiceInterface transactionService;
	
	/** The excel service. */
	@Autowired
	ExcelServiceInterface excelService;


	/** The log. */
	private static org.slf4j.Logger log = LoggerFactory.getLogger(TransactionRestController.class);
	
	/** The usr id not number. */
	@Value("${usr_id_not_number}")
	private String usrIdNotNumber;
	
	/** The trc id not number. */
	@Value("${trc_id_not_number}")
	private String trcIdNotNumber;
	
	/** The profile not existing. */
	@Value("${profile_not_existing}")
	private String profileNotExisting;
	
	/** The wrong date format. */
	@Value("${wrong_date_format}")
	private String wrongDateFormat;
	
	/** The wrong profile. */
	@Value("${wrong_profile}")
	private String wrongProfile;
	
	/** The wrong trc state. */
	@Value("${wrong_trc_state}")
	private String wrongTrcState;
	
	/** The wrong trc payed. */
	@Value("${wrong_trc_payed}")
	private String wrongTrcPayed;

	
	@GetMapping("/api/transactions/getInvoice/{trcId}/{type}")
	public InvoiceDTO getInvoice(@PathVariable("trcId") String trcId,@PathVariable("type")String type) {
		InvoiceDTO response = new InvoiceDTO();
		try {
			response.setInvoice(transactionService.getInvoice(trcId,type));
			response.setStatus(Constants.RESPONSE_STATUS.OK.toString());
		} catch (Exception e) {
			response.setStatus(Constants.RESPONSE_STATUS.KO.toString());
			response.setErrorDescription(e.getMessage());
			e.printStackTrace();
		}	
		return response;

	}
	

	/**
	 * Transactions list.
	 *
	 * @param transactionRequest the transaction request
	 * @return the DTO list
	 */
	//BE_32,BE_34,BE_37 method for retrieve the transaction list for a generic user in a specific date range
	@PostMapping("/api/transactions/transactionsList")
	public DTOList<TransactionDTO> transactionsList(@RequestBody TransactionRequestDTO transactionRequest) {	
		
		DTOList<TransactionDTO> response = new DTOList<TransactionDTO>();	
		
		//validate request
		String error = validateRequest(transactionRequest);		
		//if request has error set KO status
		if(error!=null) {
			response.setStatus(Constants.RESPONSE_STATUS.KO.toString());
			response.setErrorDescription(error);
			return response;
		}
		else {
			String usrId=transactionRequest.getUsrId();
			String profile=transactionRequest.getProfile();
			String startDate=transactionRequest.getStartDate();
			String endDate=transactionRequest.getEndDate();
			
			List<TransactionDTO> list;
			try {
				boolean listForExport = false;

				list = transactionService.transactionsList(usrId, profile, startDate, endDate, listForExport);		
				response.setStatus(Constants.RESPONSE_STATUS.OK.toString());
				response.setList(list);		
				return response;				
			} catch (Exception e) {
				response.setStatus(Constants.RESPONSE_STATUS.KO.toString());
				response.setErrorDescription(e.getMessage());
				e.printStackTrace();
				return response;

			}	

		}

	}



	/**
	 * Transaction detail.
	 *
	 * @param usrId the usr id
	 * @param trcId the trc id
	 * @return the transaction DTO
	 */
	//BE_33,BE_35,BE_38 method for retrieve a transaction detail by the User ID and the transaction ID
	@GetMapping("/api/transactions/transactionDetail/{usrId}/{trcId}")
	public TransactionDTO transactionDetail(
			@PathVariable("usrId") String usrId, 
			@PathVariable("trcId") String trcId) {
		
		TransactionDTO response = new TransactionDTO();
		
		//validate request
		String error = validateRequestForDetailOrDelete(usrId, trcId);	
		
		//if request has error set KO status
		if(error!=null) {
			response.setStatus(Constants.RESPONSE_STATUS.KO.toString());
			response.setErrorDescription(error);
			return response;
		}
						
		try {
			response = transactionService.transactionsDetail(usrId, trcId);
			response.setStatus(Constants.RESPONSE_STATUS.OK.toString());
			return response;

		} catch (Exception e) {
			response.setStatus(Constants.RESPONSE_STATUS.KO.toString());
			response.setErrorDescription(e.getMessage());
			e.printStackTrace();
			return response;
		}

	}


	/**
	 * Export transaction.
	 *
	 * @param transactionRequest the transaction request
	 * @return the transaction DTO
	 */
	//BE_40,BE_41,BE_42 method for retrieve the transaction list for a generic user and export it in excel
	@PostMapping("/api/transactions/exportTransaction")
	public TransactionDTO exportTransaction(@RequestBody TransactionRequestDTO transactionRequest) {	
		
		TransactionDTO response = new TransactionDTO();
		
		//validate request
		String error = validateRequest(transactionRequest);		
		
		//if request has error set KO status
		if(error!=null) {
			response.setStatus(Constants.RESPONSE_STATUS.KO.toString());
			response.setErrorDescription(error);
			return response;
		}

		String usrId=transactionRequest.getUsrId();
		String profile=transactionRequest.getProfile();
		String startDate=transactionRequest.getStartDate();
		String endDate=transactionRequest.getEndDate();
		
		try {
			boolean listForExport = true;
			List<TransactionDTO> list = transactionService.transactionsList(usrId, profile, startDate, endDate, true);
			
			if(list!=null && list.size()>0) {
			
				byte[] excelBytes = excelService.exportTransaction(list, usrId, startDate, endDate);		
				response.setTransactionListExcel(excelBytes);	
				response.setStatus(Constants.RESPONSE_STATUS.OK.toString());
				return response;
			}
		} catch (Exception e) {
			response.setStatus(Constants.RESPONSE_STATUS.KO.toString());
			response.setErrorDescription(e.getMessage());
			e.printStackTrace();
			return response;

		}		
		return null;

	}



	/**
	 * Gets the company orders list.
	 *
	 * @param transactionRequest the transaction request
	 * @return the company orders list
	 */
	//BE_29 method for retrieve the order transaction list for a company
	@PostMapping("/api/transactions/getCompanyOrdersList")
	public DTOList<TransactionDTO> getCompanyOrdersList(@RequestBody TransactionRequestDTO transactionRequest){
		
		DTOList<TransactionDTO> response = new DTOList<TransactionDTO>();	
		
		//validate request
		String error = validateRequestForCompanyOrderList(transactionRequest);		
		//if request has error set KO status
		if(error!=null) {
			response.setStatus(Constants.RESPONSE_STATUS.KO.toString());
			response.setErrorDescription(error);
			return response;
		}
		else {
			String usrId=transactionRequest.getUsrId();
			String profile=transactionRequest.getProfile();
			String startDate=transactionRequest.getStartDate();
			String endDate=transactionRequest.getEndDate();
			String payed = transactionRequest.getTrcPayed();
			String state= transactionRequest.getTrcState();
			
			List<TransactionDTO> list;
			try {
				boolean listForExport = false;

				list = transactionService.companyOrderList(usrId, profile, startDate, endDate, payed, state);		
				response.setStatus(Constants.RESPONSE_STATUS.OK.toString());
				response.setList(list);		
				return response;				
			} catch (Exception e) {
				response.setStatus(Constants.RESPONSE_STATUS.KO.toString());
				response.setErrorDescription(e.getMessage());
				e.printStackTrace();
				return response;

			}	

		}
	}
	
	//BE_28
	@PostMapping("/api/transactions/getReedemedVoucherOrdersList")
	public DTOList<TransactionDTO> getReedemedVoucherOrdersList(@RequestBody TransactionRequestDTO transactionRequest){
		DTOList<TransactionDTO> response = new DTOList<TransactionDTO>();	
		
		//validate request
		String error = validateRequestForRedeemedOrderList(transactionRequest);		
		//if request has error set KO status
		if(error!=null) {
			response.setStatus(Constants.RESPONSE_STATUS.KO.toString());
			response.setErrorDescription(error);
			return response;
		}
		else {
			String usrId=transactionRequest.getUsrId();
			String profile=transactionRequest.getProfile();
			String startDate=transactionRequest.getStartDate();
			String endDate=transactionRequest.getEndDate();
			String payed = transactionRequest.getTrcPayed();
			String state= transactionRequest.getTrcState();
			
			List<TransactionDTO> list;
			try {
				boolean listForExport = false;

				list = transactionService.redeemedOrderList(usrId, profile, startDate, endDate, payed, state);		
				response.setStatus(Constants.RESPONSE_STATUS.OK.toString());
				response.setList(list);		
				return response;				
			} catch (Exception e) {
				response.setStatus(Constants.RESPONSE_STATUS.KO.toString());
				response.setErrorDescription(e.getMessage());
				e.printStackTrace();
				return response;

			}	

		}
	}
	




	/**
	 * Company order del.
	 *
	 * @param usrId the usr id
	 * @param trcId the trc id
	 * @return the transaction DTO
	 */
	//BE_31
	@GetMapping("/api/transactions/companyOrderDel/{trcId}")
	public SimpleResponseDTO companyOrderDel(
			@PathVariable("trcId") String trcId) {

		SimpleResponseDTO response = new SimpleResponseDTO();

		//validate request
		String error = validateRequestForDetailOrDelete(null, trcId);	

		//if request has error set KO status
		if(error!=null) {
			response.setStatus(Constants.RESPONSE_STATUS.KO.toString());
			response.setErrorDescription(error);
			return response;
		}		

		try {
			String errorFromDelete = transactionService.companyOrderDel(trcId);
			if(errorFromDelete==null) {
				response.setStatus(Constants.RESPONSE_STATUS.OK.toString());
				return response;
			}
			else {
				response.setStatus(Constants.RESPONSE_STATUS.KO.toString());
				response.setErrorDescription(errorFromDelete);
			}

		} catch (Exception e) {
			response.setStatus(Constants.RESPONSE_STATUS.KO.toString());
			response.setErrorDescription(e.getMessage());
			e.printStackTrace();
			return response;
		}
		return response;

	}

	

	
	
	

//	--------------UTILITIES------------------------------
	
	
	private String validateRequestForRedeemedOrderList(TransactionRequestDTO transactionRequest) {
		if(transactionRequest!=null) {
			if(transactionRequest.getUsrId()!=null) {
				try {
					Integer.valueOf(transactionRequest.getUsrId());
				} catch (NumberFormatException e) {
					return usrIdNotNumber;
				}
			}
						
			if(transactionRequest.getStartDate()!=null) {
				DateFormat formatter = Constants.FORMATTER_YYYY_MM_DD;
				formatter.setLenient(false);
				try {
					Date date= formatter.parse(transactionRequest.getStartDate());
				} catch (ParseException e) {
					return wrongDateFormat;
				}
			}
			
			if(transactionRequest.getEndDate()!=null) {
				DateFormat formatter = Constants.FORMATTER_YYYY_MM_DD;
				formatter.setLenient(false);
				try {
					Date date= formatter.parse(transactionRequest.getEndDate());
				} catch (ParseException e) {
					return wrongDateFormat;
				}
			}
		}
		return null;
	}
	/**
	 * Validate request for company order list.
	 *
	 * @param transactionRequest the transaction request
	 * @return the string
	 */
	private String validateRequestForCompanyOrderList(TransactionRequestDTO transactionRequest) {
		if(transactionRequest!=null) {
			if(transactionRequest.getUsrId()!=null) {
				try {
					Integer.valueOf(transactionRequest.getUsrId());
				} catch (NumberFormatException e) {
					return usrIdNotNumber;
				}
			}

			if(transactionRequest.getProfile()!=null && !transactionRequest.getProfile().equalsIgnoreCase(ENTITIES_TYPE.COMPANY.name())) {
				return wrongProfile;
			}
			
			if(transactionRequest.getTrcState()!=null && 
					(!transactionRequest.getTrcState().equalsIgnoreCase(Constants.TRANSACTION_STATUS_CONFIRMED_STRING) 
							&& !transactionRequest.getTrcState().equalsIgnoreCase(Constants.TRANSACTION_STATUS_PENDING_STRING))) {
				return wrongTrcState;
			}
			
			if(transactionRequest.getTrcPayed()!=null && 
					(!transactionRequest.getTrcPayed().equalsIgnoreCase(Constants.TRANSACTION_PAYED_STRING) 
							&& !transactionRequest.getTrcPayed().equalsIgnoreCase(Constants.TRANSACTION_NOT_PAYED_STRING))) {
				return wrongTrcPayed;
			}

			
			if(transactionRequest.getStartDate()!=null) {
				DateFormat formatter = Constants.FORMATTER_YYYY_MM_DD;
				formatter.setLenient(false);
				try {
					Date date= formatter.parse(transactionRequest.getStartDate());
				} catch (ParseException e) {
					return wrongDateFormat;
				}
			}
			
			if(transactionRequest.getEndDate()!=null) {
				DateFormat formatter = Constants.FORMATTER_YYYY_MM_DD;
				formatter.setLenient(false);
				try {
					Date date= formatter.parse(transactionRequest.getEndDate());
				} catch (ParseException e) {
					return wrongDateFormat;
				}
			}
		}
		return null;
	}



	/**
	 * Validate request for detail.
	 *
	 * @param usrId the usr id
	 * @param trcId the trc id
	 * @return the string
	 */
	private String validateRequestForDetailOrDelete(String usrId, String trcId) {
		if(usrId!=null) {
			try {
				Integer.valueOf(usrId);
			} catch (NumberFormatException e) {
				return usrIdNotNumber;
			}
		}	
		if(trcId!=null) {
			try {
				Integer.valueOf(trcId);
			} catch (NumberFormatException e) {
				return trcIdNotNumber;
			}
		}	
		
		return null;
	}
	
	/**
	 * Validate request.
	 *
	 * @param transactionRequest the transaction request
	 * @return the string
	 */
	private String validateRequest(TransactionRequestDTO transactionRequest) {
		if(transactionRequest!=null) {
			if(transactionRequest.getUsrId()!=null) {
				try {
					Integer.valueOf(transactionRequest.getUsrId());
				} catch (NumberFormatException e) {
					return usrIdNotNumber;
				}
			}

			if(transactionRequest.getProfile()!=null
					&& (!transactionRequest.getProfile().equalsIgnoreCase(ENTITIES_TYPE.COMPANY.name()) 
							&& !transactionRequest.getProfile().equalsIgnoreCase(ENTITIES_TYPE.EMPLOYEE.name())
							&& !transactionRequest.getProfile().equalsIgnoreCase(ENTITIES_TYPE.MERCHANT.name()))) {
				return profileNotExisting;
			}

			
			if(transactionRequest.getStartDate()!=null) {
				DateFormat formatter = Constants.FORMATTER_YYYY_MM_DD;
				formatter.setLenient(false);
				try {
					Date date= formatter.parse(transactionRequest.getStartDate());
				} catch (ParseException e) {
					return wrongDateFormat;
				}
			}
			
			if(transactionRequest.getEndDate()!=null) {
				DateFormat formatter = Constants.FORMATTER_YYYY_MM_DD;
				formatter.setLenient(false);
				try {
					Date date= formatter.parse(transactionRequest.getEndDate());
				} catch (ParseException e) {
					return wrongDateFormat;
				}
			}
		}
		return null;

	}
	
	



}
