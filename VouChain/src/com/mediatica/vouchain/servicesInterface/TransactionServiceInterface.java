package com.mediatica.vouchain.servicesInterface;

import java.text.ParseException;
import java.util.List;

import com.mediatica.vouchain.dto.TransactionDTO;
import com.mediatica.vouchain.entities.Transaction;

// TODO: Auto-generated Javadoc
/**
 * The Interface TransactionServiceInterface.
 */
public interface TransactionServiceInterface {

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
	public List<TransactionDTO> transactionsList(String usrId, String profile, String startDate, String endDate, boolean listForExport) throws ParseException;

	/**
	 * Transactions detail.
	 *
	 * @param usrId the usr id
	 * @param trc_id the trc id
	 * @return the transaction DTO
	 */
	public TransactionDTO transactionsDetail(String usrId, String trc_id);

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
	public List<TransactionDTO> companyOrderList(String usrId, String profile, String startDate, String endDate,
			String payed, String state) throws ParseException;

	/**
	 * Company order del.
	 *
	 * @param trcId the trc id
	 * @return the string
	 */
	public String companyOrderDel(String trcId);

	/**
	 * Redeemed order list.
	 *
	 * @param usrId the usr id
	 * @param profile the profile
	 * @param startDate the start date
	 * @param endDate the end date
	 * @param payed the payed
	 * @param state the state
	 * @return the list
	 * @throws ParseException 
	 */
	public List<TransactionDTO> redeemedOrderList(String usrId, String profile, String startDate, String endDate,
			String payed, String state) throws ParseException;

	public byte[] getInvoice(String trcId, String type);


}
