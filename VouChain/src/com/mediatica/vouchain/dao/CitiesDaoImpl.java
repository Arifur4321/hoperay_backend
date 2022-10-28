package com.mediatica.vouchain.dao;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Repository;

import com.mediatica.vouchain.entities.Tbcomuni;
import com.mediatica.vouchain.entities.Tbprovince;

/**
 * 
 * @author Pietro Napolitano
 *
 */
@Repository
@Scope( proxyMode = ScopedProxyMode.TARGET_CLASS ) 
public class CitiesDaoImpl extends  DaoImpl<Tbcomuni> {

	@Override
	public List<Tbcomuni> selectAll() {
		Session session = sessionFactory.getCurrentSession();
		Query query = session.createNamedQuery("Tbcomuni.findAll");
		return (List<Tbcomuni>)query.getResultList();
	}

	public List<Tbcomuni> getCitiesByProvince(String idProvince) {
		Session session = sessionFactory.getCurrentSession();
		Query query = session.createNamedQuery("Tbcomuni.findBySiglaprovinciaComune");
		query.setParameter("siglaprovinciaComune", idProvince);
		return (List<Tbcomuni>)query.getResultList();

	}

}
