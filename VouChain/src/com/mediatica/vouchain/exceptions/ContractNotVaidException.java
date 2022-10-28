package com.mediatica.vouchain.exceptions;
/**
 * 
 * @author Pietro Napolitano
 *
 */

public class ContractNotVaidException extends Exception{
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public ContractNotVaidException(String message) {
        super(message);
    }

    public ContractNotVaidException(String message, Throwable throwable) {
        super(message, throwable);
    }
}
