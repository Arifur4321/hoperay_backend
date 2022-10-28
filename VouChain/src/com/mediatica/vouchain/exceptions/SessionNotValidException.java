package com.mediatica.vouchain.exceptions;

/**
 * 
 * @author Pietro Napolitano
 *
 */
public class SessionNotValidException extends Exception {

	
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public SessionNotValidException(String message) {
        super(message);
    }

    public SessionNotValidException(String message, Throwable throwable) {
        super(message, throwable);
    }


}


