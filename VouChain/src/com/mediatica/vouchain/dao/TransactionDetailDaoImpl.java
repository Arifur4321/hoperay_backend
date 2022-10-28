 package com.mediatica.vouchain.dao;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Repository;

import com.mediatica.vouchain.entities.Transaction;
import com.mediatica.vouchain.entities.TransactionDetail;

/**
 * 
 * @author Pietro Napolitano
 *
 */
@Repository
@Scope( proxyMode = ScopedProxyMode.TARGET_CLASS )
public class TransactionDetailDaoImpl extends DaoImpl<TransactionDetail> {

	@Override
	public List<TransactionDetail> selectAll() {
		// TODO Auto-generated method stub
		return null;
	}
	
	public List<TransactionDetail> findByTrcId(String trcId) {
		List<TransactionDetail> transactionDetailList = new ArrayList<TransactionDetail>();
		Session session = sessionFactory.getCurrentSession();		
		Query query = session.createQuery("FROM TransactionDetail where trc_id = :trcId");		
		query.setParameter("trcId", trcId);

		transactionDetailList = query.getResultList();
		if(transactionDetailList!=null && transactionDetailList.size()>0) {
			return transactionDetailList;
		}
		return null;
	}
	
	

}
