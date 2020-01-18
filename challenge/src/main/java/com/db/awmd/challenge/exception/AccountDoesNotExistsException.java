package com.db.awmd.challenge.exception;

public class AccountDoesNotExistsException extends Exception {

	/**
	 * Exception class for account id is incorrect
	 */
	private static final long serialVersionUID = 1L;

	public AccountDoesNotExistsException(String message) {
	    super(message);
	  }

}
