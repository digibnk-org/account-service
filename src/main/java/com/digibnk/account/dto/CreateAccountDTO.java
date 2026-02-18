package com.digibnk.account.dto;

import com.digibnk.account.enums.AccountType;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class CreateAccountDTO {
    @NotNull(message = "Customer ID is required")
    private Long customerId;

    @NotNull(message = "Account type is required")
    private AccountType accountType;

    private String currency;

    private BigDecimal initialDeposit;
}
