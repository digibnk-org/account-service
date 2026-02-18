package com.digibnk.account.controller;

import com.digibnk.account.dto.AccountDTO;
import com.digibnk.account.dto.CreateAccountDTO;
import com.digibnk.account.service.AccountService;
import com.digibnk.common.dto.BaseResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/accounts")
@RequiredArgsConstructor
public class AccountController {

    private final AccountService accountService;

    @PostMapping
    public ResponseEntity<BaseResponse<AccountDTO>> createAccount(@Valid @RequestBody CreateAccountDTO createAccountDTO) {
        AccountDTO account = accountService.createAccount(createAccountDTO);
        return new ResponseEntity<>(BaseResponse.success(account, "Account created successfully"), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<BaseResponse<AccountDTO>> getAccountById(@PathVariable Long id) {
        AccountDTO account = accountService.getAccountById(id);
        return ResponseEntity.ok(BaseResponse.success(account));
    }

    @GetMapping("/number/{accountNumber}")
    public ResponseEntity<BaseResponse<AccountDTO>> getAccountByAccountNumber(@PathVariable String accountNumber) {
        AccountDTO account = accountService.getAccountByAccountNumber(accountNumber);
        return ResponseEntity.ok(BaseResponse.success(account));
    }

    @GetMapping
    public ResponseEntity<BaseResponse<List<AccountDTO>>> getAllAccounts() {
        List<AccountDTO> accounts = accountService.getAllAccounts();
        return ResponseEntity.ok(BaseResponse.success(accounts));
    }
}
