package com.bank.web.service.impl;

import com.bank.web.exception.AccountNotFoundException;
import com.bank.web.model.*;
import com.bank.web.model.dto.request.AccountRequest;
import com.bank.web.model.dto.response.AccountResponse;
import com.bank.web.repository.AccountRepository;
import com.bank.web.repository.UserRepository;
import com.bank.web.service.AccountService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {
    private final AccountRepository accountRepository;
    private final UserRepository userRepository;

    @Override
    @Transactional
    public AccountResponse createAccount(AccountRequest accountRequest) {
        User user = userRepository.findById(accountRequest.userId())
                .orElseThrow(() -> new RuntimeException("Error: User not found."));

        Account account = new Account();
        account.setAccountNumber(generateAccountNumber());
        account.setUser(user);
        account.setAccountType(accountRequest.accountType());
        account.setBalance(BigDecimal.ZERO);
        account.setStatus(Account.AccountStatus.ACTIVE);

        accountRepository.save(account);
        return AccountResponse.fromAccount(account);
    }

    @Override
    public AccountResponse getAccountDetails(String accountNumber) {
        Account account = accountRepository.findByAccountNumber(accountNumber)
                .orElseThrow(() -> new AccountNotFoundException(accountNumber));
        return AccountResponse.fromAccount(account);
    }

    @Override
    public List<AccountResponse> getUserAccounts(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));

        return accountRepository.findByUser(user).stream()
                .map(AccountResponse::fromAccount)
                .collect(Collectors.toList());
    }

    @Override
    public AccountResponse updateAccountStatus(String accountNumber, Account.AccountStatus status) {
        Account account = accountRepository.findByAccountNumber(accountNumber)
                .orElseThrow(() -> new AccountNotFoundException(accountNumber));

        account.setStatus(status);
        accountRepository.save(account);
        return AccountResponse.fromAccount(account);
    }

    @Override
    public void deleteAccount(String accountNumber) {
        Account account = accountRepository.findByAccountNumber(accountNumber)
                .orElseThrow(() -> new AccountNotFoundException(accountNumber));
        accountRepository.delete(account);
    }

    private String generateAccountNumber() {
        return "AC" + System.currentTimeMillis();
    }
}