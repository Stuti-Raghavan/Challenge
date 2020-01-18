package com.db.awmd.challenge.service;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.db.awmd.challenge.domain.Account;
import com.db.awmd.challenge.domain.TransferAmount;
import com.db.awmd.challenge.exception.AccountDoesNotExistsException;
import com.db.awmd.challenge.exception.AmountGreaterThanBalanceException;
import com.db.awmd.challenge.exception.TransferFailedException;
import com.db.awmd.challenge.repository.AccountsRepository;
import com.db.awmd.challenge.util.ConfigServerProperties;
import com.db.awmd.challenge.util.Constants;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

/**
 * Service class
 *
 */
@Service
@Slf4j
public class AccountsService {
	
  @Getter
  private final AccountsRepository accountsRepository;
 
  @Autowired
  ConfigServerProperties props;
  
  @Autowired
  EmailNotificationService notificationService;

  @Autowired
  public AccountsService(AccountsRepository accountsRepository) {
    this.accountsRepository = accountsRepository;
  }

  public void createAccount(Account account) {
    this.accountsRepository.createAccount(account);
  }

  public Account getAccount(String accountId) {
    return this.accountsRepository.getAccount(accountId);
  }

  /**
   * Method to transfer amount
 * @param transactionDetails
 * @throws AmountGreaterThanBalanceException
 * @throws AccountDoesNotExistsException
 * @throws TransferFailedException
 */
   public void transferAmount(TransferAmount transactionDetails) throws AmountGreaterThanBalanceException, AccountDoesNotExistsException, TransferFailedException {
	 int rowCount=0; //To track no. of records updated
	 Account sendersAccount = getAccount(transactionDetails.getSendersAccountId());
	 Account receiversAccount=getAccount(transactionDetails.getReceiversAccountId());
	 log.info("Sender's Account Details {} ",sendersAccount);
	 log.info("Receiver's Account Details {} ",receiversAccount);
	 if(sendersAccount != null && receiversAccount != null) {
			if(transactionDetails.getAmount().compareTo(sendersAccount.getBalance())>0) {
				  throw new AmountGreaterThanBalanceException(props.getResponseMessages().get(Constants.AMOUNT_GREATER_THAN_BALANCE));
			 }
	  }else {
			throw new AccountDoesNotExistsException(props.getResponseMessages().get(Constants.ACCOUNT_ID_DOES_NOT_EXISTS));
	  }
	  rowCount=this.accountsRepository.debitAccount(transactionDetails.getSendersAccountId(), transactionDetails.getAmount());
	  if(rowCount==1) {
		    rowCount+=this.accountsRepository.creditAccount(transactionDetails.getReceiversAccountId(), transactionDetails.getAmount());
		    if(rowCount==2) {
		    	  log.info("No of records Updated: {} ",rowCount);
			  	  sendEmailNotification(transactionDetails);
		    }else {
		    	  log.error("Failed to credit money to Receiver's Account.Rollback debit for Acct ID {}"+sendersAccount.getAccountId());
				  throw new TransferFailedException(props.getResponseMessages().get(Constants.TRANSFER_FAILED));
		  }
	  }else {
		    log.error("Failed to debit money from Sender's Account. Acct Id: {}",sendersAccount.getAccountId());
			throw new TransferFailedException(props.getResponseMessages().get(Constants.TRANSFER_FAILED));
	  }
  }
	
  /**
   * Method to invoke Notification Service
 * @param transactionDetails
 */
  private void sendEmailNotification(TransferAmount transactionDetails) {
		String sendersActId= transactionDetails.getSendersAccountId();
		String receiversActId=transactionDetails.getReceiversAccountId();
		BigDecimal amt=transactionDetails.getAmount();
		notificationService.notifyAboutTransfer(new Account(sendersActId,amt),props.getResponseMessages().get(Constants.SENDER_TRANSFER_DESCRIPTION));
		notificationService.notifyAboutTransfer(new Account(receiversActId,amt),props.getResponseMessages().get(Constants.RECEIVER_TRANSFER_DESCRIPTION));
  }
}
