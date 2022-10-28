package com.mediatica.vouchain.dao;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Repository;

import com.mediatica.vouchain.config.Constants.ENTITIES_TYPE;
import com.mediatica.vouchain.config.enumerations.TransactionType;
import com.mediatica.vouchain.dto.TransactionDTO;
import com.mediatica.vouchain.dto.VoucherDTO;
import com.mediatica.vouchain.entities.Transaction;

// TODO: Auto-generated Javadoc
/**
 * The Class TransactionDaoImpl.
 *
 * @author Pietro Napolitano
 */
@Repository
@Scope( proxyMode = ScopedProxyMode.TARGET_CLASS )
@PropertySource("file:${vouchain_home}/configurations/config.properties")
public class TransactionDaoImpl extends DaoImpl<Transaction>{

	
	@Value("${transaction_not_found}")
	private String transactionNotFound;
	
	@Value("${transaction_payed}")
	private String transactionPayed;
	
	/**
	 * Select all.
	 *
	 * @return the list
	 */
	@Override
	public List<Transaction> selectAll() {
		// TODO Auto-generated method stub
		return null;
	}
	
	/**
	 * Gets the last transaction.
	 *
	 * @param usrId the usr id
	 * @return the last transaction
	 */
	public Transaction getLastTransaction(Integer usrId) {
		List<Transaction> transactionList = new ArrayList<Transaction>();
		Session session = sessionFactory.getCurrentSession();
		Query query = session.createQuery("FROM Transaction where usr_id_da = :usrId or usr_id_a = :usrId");
		query.setParameter("usrId", usrId);

		transactionList = query.getResultList();
		if(transactionList!=null && transactionList.size()>0) {
			return transactionList.get(transactionList.size()-1);
		}
		return null;
	}

	/**
	 * Extract all the transaction CONFIRMED trc_state=1 .
	 *
	 * @param usrId the usr id
	 * @param profile the profile
	 * @param startDate the start date
	 * @param endDate the end date
	 * @return the transaction list by user id
	 */
	//method for retrieve the transaction list for a generic user in a specific date range
	public List<Transaction> getTransactionList(String usrId, String profile, Timestamp startDate, Timestamp endDate) {
		
		List<Transaction> transactionList = new ArrayList<Transaction>();
		Session session = sessionFactory.getCurrentSession();
		
		StringBuilder queryString = new StringBuilder(" FROM Transaction ");
		
		//queryString.append(" where trc_state=1");
		queryString.append(" where 1=1");
		
		queryString.append(" and trc_canc_date is null ");

		
		if(usrId!=null) {
			queryString.append(" and (usr_id_da = :usrId or usr_id_a = :usrId) ");
		}
		
		if(profile!=null && profile.equalsIgnoreCase(ENTITIES_TYPE.COMPANY.toString())) {			
			queryString.append(" and (trc_type = '"+ TransactionType.COMPANY_BUY_VOUCHER.getDescription()  +"' or trc_type = '"+TransactionType.COMPANY_GIVE_VOUCHER_TO_EMPLOYEE.getDescription()+"' or trc_type = '"+TransactionType.NEW_VOUCHER_TYPE.getDescription()+"') ");
		}
		else if(profile!=null && profile.equalsIgnoreCase(ENTITIES_TYPE.EMPLOYEE.toString())) {			
			queryString.append(" and (trc_type = '"+TransactionType.COMPANY_GIVE_VOUCHER_TO_EMPLOYEE.getDescription()+"' or trc_type = '"+TransactionType.EMPLOYEE_GIVE_VOUCHER_TO_MERCHANT.getDescription()+"') ");
		}
		else if(profile!=null && profile.equalsIgnoreCase(ENTITIES_TYPE.MERCHANT.toString())) {			
			queryString.append(" and (trc_type = '"+TransactionType.EMPLOYEE_GIVE_VOUCHER_TO_MERCHANT.getDescription()+"' or trc_type = '"+TransactionType.REDEEM_VOUCHER.getDescription()+"') ");
		}		
		//if dates are != null add dates to query
		if(startDate!=null) {
			queryString.append(" and trc_date >= :startDate ");
		}
		if(endDate!=null) {
			queryString.append(" and trc_date <= :endDate ");
		}		
		queryString.append(" order by trc_date DESC ");
		
		Query query = session.createQuery(queryString.toString());		
		
		if(usrId!=null) {
			query.setParameter("usrId", usrId);	
		}
		
		if(startDate!=null) {
			query.setParameter("startDate", startDate);
		}
		if(endDate!=null) {
			query.setParameter("endDate", setEndOfTheDay(endDate));
		}		
		transactionList = query.getResultList();			
		return transactionList;		
	}
	

	/**
	 * Gets the company order list.
	 *
	 * @param usrId the usr id
	 * @param profile the profile
	 * @param startDate the start date
	 * @param endDate the end date
	 * @param payed the payed
	 * @param state the state
	 * @return the company order list
	 */
	//method for retrieve the order transaction list for a company
	public List<Transaction> getCompanyOrderList(String usrId, String profile, Timestamp startDate,
			Timestamp endDate, String payed, String state) {
		
		List<Transaction> transactionList = new ArrayList<Transaction>();
		Session session = sessionFactory.getCurrentSession();
		
		StringBuilder queryString = new StringBuilder(" FROM Transaction ");
				
		queryString.append(" where (trc_type = '"+ TransactionType.COMPANY_BUY_VOUCHER.getDescription()  +"' or trc_type = '"+TransactionType.NEW_VOUCHER_TYPE.getDescription()+"') ");


		if(state!=null) {
			queryString.append(" and trc_state= :trc_state");
		}		
		if(payed!=null) {
			queryString.append(" and trc_payed= :trc_payed");
		}		
		if(usrId!=null) {
			queryString.append(" and usr_id_a = :usrId ");
		}		
		//if dates are != null add dates to query
		if(startDate!=null) {
			queryString.append(" and trc_date >= :startDate ");
		}
		if(endDate!=null) {
			queryString.append(" and trc_date <= :endDate ");
		}		
		queryString.append(" order by trc_date desc");
		
		Query query = session.createQuery(queryString.toString());		
		
		if(state!=null) {
			query.setParameter("trc_state", state);	
		}			
		if(payed!=null) {
			query.setParameter("trc_payed", payed);	
		}	
		if(usrId!=null) {
			query.setParameter("usrId", usrId);	
		}		
		if(startDate!=null) {
			query.setParameter("startDate", startDate);
		}
		if(endDate!=null) {
			query.setParameter("endDate", setEndOfTheDay(endDate));
		}	
	
		
		transactionList = query.getResultList();			
		return transactionList;	
	}


	public List<Transaction> getRedeemedOrderList(String usrId, String profile, Timestamp startDate,
			Timestamp endDate, String payed, String state) {
		
		
		List<Transaction> transactionList = new ArrayList<Transaction>();
		Session session = sessionFactory.getCurrentSession();
		
		StringBuilder queryString = new StringBuilder(" FROM Transaction ");
				
		queryString.append(" where trc_type = '"+ TransactionType.REDEEM_VOUCHER.getDescription()  +"'");


		if(state!=null) {
			queryString.append(" and trc_state= :trc_state");
		}		
		if(payed!=null) {
			queryString.append(" and trc_payed= :trc_payed");
		}		
		if(usrId!=null) {
			queryString.append(" and usr_id_da = :usrId ");
		}		
		//if dates are != null add dates to query
		if(startDate!=null) {
			queryString.append(" and trc_date >= :startDate ");
		}
		if(endDate!=null) {
			queryString.append(" and trc_date <= :endDate ");
		}		
		queryString.append(" order by trc_date");
		
		Query query = session.createQuery(queryString.toString());		
		
		if(state!=null) {
			query.setParameter("trc_state", state);	
		}			
		if(payed!=null) {
			query.setParameter("trc_payed", payed);	
		}	
		if(usrId!=null) {
			query.setParameter("usrId", usrId);	
		}		
		if(startDate!=null) {
			query.setParameter("startDate", startDate);
		}
		if(endDate!=null) {
			query.setParameter("endDate", setEndOfTheDay(endDate));
		}		
		transactionList = query.getResultList();			
		return transactionList;	
		
	}
	
	

	/**
	 * Gets the transaction detail.
	 *
	 * @param usrId the usr id
	 * @param trcid the trcid
	 * @return the transaction detail
	 */
	//method for retrieve a transaction detail by the User ID and the transaction ID
	public TransactionDTO getTransactionDetail(String usrId, String trcid) {
		
		Session session = sessionFactory.getCurrentSession();
		
		Query query = session.createQuery(
				"SELECT td.transactionDetailPK.trcId, td.transactionDetailPK.vchName, v.vchEndDate, v.vchValue, td.trdQuantity "
				+ " FROM TransactionDetail td, Voucher v, Transaction t "
				+ " WHERE td.transactionDetailPK.vchName = v.vchName"
				+ " AND td.transactionDetailPK.trcId = t.trcId"
				+ " AND td.transactionDetailPK.trcId = :trcId" 
				+ " AND (t.usrIdDa.usrId = :usrId OR t.usrIdA.usrId = :usrId)");
		
		query.setParameter("trcId", Integer.valueOf(trcid));	
		query.setParameter("usrId", Integer.valueOf(usrId));
		
		List<Object[]> result = (List<Object[]>) query.getResultList();
		
		TransactionDTO dto = mapFromObject(result);
		
		return dto;
		
	}
	

	public List<Transaction> getTransactionListByPayedandType(boolean transactionPayed2, String type) {
		List<Transaction> transactionList = new ArrayList<Transaction>();
		Session session = sessionFactory.getCurrentSession();
		
		StringBuilder queryString = new StringBuilder(" FROM Transaction ");
		
		queryString.append(" where (trc_payed = :trc_payed OR trc_payed IS NULL) ");	
		queryString.append(" and trc_canc_date IS NULL ");
		if(type!=null) {
			queryString.append(" and trc_type = :trc_type ");
		}
		queryString.append(" order by trc_date");		
		
		
		Query query = session.createQuery(queryString.toString());	
		
		
		query.setParameter("trc_payed", transactionPayed2);			
		if(type!=null) {
			query.setParameter("trc_type", type);
		}

		transactionList = query.getResultList();			
		return transactionList;		
	}

	

	/**
	 * Company order del.
	 *
	 * @param usrId the usr id
	 * @return the transaction DTO
	 */
	public String companyOrderDel(String trcId) {
		
		Transaction transaction = new Transaction();		
		transaction = findByPrimaryKey(transaction, Integer.valueOf(trcId));
		
		if(transaction!=null) {
			if(!transaction.getTrcPayed()) {
				transaction.setTrcCancDate(new Date());
				update(transaction);				
			}
			else {
				return transactionPayed;
			}
		}
		else {
			return transactionNotFound;
		}
		
		return null;
	}
	
	
	
	public List<Transaction> getPendingTransactions(Date date, long delay) {
		
		List<Transaction> transactionList = new ArrayList<Transaction>();		
		Session session = sessionFactory.getCurrentSession();
		
		StringBuilder queryString = new StringBuilder(" FROM Transaction ");		
		queryString.append(" where (trc_state=0 OR trc_state IS NULL)");
		queryString.append(" and trc_canc_date IS NULL ");
		queryString.append(" and trc_type <> 'ACV' ");
		
		if(date!=null) {
			queryString.append(" and trc_date < :datePlusDelay ");
		}		
		queryString.append(" order by trc_date");		
		
		Query query = session.createQuery(queryString.toString());			
		
		if(date!=null) {
			Date datePlusDelay = addDelayToDate(date, delay);	
			query.setParameter("datePlusDelay", datePlusDelay);
		}		
		transactionList = query.getResultList();			
		return transactionList;		
	}
	
	public List<Transaction> getPendingTransactions(Date date, long delay,Session session) {
		
		List<Transaction> transactionList = new ArrayList<Transaction>();		
		
		
		StringBuilder queryString = new StringBuilder(" FROM Transaction ");		
		queryString.append(" where trc_state=0 ");			
		if(date!=null) {
			queryString.append(" and trc_date < :datePlusDelay ");
		}		
		queryString.append(" order by trc_date");		
		
		Query query = session.createQuery(queryString.toString());			
		
		if(date!=null) {
			Date datePlusDelay = addDelayToDate(date, delay);		
			query.setParameter("datePlusDelay", datePlusDelay);
		}		
		transactionList = query.getResultList();			
		return transactionList;		
	}

	/**
	 * Map from object.
	 *
	 * @param resultList the result list
	 * @return the transaction DTO
	 */
	private TransactionDTO mapFromObject(List<Object[]> resultList) {
		
		TransactionDTO dto = new TransactionDTO();
		List<VoucherDTO> voucherList = new ArrayList<VoucherDTO>();
		dto.setVoucherList(voucherList);
		
		long trcValue = 0;

		if(resultList!=null && resultList.size()>0) {
			for(Object[] result : resultList) {
				VoucherDTO voucherDTO = new VoucherDTO();
				
				double vchValue = 0;
				double trdQuantity = 0;				
				
				if(result[3]!=null) {
					vchValue = Double.parseDouble(result[3].toString());
				}
				if(result[4]!=null) {
					trdQuantity = Double.parseDouble(result[4].toString());
				}
				
				voucherDTO.setVchValue(String.valueOf(vchValue));
				voucherDTO.setVchQuantity(String.valueOf(trdQuantity));	
				trcValue+= vchValue * trdQuantity;	
				if(result[1]!=null) {
					voucherDTO.setVchName(result[1].toString());
				}
				if(result[2]!=null) {
					voucherDTO.setVchEndDate(result[2].toString());	
				}
				if(result[0]!=null) {
					dto.setTrcId(result[0].toString());			
				}
				
				dto.getVoucherList().add(voucherDTO);					
			}
			
			dto.setTrcValue(String.valueOf(trcValue));

		}

		return dto;
	}

	
	

	private Timestamp setEndOfTheDay(Timestamp endDate) {

	    //Calendar based on ts          
	    Calendar cTs=Calendar.getInstance();
	    cTs.setTimeInMillis(endDate.getTime());
	    
	    cTs.set(Calendar.HOUR_OF_DAY, 23);
	    cTs.set(Calendar.MINUTE, 59);
	    cTs.set(Calendar.SECOND, 59);

	    //set value of ts based on the modified cTs
	    endDate.setTime(cTs.getTimeInMillis());
	    System.out.println(endDate.getTime());	    
	    return endDate;
	}
	

	private Date addDelayToDate(Date date, long delay) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.SECOND, (int)delay*(-1));
		Date datePlusDelay = calendar.getTime();
		return datePlusDelay;
	}
	
	public void update2(Transaction element,String toUpdate) {
		Session session = sessionFactory.getCurrentSession();
		Query query = session.createQuery("update Transaction "
				+ " set trcTxId = :toUpdate "
				+ " where trcId = :id");
		
		query.setParameter("toUpdate", toUpdate);
		query.setParameter("id", element.getTrcId());
		
		int numberoOrSessionCleande = query.executeUpdate();
	}

	public void removePhysical2(Transaction element) {
		Session session = sessionFactory.getCurrentSession();
		Query query = session.createQuery("delete Transaction "
				+ " where trcId = :id");
		query.setParameter("id", element.getTrcId());
		
		int numberoOrSessionCleande = query.executeUpdate();
	}
	
	public void removeTransactionsDetails(Transaction element) {
		Session session = sessionFactory.getCurrentSession();
		Query query = session.createQuery("delete TransactionDetail "
				+ " where transactionDetailPK.trcId = :id");
		query.setParameter("id", element.getTrcId());
		
		int numberoOrSessionCleande = query.executeUpdate();
	}



}
