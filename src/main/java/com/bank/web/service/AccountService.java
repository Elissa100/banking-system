package com.bank.web.service;

import com.bank.web.model.Account;
import com.bank.web.model.dto.request.AccountRequest;
import com.bank.web.model.dto.request.UpdateAccountStatusRequest;
import com.bank.web.model.dto.response.AccountResponse;

import java.util.List;

public interface AccountService {
    AccountResponse createAccount(AccountRequest accountRequest);
    AccountResponse getAccountDetails(String accountNumber);
    List<AccountResponse> getUserAccounts(Long userId);
    AccountResponse updateAccountStatus(String accountNumber, Account.AccountStatus status);
    void deleteAccount(String accountNumber);
}