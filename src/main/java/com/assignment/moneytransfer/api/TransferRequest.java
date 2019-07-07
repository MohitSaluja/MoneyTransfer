package com.assignment.moneytransfer.api;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Min;
import java.math.BigDecimal;

public class TransferRequest {

    @Min(1)
    private Long fromAccount;

    @Min(1)
    private Long toAccount;

    @DecimalMin("0.1")
    private BigDecimal amount;

    public TransferRequest() {
    }

    public TransferRequest(Long fromAccount, Long toAccount, BigDecimal amount) {
        this.fromAccount = fromAccount;
        this.toAccount = toAccount;
        this.amount = amount;
    }

    @JsonProperty
    public Long getFromAccount() {
        return fromAccount;
    }

    @JsonProperty
    public Long getToAccount() {
        return toAccount;
    }

    public BigDecimal getAmount() {
        return amount;
    }
}
