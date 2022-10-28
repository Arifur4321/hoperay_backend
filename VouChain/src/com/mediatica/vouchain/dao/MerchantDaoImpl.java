package com.mediatica.vouchain.dao;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;

import javax.persistence.NoResultException;

import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Repository;

import com.mediatica.vouchain.dto.LocalizationDTO;
import com.mediatica.vouchain.entities.Merchant;

/**
 * 
 * @author Pietro Napolitano
 *
 */
@Repository
@Scope( proxyMode = ScopedProxyMode.TARGET_CLASS )
public class MerchantDaoImpl extends DaoImpl<Merchant>{

	@Override
	public List<Merchant> selectAll() {
		// TODO Auto-generated method stub
		return null;
	}

	public List<Merchant> listAll() {
		Session session = sessionFactory.getCurrentSession();
		Query query = session.createQuery("SELECT mrc FROM Merchant mrc WHERE mrc.mrcChecked= 1 ORDER BY mrc.mrcRagioneSociale");
		List<Merchant> result = null;
		try {
			result = (List<Merchant>)query.getResultList();
		}catch (NoResultException nre){
			//Ignore this because as per your logic this is ok!
		}
		return result ;
	}
	
	public HashMap<Merchant, String> cercaMerchant(LocalizationDTO dto) {
		Session session = sessionFactory.getCurrentSession();
		Query<?> query = session.createNativeQuery("select mrc.*" + 
										" from merchant mrc" + 
										" where ST_Distance_Sphere(POINT(:latitude,:longitude),POINT(mrc.mrc_latitude,mrc.mrc_longitude))/1000  < :km")
				.addEntity(Merchant.class);
		Query queryDistanza = session.createNativeQuery("SELECT ST_Distance_Sphere(POINT(mrc.mrc_latitude,mrc.mrc_longitude),POINT(:latitude,:longitude))/1000" + 
													  " from merchant mrc" + 
													  " where ST_Distance_Sphere(POINT(mrc.mrc_latitude,mrc.mrc_longitude),POINT(:latitude,:longitude))/1000 < :km");
		HashMap<Merchant, String> map=new HashMap<Merchant, String>();
		query.setParameter("longitude", new BigDecimal(dto.getLongitude()));
		System.out.println("LONGITUDINE__________: "+dto.getLongitude());
		query.setParameter("latitude", new BigDecimal(dto.getLatitude()));
		System.out.println("LATITUDINE___________: "+dto.getLatitude());
		query.setParameter("km", new BigDecimal(dto.getRaggio()));
		queryDistanza.setParameter("longitude", new BigDecimal(dto.getLongitude()));
		queryDistanza.setParameter("latitude", new BigDecimal(dto.getLatitude()));
		queryDistanza.setParameter("km", new BigDecimal(dto.getRaggio()));
		
		try {
			List<Merchant> merchants = (List<Merchant>) query.getResultList();
			List<Double> distanze = queryDistanza.getResultList();
			
			if(merchants.size()==distanze.size()) {
				for (int i = 0; i < merchants.size(); i++) {
					 map.put(merchants.get(i), distanze.get(i).toString());
				}
			}
		}catch (NoResultException nre){
			//Ignore this because as per your logic this is ok!
		}
		return map;
	}

}
