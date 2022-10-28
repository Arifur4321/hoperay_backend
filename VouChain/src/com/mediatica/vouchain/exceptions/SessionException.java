package com.mediatica.vouchain.exceptions;

public class SessionException extends Exception {

	
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public SessionException(String message) {
        super(message);
    }

    public SessionException(String message, Throwable throwable) {
        super(message, throwable);
    }


}



