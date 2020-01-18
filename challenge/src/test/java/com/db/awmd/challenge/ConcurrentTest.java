package com.db.awmd.challenge;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;

import java.math.BigDecimal;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.anarsoft.vmlens.concurrent.junit.ConcurrentTestRunner;
import com.anarsoft.vmlens.concurrent.junit.ThreadCount;
import com.db.awmd.challenge.domain.Account;
import com.db.awmd.challenge.domain.TransferAmount;
import com.db.awmd.challenge.entity.Accounts;
import com.db.awmd.challenge.repository.UpdateAccountsRepository;
import com.db.awmd.challenge.service.AccountsService;
import com.db.awmd.challenge.service.EmailNotificationService;

@Ignore
@RunWith(ConcurrentTestRunner.class)
@SpringBootTest
public class ConcurrentTest {

	@Autowired
	private AccountsService accountsService;

	@Autowired
	private UpdateAccountsRepository UpdateAcctsRepos;

	@Before
	public void setUp() {
		accountsService.getAccountsRepository().clearAccounts();
	}

	@Test
	@ThreadCount(4)
	public void transferAmount_Success() throws Exception {
		Account senderAccount = new Account("Id-10001");
		senderAccount.setBalance(new BigDecimal(5000));
		accountsService.createAccount(senderAccount);

		Account receiverAccount = new Account("Id-10002");
		receiverAccount.setBalance(new BigDecimal(2000));
		accountsService.createAccount(receiverAccount);

		accountsService.transferAmount(new TransferAmount("Id-10001", "Id-10002", new BigDecimal(500)));
		EmailNotificationService emailSvcMock = mock(EmailNotificationService.class);
		Mockito.doNothing().when(emailSvcMock).notifyAboutTransfer(anyObject(), anyString());

	}

	@After
	public void checkBalance() {
		List<Accounts> accounts = UpdateAcctsRepos.checkBalance("Id-10001", "Id-10002");
		if (accounts != null && accounts.size() > 0) {
			for (Accounts acct : accounts) {
				if ("Id-10001".equals(acct.getAccountId())) {
					System.out.println(acct.getBalance());
					assertThat(String.valueOf(acct.getBalance())).isEqualTo("3000.00");
				} else {
					assertThat(String.valueOf(acct.getBalance())).isEqualTo("4000.00");
				}
			}
		}
	}
}
