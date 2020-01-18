package com.db.awmd.challenge.exception;

public class AmountGreaterThanBalanceException extends Exception {
	
	  /**
	 * Exception class for amount to be transfered is greater than balance 
	 */
	private static final long serialVersionUID = 1L;

	public AmountGreaterThanBalanceException(String message) {
		    super(message);
		  }

}
