package com.db.awmd.challenge.web;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.db.awmd.challenge.domain.Account;
import com.db.awmd.challenge.domain.TransferAmount;
import com.db.awmd.challenge.exception.AccountDoesNotExistsException;
import com.db.awmd.challenge.exception.AmountGreaterThanBalanceException;
import com.db.awmd.challenge.exception.DuplicateAccountIdException;
import com.db.awmd.challenge.exception.TransferFailedException;
import com.db.awmd.challenge.service.AccountsService;
import com.db.awmd.challenge.util.Constants;

import lombok.extern.slf4j.Slf4j;

/**
 * Rest controller
 *
 */
@RestController
@RequestMapping("/v1/accounts")
@Slf4j
public class AccountsController {

  private final AccountsService accountsService;

  @Autowired
  public AccountsController(AccountsService accountsService) {
    this.accountsService = accountsService;
  }

  @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<Object> createAccount(@RequestBody @Valid Account account) {
    log.info("Creating account {}", account);

    try {
    this.accountsService.createAccount(account);
    } catch (DuplicateAccountIdException daie) {
      return new ResponseEntity<>(daie.getMessage(), HttpStatus.BAD_REQUEST);
    }

    return new ResponseEntity<>(HttpStatus.CREATED);
  }

  @GetMapping(path = "/{accountId}")
  public Account getAccount(@PathVariable String accountId) {
    log.info("Retrieving account for id {}", accountId);
    return this.accountsService.getAccount(accountId);
  }

  /** Method to transfer amount
 * @param transactionDetails
 * @return ResponseEntity
 */
  @PutMapping(path="/transfer",consumes = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<Object> transferAmount(@RequestBody @Valid TransferAmount transactionDetails) {
    log.info("Transaction Details  {}", transactionDetails);
    try {
    	this.accountsService.transferAmount(transactionDetails);
    }catch (AmountGreaterThanBalanceException agtbe) {
    	log.error(Constants.ERROR_MESSAGE +agtbe.getMessage());
    	return new ResponseEntity<>(agtbe.getMessage(), HttpStatus.BAD_REQUEST);
    }catch (AccountDoesNotExistsException adnee) {
    	log.error(Constants.ERROR_MESSAGE +adnee.getMessage());
        return new ResponseEntity<>(adnee.getMessage(), HttpStatus.BAD_REQUEST);
    }catch (TransferFailedException tfe) {
    	log.error(Constants.ERROR_MESSAGE +tfe.getMessage());
        return new ResponseEntity<>(tfe.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }catch(Exception e) {
    	log.error(Constants.ERROR_MESSAGE +e.getMessage());
    	return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
    return new ResponseEntity<>(HttpStatus.OK);
  }
}
