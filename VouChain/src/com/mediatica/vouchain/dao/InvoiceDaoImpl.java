package com.mediatica.vouchain.dao;

import java.util.List;

import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import com.mediatica.vouchain.entities.Invoice;

@Repository
@Scope( proxyMode = ScopedProxyMode.TARGET_CLASS )
public class InvoiceDaoImpl extends DaoImpl<Invoice>{

	@Override
	public List<Invoice> selectAll() {
		// TODO Auto-generated method stub
		return null;
	}

}
