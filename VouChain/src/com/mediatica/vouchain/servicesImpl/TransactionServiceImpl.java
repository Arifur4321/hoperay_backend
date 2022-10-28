package com.mediatica.vouchain.servicesImpl;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mediatica.vouchain.config.Constants;
import com.mediatica.vouchain.dao.TransactionDaoImpl;
import com.mediatica.vouchain.dto.CompanyDTO;
import com.mediatica.vouchain.dto.TransactionDTO;
import com.mediatica.vouchain.entities.Transaction;
import com.mediatica.vouchain.servicesInterface.TransactionServiceInterface;

// TODO: Auto-generated Javadoc
/**
 * The Class TransactionServiceImpl.
 */
@Service
@Transactional
@PropertySource("file:${vouchain_home}/configurations/config.properties")
@Scope( proxyMode = ScopedProxyMode.TARGET_CLASS )
public class TransactionServiceImpl implements TransactionServiceInterface{
	
	/** The transaction dao impl. */
	@Autowired
	TransactionDaoImpl transactionDaoImpl;

	/**
	 * Transactions list.
	 *
	 * @param usrId the usr id
	 * @param profile the profile
	 * @param startDate the start date
	 * @param endDate the end date
	 * @param listForExport the list for export
	 * @return the list
	 * @throws ParseException the parse exception
	 */
	//method for retrieve the transaction list for a generic user in a specific date range
	public List<TransactionDTO> transactionsList(String usrId, String profile, String startDate, String endDate, boolean listForExport) throws ParseException {
		List<TransactionDTO> response = null;					
		List<Transaction> list = transactionDaoImpl.getTransactionList(usrId, profile, mapToTimestamp(startDate), mapToTimestamp(endDate));		
		if(list!=null) {
			response = new ArrayList<TransactionDTO>();
			Long trcValue = null;
			for(Transaction transaction : list) {
				TransactionDTO dto = new TransactionDTO();				
				dto = (TransactionDTO)dto.wrap(transaction, listForExport);
				response.add(dto);				
			}
		}				
		return response;
	}


	/**
	 * Transactions detail.
	 *
	 * @param usrId the usr id
	 * @param trc_id the trc id
	 * @return the transaction DTO
	 */
	//method for retrieve a transaction detail by the User ID and the transaction ID
	@Override
	public TransactionDTO transactionsDetail(String usrId, String trc_id) {				
		TransactionDTO result = transactionDaoImpl.getTransactionDetail(usrId, trc_id);			
		return result;
	}
	


	/**
	 * Company order list.
	 *
	 * @param usrId the usr id
	 * @param profile the profile
	 * @param startDate the start date
	 * @param endDate the end date
	 * @param payed the payed
	 * @param state the state
	 * @return the list
	 * @throws ParseException the parse exception
	 */
	//method for retrieve the order transaction list for a company
	@Override
	public List<TransactionDTO> companyOrderList(String usrId, String profile, String startDate, String endDate,
			String payed, String state) throws ParseException {
		List<TransactionDTO> response = null;
				
		List<Transaction> list = transactionDaoImpl.getCompanyOrderList(usrId, profile, mapToTimestamp(startDate), mapToTimestamp(endDate), managePayed(payed), manageState(state));		
		if(list!=null) {
			response = new ArrayList<TransactionDTO>();
			Long trcValue = null;
			for(Transaction transaction : list) {
				TransactionDTO dto = new TransactionDTO();				
				dto = (TransactionDTO)dto.wrap(transaction, false);
				response.add(dto);				
			}
		}				
		return response;
	}
	


	@Override
	public List<TransactionDTO> redeemedOrderList(String usrId, String profile, String startDate, String endDate,
			String payed, String state) throws ParseException {
		List<TransactionDTO> response = null;
		
		List<Transaction> list = transactionDaoImpl.getRedeemedOrderList(usrId, profile, mapToTimestamp(startDate), mapToTimestamp(endDate), managePayed(payed), manageState(state));		
		if(list!=null) {
			response = new ArrayList<TransactionDTO>();
			Long trcValue = null;
			for(Transaction transaction : list) {
				TransactionDTO dto = new TransactionDTO();				
				dto = (TransactionDTO)dto.wrap(transaction, false);
				response.add(dto);				
			}
		}				
		return response;	}

	//---------------------UTILITIES---------------------	
	

	/**
	 * Manage state.
	 *
	 * @param state the state
	 * @return the string
	 */
	private String manageState(String state) {
		if(state!=null && state.equalsIgnoreCase(Constants.TRANSACTION_STATUS_CONFIRMED_STRING)) {
			return "1";
		}
		else if(state!=null && state.equalsIgnoreCase(Constants.TRANSACTION_STATUS_PENDING_STRING)) {
			return "0";
		}
		return null;
	}


	/**
	 * Manage payed.
	 *
	 * @param payed the payed
	 * @return the string
	 */
	private String managePayed(String payed) {
		if(payed!=null && payed.equalsIgnoreCase(Constants.TRANSACTION_PAYED_STRING)) {
			return "1";
		}
		else if(payed!=null && payed.equalsIgnoreCase(Constants.TRANSACTION_NOT_PAYED_STRING)) {
			return "0";
		}
		return null;
	}


	/**
	 * Map to timestamp.
	 *
	 * @param dataString the data string
	 * @return the timestamp
	 * @throws ParseException the parse exception
	 */
	public Timestamp mapToTimestamp(String dataString) throws ParseException {		

		if(dataString!=null) {
		    SimpleDateFormat dateFormat = Constants.FORMATTER_YYYY_MM_DD;
		    dateFormat.setLenient(false);

		    Date parsedDate = dateFormat.parse(dataString);
		    Timestamp timestamp = new java.sql.Timestamp(parsedDate.getTime());
		    return timestamp;

		}		
		return null;		
	}


	/**
	 * Company order del.
	 *
	 * @param usrId the usr id
	 * @param trcId the trc id
	 * @return the transaction DTO
	 */
	//[BE_31] CompanyOrderDel
	@Override
	public String companyOrderDel(String trcId) {
		String result = transactionDaoImpl.companyOrderDel(trcId);			
		return result;
	}


	@Override
	public byte[] getInvoice(String trcId, String type) {
		byte[] invoice=null;
		Transaction transaction =  new Transaction();
		transaction = transactionDaoImpl.findByPrimaryKey(transaction, Integer.parseInt(trcId));
		if(type!=null && transaction!=null && type.equalsIgnoreCase("pdf")) {
			invoice = transaction.getTrcInvoice();
		}else if(type!=null && transaction!=null &&  type.equalsIgnoreCase("xml")) {
			invoice = transaction.getTrcInvoiceXml();
		}
		return invoice;
	}





	
}
