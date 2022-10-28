package com.mediatica.vouchain.exceptions;

public class VoucherYetInTheSystemException extends Exception {
	private static final long serialVersionUID = 1L;

	public VoucherYetInTheSystemException(String message) {
        super(message);
    }

    public VoucherYetInTheSystemException(String message, Throwable throwable) {
        super(message, throwable);
    }
}
