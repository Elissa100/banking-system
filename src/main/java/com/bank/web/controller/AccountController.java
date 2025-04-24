package com.bank.web.controller;

import com.bank.web.model.dto.request.AccountRequest;
import com.bank.web.model.dto.request.UpdateAccountStatusRequest;
import com.bank.web.model.dto.response.AccountResponse;
import com.bank.web.service.AccountService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/accounts")
@RequiredArgsConstructor
public class AccountController {
    private final AccountService accountService;

    @PostMapping
    @PreAuthorize("hasRole('CUSTOMER') or hasRole('TELLER') or hasRole('ADMIN')")
    public ResponseEntity<AccountResponse> createAccount(@Valid @RequestBody AccountRequest accountRequest) {
        return ResponseEntity.ok(accountService.createAccount(accountRequest));
    }

    @GetMapping("/{accountNumber}")
    @PreAuthorize("hasRole('CUSTOMER') or hasRole('TELLER') or hasRole('ADMIN')")
    public ResponseEntity<AccountResponse> getAccountDetails(@PathVariable String accountNumber) {
        return ResponseEntity.ok(accountService.getAccountDetails(accountNumber));
    }

    @PatchMapping("/{accountNumber}/status")
    @PreAuthorize("hasRole('TELLER') or hasRole('ADMIN')")
    public ResponseEntity<AccountResponse> updateAccountStatus(
            @PathVariable String accountNumber,
            @RequestBody @Valid UpdateAccountStatusRequest request) {
        return ResponseEntity.ok(accountService.updateAccountStatus(accountNumber, request.status()));
    }
}