package com.mediatica.vouchain.dao;

import java.math.BigDecimal;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.Transaction;

import com.mediatica.vouchain.exceptions.SessionNotValidException;

/**
 * 
 * @author Pietro Napolitano
 *
 * @param <E>
 */
public interface Dao <E>{

	/**
	 * Returns teh list of items
	 * 
	 * @return
	 */
	public abstract List<E> selectAll();
	
	/**
	 * FindBy Primary key
	 * 
	 * @param element
	 * @param id
	 * @return
	 */
	public abstract E findByPrimaryKey(E element,Integer id);
	
	/**
	 * Inserts l'entità
	 * 
	 * @param element
	 * @return
	 */
	public abstract boolean insert(E element);

	/**
	 * 
	 * @param element
	 */
	public abstract void insertTransactional(E element);
	
	/**
	 * 
	 * @param element
	 * @param s
	 * @return
	 */
	@Deprecated
	public abstract void insertTransactional(E element, Session s);
	
	
	
	/**
	 * Update the entity
	 * 
	 * @param element
	 * @return
	 */
	public abstract void update(E element);
	
	
	/**
	 * 
	 * @param element
	 * @throws Exception
	 */
	
	public abstract void updateTransactional(E element) ;
	
	/**
	 * 
	 * @param element
	 * @param session
	 * @return
	 */
	@Deprecated
	boolean updateTransactional(E element, Session session);
	
	/**
	 * Rimuove logicamente l'entità
	 * 
	 * @param element
	 * @return
	 * @throws Exception 
	 */
	public abstract  boolean removeLogical(E element) ;
	
	/**
	 * Rimuove fisicamente l'entità
	 * 
	 * @param e
	 * @return
	 */
	public abstract void removePhysical(E e);
	
	/**
	 * 
	 * @return
	 */
	@Deprecated
	public abstract Session openSession();
	
	/**
	 * 
	 * @return
	 */
	@Deprecated
	public abstract boolean closeSession(Session session);
	
	
	/**
	 * Open a transaction
	 * 
	 * @return
	 * @throws SessionNotValidException 
	 */
	@Deprecated
	public abstract Transaction beginTransaction(Session session) throws SessionNotValidException;

	/**
	 * Committa una transazione
	 * 
	 * @param transaction
	 */
	@Deprecated
	public abstract boolean commitTransaction(Transaction transaction);
	
	/**
	 * 
	 */
	@Deprecated
	public abstract boolean rollbackTransaction(Transaction transaction);

	/**
	 * 
	 * @param element
	 */
	public abstract void removePhysicalTransactional(E element);








	
}
