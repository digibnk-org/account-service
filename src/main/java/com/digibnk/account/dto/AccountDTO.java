package com.digibnk.account.dto;

import com.digibnk.account.enums.AccountStatus;
import com.digibnk.account.enums.AccountType;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
public class AccountDTO {
    private Long id;
    private String accountNumber;
    private Long customerId;
    private AccountType accountType;
    private String currency;
    private BigDecimal balance;
    private AccountStatus status;
    private LocalDateTime createDate;
    private LocalDateTime updateDate;
}
