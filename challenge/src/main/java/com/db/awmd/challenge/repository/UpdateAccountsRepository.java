package com.db.awmd.challenge.repository;

import java.math.BigDecimal;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.db.awmd.challenge.entity.Accounts;
import com.db.awmd.challenge.util.Constants;

/**
 * Interface with methods to debit/credit amount & check balance
 *
 */
@Transactional
public interface UpdateAccountsRepository extends JpaRepository<Accounts, String> {
	
	@Modifying
	@Query(Constants.DEBIT_QUERY)
	public int debitSendersAccount(@Param("accountId") String accountId, @Param("amount") BigDecimal amount);

	@Modifying
	@Query(Constants.CREDIT_QUERY)
	public int creditReceiversAccount(@Param("accountId") String accountId, @Param("amount") BigDecimal amount);

	@Query(Constants.BALANCE_QUERY)
	public List<Accounts> checkBalance(@Param("toAcctId") String toAcctId, @Param("fromAcctId") String fromAcctId);

}