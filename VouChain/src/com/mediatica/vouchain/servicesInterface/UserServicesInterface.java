package com.mediatica.vouchain.servicesInterface;


import com.mediatica.vouchain.dto.DTO;
import com.mediatica.vouchain.dto.PasswordDTO;
import com.mediatica.vouchain.exceptions.EmailYetInTheSystemException;
import com.mediatica.vouchain.exceptions.PasswordNotCorrectException;

public interface UserServicesInterface {

	/**
	 * verifies the credentials and returns the login Object or errorMessage
	 * 
	 * @param email
	 * @param password
	 * @return
	 */
	public DTO login(String email);
	
	
	
	/**
	 * it saves an entity and returns the saved object with the id or an error message
	 * @param usr
	 * @return
	 */
	public DTO signUp(DTO usr)throws EmailYetInTheSystemException;
	
	/**
	 * retrieves an entity by primary key returns the object retrieved in the db by the primary key or an error message
	 * 
	 * @param usrId
	 * @return
	 */
	public DTO showProfile(String usrId);
	
	
	/**
	 * returns the updated object or an error message
	 * 
	 * @param usr
	 * @return
	 */
	public DTO modProfile (DTO usr)throws Exception;
	
	
	/**
	 * 
	 * manages the recover and the change password
	 * 
	 * @param email
	 * @param password
	 * @return
	 * @throws PasswordNotCorrectException 
	 */
	public boolean changePassword(PasswordDTO dto) throws PasswordNotCorrectException;

	
	
}
