package com.db.awmd.challenge.util;


public class Constants {

	public static final String ERROR_MESSAGE = "Error  Occured: ";
	public static final String DEBIT_QUERY = "UPDATE Accounts SET balance=balance-:amount WHERE accountId=:accountId";
	public static final String CREDIT_QUERY = "UPDATE Accounts SET balance=balance+:amount WHERE accountId=:accountId";
	public static final String ACCOUNT_ID_DOES_NOT_EXISTS="accountIdDoesNotExists";
	public static final Object AMOUNT_GREATER_THAN_BALANCE = "amountGreaterThanBalance";
	public static final Object TRANSFER_FAILED = "transferFailed";
	public static final String SENDER_TRANSFER_DESCRIPTION = "sendersTransferDescription";
	public static final Object RECEIVER_TRANSFER_DESCRIPTION = "receiversTransferDescription";
	public static final String BALANCE_QUERY = "SELECT a from Accounts a WHERE a.accountId=:toAcctId OR a.accountId=:fromAcctId";
}
