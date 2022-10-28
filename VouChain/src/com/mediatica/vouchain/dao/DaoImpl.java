package com.mediatica.vouchain.dao;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.mediatica.vouchain.exceptions.SessionNotValidException;

/**
 * 
 * @author Pietro Napolitano
 *
 * @param <E>
 */
public  abstract class DaoImpl<E> implements Dao<E>{

	@Autowired
	SessionFactory sessionFactory;
	

	
	private static org.slf4j.Logger log = LoggerFactory.getLogger(DaoImpl.class);

	@Override
	public boolean insert(E element) {
		boolean flag = true;
			Session session = sessionFactory.getCurrentSession();
			session.persist(element);
			session.flush();
		return flag; 
	}
	
	
	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT)
	public void insertTransactional(E element) {
		Session session = sessionFactory.getCurrentSession();
		session.save(element);
	}
	
	
	@SuppressWarnings("unchecked")
	@Override
	public  E findByPrimaryKey(E o,Integer id){
		E element = null;
			Session session = sessionFactory.getCurrentSession();
			if(o!=null){
				element =  (E) session.get(o.getClass(), id);
			}
		return element; 
		
	}

	@Override
	public void update(E element) {
			Session session = sessionFactory.getCurrentSession();
			session.merge(element);
			session.flush();

	}
	


	
	
	
	@Override
	public boolean updateTransactional(E element,Session session) {
		boolean flag = true;
		session.merge(element);
		return flag;
	}
	
	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT)
	public void updateTransactional(E element) {
		Session session = sessionFactory.getCurrentSession();
		session.merge(element);
	}



	@Override
	public void removePhysical(E element) {
			Session session = sessionFactory.getCurrentSession();
			session.delete(element);
			session.flush();
	}
	
	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT)	
	public void removePhysicalTransactional(E element) {
			Session session = sessionFactory.getCurrentSession();
			session.delete(element);
	}
	
	
	
	@Override
	public boolean removeLogical(E element)  {
		log.error("method is not supported");
		return false;
	}
	
	
	
	
	@Override
	public Session openSession(){
		return sessionFactory.openSession();
		
	}
	
	
	public boolean closeSession(Session session){
		boolean flag=false;
		try{
			if(session.isOpen()){
				session.close();
				flag=true;
			}
		}catch(Exception e){
		//	log.error(e,e);
			flag=false;
		}
		return flag;
	}
	
	
	@Override
	public void insertTransactional(E element,Session session) {
		session.save(element);
	}

	public Session getCurrentSession() {
		return sessionFactory.getCurrentSession();
	}
	
	@Override
	public Transaction beginTransaction(Session session) throws SessionNotValidException{
		if(!session.isOpen()||!session.isConnected()){
			throw new SessionNotValidException("Sessione non valida");
		}
		Transaction transaction = session.beginTransaction();
		return transaction;
	}
	
	
	@Override
	public boolean commitTransaction(Transaction transaction){
		boolean flag = false;
		try{
			transaction.commit();
			flag=true;
		}catch(Exception e){
			e.printStackTrace();
			flag=false;
		}
		return flag;
	}
	
	
	@Override
	@Deprecated
	public boolean rollbackTransaction(Transaction transaction){
		boolean flag = false;
		try{
			transaction.rollback();
			flag=true;
		}catch(Exception e){
			e.printStackTrace();
			flag=false;
		}
		return flag;
	}
	
	
	



}
