package com.mediatica.vouchain.exceptions;
/**
 * 
 * @author Pietro Napolitano
 *
 */

public class InvokeBlockchainException extends Exception{

	    /**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		public InvokeBlockchainException(String message) {
	        super(message);
	    }

	    public InvokeBlockchainException(String message, Throwable throwable) {
	        super(message, throwable);
	    }

}
