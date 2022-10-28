package com.mediatica.vouchain.dao;



import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.persistence.NoResultException;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.mediatica.vouchain.entities.User;

/**
 * 
 * @author Pietro Napolitano
 *
 */
@Repository
@Transactional
@Scope( proxyMode = ScopedProxyMode.TARGET_CLASS )
public class UserDaoImpl extends DaoImpl<User>{

	@Autowired
	SessionFactory sessionFactory;
	
	/**
	 * Finds an user by UserName and Password
	 * 
	 * @param email
	 * @param password
	 * @return
	 */
	public User findUserByEmailPassword(String email,String password) {
		User user = null;
		Session session = sessionFactory.getCurrentSession();
		Query query = session.createQuery("SELECT u " +
										  "FROM User u " +
										  "WHERE u.usrEmail =:email "
										+ "AND u.usrPassword =:password ");
		query.setParameter("email", email);
		query.setParameter("password", password);
		try {
			user = (User)query.uniqueResult();
		}catch (NoResultException nre){
			//Ignore this because as per your logic this is ok!
		}
		return user;
	}

	
	//@Override
	public List<User> selectAll() {
		// TODO Auto-generated method stub
		return null;
	}


	public User findUserByEmail(String p_email) {
		User user = null;
		Session session = sessionFactory.getCurrentSession();
		Query query = session.createQuery("SELECT u " +
										  "FROM User u " +
										  "WHERE u.usrEmail =:email ");
		query.setParameter("email", p_email);
		try {
			user = (User)query.uniqueResult();
		}catch (NoResultException nre){
			//Ignore this because as per your logic this is ok!
		}
		return user;	
	}


	public User findUserByRecoveryCode(String resetCode) {
		User user = null;
		Session session = sessionFactory.getCurrentSession();
		Query query = session.createQuery("SELECT u " +
										  "FROM User u " +
										  "WHERE u.usrRecoverEmailCode =:resetCode ");
		query.setParameter("resetCode", resetCode);
		try {
			user = (User)query.uniqueResult();
		}catch (NoResultException nre){
			//Ignore this because as per your logic this is ok!
		}
		return user;
	}
	
	public User findUserById(Integer id) {
		User user = null;
		Session session = sessionFactory.getCurrentSession();
		Query query = session.createQuery("SELECT u " +
										  "FROM User u " +
										  "WHERE u.usrId =:id ");
		query.setParameter("id", id);
		try {
			user = (User)query.uniqueResult();
		}catch (NoResultException nre){
			//Ignore this because as per your logic this is ok!
		}
		return user;
	}
	
	public List<User> getUserByLastInvocationDate(Date date) {
		List<User> userList = new ArrayList<User>();
		Session session = sessionFactory.getCurrentSession();
		Query query = session.createQuery("SELECT u " +
										  "FROM User u " +
										  "WHERE u.lastInvocationDate >= :lastInvocationDate ");
		query.setParameter("lastInvocationDate", date);
		try {
			userList = query.getResultList();
		}catch (NoResultException nre){
			//Ignore this because as per your logic this is ok!
		}
		return userList;	
	}


	public int cleanSessions(String cleanSessionTimeInMinutes) {		
		Session session = sessionFactory.getCurrentSession();		
		Query query = session.createQuery("update User "
				+ " set last_invocation_date=null, "
				+ " usr_session=null "
				+ " where last_invocation_date< :date");
		
		query.setParameter("date", removeDelayToDate(cleanSessionTimeInMinutes));		
		int numberoOrSessionCleande = query.executeUpdate();
		return numberoOrSessionCleande;
	}
	
	private Date removeDelayToDate(String cleanSessionTimeInMinutes) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Date());
		calendar.add(Calendar.MINUTE, -Integer.valueOf(cleanSessionTimeInMinutes));
		Date dateMinusDelay = calendar.getTime();
		return dateMinusDelay;
	}
	


}
