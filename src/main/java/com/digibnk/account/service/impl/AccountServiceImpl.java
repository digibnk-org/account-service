package com.digibnk.account.service.impl;

import com.digibnk.account.dto.AccountDTO;
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
import java.util.stream.Collectors;

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

    private String generateAccountNumber() {
        return UUID.randomUUID().toString().replace("-", "").substring(0, 10).toUpperCase();
    }
}
