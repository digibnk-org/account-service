package com.digibnk.account.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TransactionCreatedEvent {

    private Long transactionId;
    private String reference;
    private String type;          // DEPOSIT, WITHDRAWAL, TRANSFER
    private BigDecimal amount;
    private Long sourceAccountId;
    private Long targetAccountId;
    private String status;
    private LocalDateTime occurredAt;
}
