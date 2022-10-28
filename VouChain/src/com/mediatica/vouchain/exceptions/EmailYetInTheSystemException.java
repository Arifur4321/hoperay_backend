package com.mediatica.vouchain.exceptions;
/**
 * 
 * @author Pietro Napolitano
 *
 */

public class EmailYetInTheSystemException extends Exception {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public EmailYetInTheSystemException(String message) {
        super(message);
    }

    public EmailYetInTheSystemException(String message, Throwable throwable) {
        super(message, throwable);
    }
}
