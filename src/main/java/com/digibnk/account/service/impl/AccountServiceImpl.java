package com.digibnk.account.service.impl;

import com.digibnk.account.dto.AccountDTO;
import com.digibnk.account.dto.BalanceUpdateRequest;
import com.digibnk.account.dto.CreateAccountDTO;
import com.digibnk.account.entity.Account;
import com.digibnk.account.enums.AccountStatus;
import com.digibnk.account.mapper.AccountMapper;
import com.digibnk.account.repository.AccountRepository;
import com.digibnk.account.service.AccountService;
import com.digibnk.common.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;
    private final AccountMapper accountMapper;

    @Override
    @Transactional
    public AccountDTO createAccount(CreateAccountDTO createAccountDTO) {
        log.info("Creating account for customer: {}", createAccountDTO.getCustomerId());

        Account account = Account.builder()
                .customerId(createAccountDTO.getCustomerId())
                .accountType(createAccountDTO.getAccountType())
                .currency(createAccountDTO.getCurrency() != null ? createAccountDTO.getCurrency() : "USD")
                .balance(createAccountDTO.getInitialDeposit() != null ? createAccountDTO.getInitialDeposit() : BigDecimal.ZERO)
                .status(AccountStatus.ACTIVE)
                .accountNumber(generateAccountNumber())
                .build();

        account = accountRepository.save(account);
        return accountMapper.toDTO(account);
    }

    @Override
    public AccountDTO getAccountById(Long id) {
        log.info("Fetching account with id: {}", id);
        Account account = accountRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Account not found with id: " + id));
        return accountMapper.toDTO(account);
    }

    @Override
    public AccountDTO getAccountByAccountNumber(String accountNumber) {
        log.info("Fetching account with number: {}", accountNumber);
        Account account = accountRepository.findByAccountNumber(accountNumber)
                .orElseThrow(() -> new ResourceNotFoundException("Account not found with number: " + accountNumber));
        return accountMapper.toDTO(account);
    }

    @Override
    public List<AccountDTO> getAllAccounts() {
        return accountRepository.findAll().stream()
                .map(accountMapper::toDTO)
                .toList();
    }

    @Override
    @Transactional
    public AccountDTO updateBalance(Long id, BalanceUpdateRequest request) {
        log.info("Updating balance for account id: {} by amount: {}", id, request.getAmount());

        Account account = accountRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Account not found with id: " + id));

        if (account.getStatus() != AccountStatus.ACTIVE) {
            throw new IllegalStateException("Account " + id + " is not active");
        }

        BigDecimal newBalance = account.getBalance().add(request.getAmount());

        if (newBalance.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalStateException("Insufficient funds in account " + id
                    + ". Available: " + account.getBalance() + ", Required deduction: " + request.getAmount().abs());
        }

        account.setBalance(newBalance);
        account = accountRepository.save(account);
        log.info("Balance updated. Account: {}, New balance: {}", id, newBalance);
        return accountMapper.toDTO(account);
    }

    private String generateAccountNumber() {
        return UUID.randomUUID().toString().replace("-", "").substring(0, 10).toUpperCase();
    }
}
