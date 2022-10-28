package com.mediatica.vouchain.dao;

import java.util.Date;
import java.util.List;

import javax.persistence.NoResultException;

import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Repository;

import com.mediatica.vouchain.dto.VoucherDTO;
import com.mediatica.vouchain.entities.Merchant;
import com.mediatica.vouchain.entities.Voucher;

@Repository
@Scope( proxyMode = ScopedProxyMode.TARGET_CLASS )
public class VoucherDaoImpl extends DaoImpl<Voucher>{

	@Override
	public List<Voucher> selectAll() {
		// TODO Auto-generated method stub
		return null;
	}

	public List<Voucher> getPurchasableVoucherList(Date today) {
		Session session = sessionFactory.getCurrentSession();
		Query query = session.createQuery("SELECT vou "
										+ "FROM Voucher vou "
										+ "WHERE vou.vchEndDate >= :today "
										+ "ORDER BY vou.vchValue ASC,vou.vchEndDate DESC");
		query.setParameter("today", today);
		List<Voucher> result = null;
		try {
			result = (List<Voucher>)query.getResultList();
		}catch (NoResultException nre){
			//Ignore this because as per your logic this is ok!
		}
		return result ;
	}

	public Voucher findByName(String vchName) {
		Session session = sessionFactory.getCurrentSession();
		Query query = session.createQuery("SELECT v "
				+ 						    "FROM Voucher v "
										  + "WHERE v.vchName = :vchName ");
		query.setParameter("vchName", vchName.trim());
		Voucher result = null;
		try {
			result = (Voucher)query.getSingleResult();
		}catch (NoResultException nre){
			//Ignore this because as per your logic this is ok!
			System.out.println("here: ");
		}
		return result ;	
		}

	public List<Voucher> getExpendableVouchersList(int employeeId) {
		return null;
	}
	
}
