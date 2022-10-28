package com.mediatica.vouchain.dao;

import java.util.List;

import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Repository;

import com.mediatica.vouchain.entities.Tbregioni;
import com.mediatica.vouchain.entities.User;

/**
 * 
 * @author Pietro Napolitano
 *
 */
@Repository
@Scope( proxyMode = ScopedProxyMode.TARGET_CLASS )
public class RegionsDaoImpl extends  DaoImpl<Tbregioni> {

	@Override
	public List<Tbregioni> selectAll() {
		// TODO Auto-generated method stub
		return null;
	}

}
