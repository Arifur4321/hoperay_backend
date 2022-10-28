package com.mediatica.vouchain.exceptions;

public class PasswordNotCorrectException extends Exception {
	  /**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		public PasswordNotCorrectException(String message) {
	        super(message);
	    }

	    public PasswordNotCorrectException(String message, Throwable throwable) {
	        super(message, throwable);
	    }
}
