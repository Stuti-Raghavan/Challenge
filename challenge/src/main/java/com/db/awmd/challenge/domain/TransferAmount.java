package com.db.awmd.challenge.domain;

import java.math.BigDecimal;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotEmpty;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

/**
 * Request class for Amount Transfer
 *
 */
@Data
public class TransferAmount {

	@NotNull
	@NotEmpty
	@JsonProperty("accountFromId") 
	private final String sendersAccountId;

	@NotNull
	@NotEmpty
	@JsonProperty("accountToId")
	private final String receiversAccountId;

	@NotNull
	@Min(value = 0, message = "Amount must be positive.")
	private BigDecimal amount;


	@JsonCreator
	public TransferAmount(@JsonProperty("accountFromId") String sendersAccountId,
			@JsonProperty("accountToId") String receiversAccountId, @JsonProperty("amount") BigDecimal amount) {
		this.sendersAccountId = sendersAccountId;
		this.receiversAccountId = receiversAccountId;
		this.amount = amount;
	}
}
