package com.mediatica.vouchain.dao;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Repository;

import com.mediatica.vouchain.entities.Company;
/**
 * 
 * @author Pietro Napolitano
 *
 */
@Repository
@Scope( proxyMode = ScopedProxyMode.TARGET_CLASS )
public class CompanyDaoImpl extends DaoImpl<Company>{
	@Autowired
	SessionFactory sessionFactory;
	

	@Override
	public List<Company> selectAll() {
		// TODO Auto-generated method stub
		return null;
	}
}
