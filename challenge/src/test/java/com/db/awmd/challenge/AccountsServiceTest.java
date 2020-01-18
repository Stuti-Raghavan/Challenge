package com.db.awmd.challenge;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.fail;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;

import java.math.BigDecimal;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.db.awmd.challenge.domain.Account;
import com.db.awmd.challenge.domain.TransferAmount;
import com.db.awmd.challenge.entity.Accounts;
import com.db.awmd.challenge.exception.AccountDoesNotExistsException;
import com.db.awmd.challenge.exception.AmountGreaterThanBalanceException;
import com.db.awmd.challenge.exception.DuplicateAccountIdException;
import com.db.awmd.challenge.repository.UpdateAccountsRepository;
import com.db.awmd.challenge.service.AccountsService;
import com.db.awmd.challenge.service.EmailNotificationService;

@RunWith(SpringRunner.class)
@SpringBootTest
public class AccountsServiceTest {

  @Autowired
  private AccountsService accountsService;
  
  @Autowired
  private UpdateAccountsRepository UpdateAcctsRepos;

  @Before
  public void clearAccounts() {
    // Reset the existing accounts before each test.
    accountsService.getAccountsRepository().clearAccounts();
  }
  
  @Test
  public void addAccount() throws Exception {
    Account account = new Account("Id-123");
    account.setBalance(new BigDecimal(1000));
    this.accountsService.createAccount(account);

    assertThat(this.accountsService.getAccount("Id-123")).isEqualTo(account);
  }

  @Test
  public void addAccount_failsOnDuplicateId() throws Exception {
    String uniqueId = "Id-" + System.currentTimeMillis();
    Account account = new Account(uniqueId);
    this.accountsService.createAccount(account);

    try {
      this.accountsService.createAccount(account);
      fail("Should have failed when adding duplicate account");
    } catch (DuplicateAccountIdException ex) {
      assertThat(ex.getMessage()).isEqualTo("Account id " + uniqueId + " already exists!");
    }

  }
  
  @Test
  public void transferAmount_failsOnIncorrectReceiverAccountId() throws Exception {
	  Account senderAccount = new Account("Id-123");
	  senderAccount.setBalance(new BigDecimal(1000));
	  this.accountsService.createAccount(senderAccount);
	  
	  Account receiverAccount = new Account("Id-124");
	  receiverAccount.setBalance(new BigDecimal(1000));
	  this.accountsService.createAccount(receiverAccount);
	  
	  try {
	  this.accountsService.transferAmount(new TransferAmount("Id-123", "Id-12345", new BigDecimal(500)));
	    
	  }catch(AccountDoesNotExistsException ex) {
		  assertThat(ex.getMessage()).isEqualTo("Sender/Receiver Account Id does not exists");
	  }  
	  
  }
  
  @Test
  public void transferAmount_failsOnIncorrectSenderAccountId() throws Exception {
	  Account senderAccount = new Account("Id-123");
	  senderAccount.setBalance(new BigDecimal(1000));
	  this.accountsService.createAccount(senderAccount);
	  
	  Account receiverAccount = new Account("Id-124");
	  receiverAccount.setBalance(new BigDecimal(1000));
	  this.accountsService.createAccount(receiverAccount);
	  
	  try {
	  this.accountsService.transferAmount(new TransferAmount("Id-12345", "Id-124", new BigDecimal(500)));
	    
	  }catch(AccountDoesNotExistsException ex) {
		  assertThat(ex.getMessage()).isEqualTo("Sender/Receiver Account Id does not exists");
	  }  
	  
  }
  
  @Test
  public void transferAmount_failsOnAmountGreaterThanBalance() throws Exception {
	  Account senderAccount = new Account("Id-123");
	  senderAccount.setBalance(new BigDecimal(1000));
	  this.accountsService.createAccount(senderAccount);
	  
	  Account receiverAccount = new Account("Id-124");
	  receiverAccount.setBalance(new BigDecimal(1000));
	  this.accountsService.createAccount(receiverAccount);
	  
	  try {
	  this.accountsService.transferAmount(new TransferAmount("Id-123", "Id-124", new BigDecimal(5000)));
	    
	  }catch(AmountGreaterThanBalanceException ex) {
		  assertThat(ex.getMessage()).isEqualTo("Amount is greater than balance");
	  }  
	  
  }
  
  @Test
  public void transferAmount_Success() throws Exception{
	  Account senderAccount = new Account("Id-1001");
	  senderAccount.setBalance(new BigDecimal(1000));
	  this.accountsService.createAccount(senderAccount);
	  
	  Account receiverAccount = new Account("Id-1002");
	  receiverAccount.setBalance(new BigDecimal(2000));
	  this.accountsService.createAccount(receiverAccount);
	  
	  this.accountsService.transferAmount(new TransferAmount("Id-1001", "Id-1002", new BigDecimal(500)));
	  EmailNotificationService emailSvcMock= mock(EmailNotificationService.class);
	  Mockito.doNothing().when(emailSvcMock).notifyAboutTransfer(anyObject(), anyString());
	  List<Accounts> accounts = UpdateAcctsRepos.checkBalance("Id-1001", "Id-1002");
	  if(accounts !=null && accounts.size()>0) {
		  for( Accounts acct:accounts) {
			 if("Id-1001".equals(acct.getAccountId())){
				 System.out.println(acct.getBalance());
				 assertThat(String.valueOf(acct.getBalance())).isEqualTo("500.00");
			 }else {
				 assertThat(String.valueOf(acct.getBalance())).isEqualTo("2500.00");
			 }
		  }
	  }
  }
  
  @Test
  public void mockNotificationService() throws Exception{
	  Account senderAccount = new Account("Id-10001");
	  senderAccount.setBalance(new BigDecimal(1000));
	  this.accountsService.createAccount(senderAccount);
	  
	  Account receiverAccount = new Account("Id-10002");
	  receiverAccount.setBalance(new BigDecimal(2000));
	  this.accountsService.createAccount(receiverAccount);
	  
	  this.accountsService.transferAmount(new TransferAmount("Id-10001", "Id-10002", new BigDecimal(500)));
	  EmailNotificationService emailSvcMock= mock(EmailNotificationService.class);
	  Mockito.doNothing().when(emailSvcMock).notifyAboutTransfer(anyObject(), anyString());
  }
}