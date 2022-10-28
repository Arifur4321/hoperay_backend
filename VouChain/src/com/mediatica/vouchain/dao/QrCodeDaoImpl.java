package com.mediatica.vouchain.dao;

import java.util.List;

import javax.persistence.NoResultException;

import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Repository;

import com.mediatica.vouchain.dto.MerchantDTO;
import com.mediatica.vouchain.entities.Merchant;
import com.mediatica.vouchain.entities.QrCode;
import com.mediatica.vouchain.entities.User;

/**
 * 
 * @author Luca Gulinelli
 *
 */
@Repository
@Scope( proxyMode = ScopedProxyMode.TARGET_CLASS )
public class QrCodeDaoImpl extends DaoImpl<QrCode>{

	@Override
	public List<QrCode> selectAll() {
		// TODO Auto-generated method stub
		return null;
	}

	public QrCode save(QrCode qrCode) {
		Session session = sessionFactory.getCurrentSession();
		try {
			session.save(qrCode);
		}catch (NoResultException nre){
			//Ignore this because as per your logic this is ok!
		}
		return qrCode ;
	}
	
	public QrCode delete(QrCode qrCode) {
		Session session = sessionFactory.getCurrentSession();
		try {
			session.delete(qrCode);
		}catch (NoResultException nre){
			//Ignore this because as per your logic this is ok!
		}
		return qrCode ;
	}
	
	public QrCode findQrCodeByMrcCash(Integer mrc,String cash) {
		QrCode qrCode = null;
		Session session = sessionFactory.getCurrentSession();
		Query query = session.createQuery("SELECT q " +
										  "FROM QrCode q " +
										  "WHERE q.qrMrcId =:id "
										+ "AND q.qrCash =:cash ");
		query.setParameter("id", mrc);
		query.setParameter("cash", Integer.parseInt(cash));
		try {
			qrCode = (QrCode)query.uniqueResult();
		}catch (NoResultException nre){
			//Ignore this because as per your logic this is ok!
		}
		return qrCode;
	}
	
	public QrCode findQrCodeByValue(String value) {
		QrCode qrCode = null;
		Session session = sessionFactory.getCurrentSession();
		Query query = session.createQuery("SELECT q " +
										  "FROM QrCode q " +
										  "WHERE q.qrValue =:value ");
		System.out.println("VALUEEEEEEEEEEEEEEEEEE"+value);
		query.setParameter("value", value);
		try {
			qrCode = (QrCode)query.uniqueResult();
		}catch (NoResultException nre){
			//Ignore this because as per your logic this is ok!
		}
		return qrCode;
	}
	public List<QrCode> findQrCodeByMerchant(Integer id){
		Session session = sessionFactory.getCurrentSession();
		//TODO Query da controllare
		Query query = session.createQuery("SELECT q " +
				  "FROM QrCode q " +
				  "WHERE q.qrMrcId =:id ");
		query.setParameter("id", id);
		List<QrCode> result = null;
		try {
			result = (List<QrCode>)query.getResultList();
		}catch (NoResultException nre){
			//Ignore this because as per your logic this is ok!
		}
		return result ;
		
	}

}
