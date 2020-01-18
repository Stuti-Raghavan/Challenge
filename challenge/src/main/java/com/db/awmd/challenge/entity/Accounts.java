package com.db.awmd.challenge.entity;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Class to interact with Accounts Table
 *
 */
@Data
@NoArgsConstructor
@Entity
@Table(name = "Accounts")
public class Accounts {

	@Id
	@Column(name = "account_id")
	private  String accountId;

	@Column(name = "balance")
	private BigDecimal balance;
}
