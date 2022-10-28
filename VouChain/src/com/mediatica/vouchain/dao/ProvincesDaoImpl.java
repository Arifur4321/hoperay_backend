package com.mediatica.vouchain.dao;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Repository;

import com.mediatica.vouchain.entities.Tbprovince;

/**
 * 
 * @author Pietro Napolitano
 *
 */
@Repository
@Scope( proxyMode = ScopedProxyMode.TARGET_CLASS )
public class ProvincesDaoImpl extends  DaoImpl<Tbprovince> {

	@Override
	public List<Tbprovince> selectAll() {
		Session session = sessionFactory.getCurrentSession();
		Query query = session.createNamedQuery("Tbprovince.findAll");
		return (List<Tbprovince>)query.getResultList();
	}

}
