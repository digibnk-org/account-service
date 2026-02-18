package com.digibnk.account.service;

import com.digibnk.account.dto.AccountDTO;
import com.digibnk.account.dto.CreateAccountDTO;

import java.util.List;

public interface AccountService {
    AccountDTO createAccount(CreateAccountDTO createAccountDTO);
    AccountDTO getAccountById(Long id);
    AccountDTO getAccountByAccountNumber(String accountNumber);
    List<AccountDTO> getAllAccounts();
}
