package com.db.awmd.challenge.repository;

import java.math.BigDecimal;

import com.db.awmd.challenge.domain.Account;
import com.db.awmd.challenge.exception.DuplicateAccountIdException;

public interface AccountsRepository {

	void createAccount(Account account) throws DuplicateAccountIdException;

	void clearAccounts();

	int debitAccount(String accountId,BigDecimal amount);
	
	int creditAccount(String accountId,BigDecimal amount);
	
	Account getAccount(String accountId);
}
