package com.db.awmd.challenge.exception;

public class TransferFailedException extends RuntimeException {

	  /**
	 * Exception class for money transfer failure
	 */
	private static final long serialVersionUID = 1L;

	public TransferFailedException(String message) {
	    super(message);
	  }
	}