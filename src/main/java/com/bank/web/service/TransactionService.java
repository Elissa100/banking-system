package com.bank.web.service;

import com.bank.web.model.Account;
import com.bank.web.model.dto.request.TransactionRequest;
import com.bank.web.model.dto.response.TransactionResponse;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public interface TransactionService {
    TransactionResponse deposit(TransactionRequest transactionRequest);
    TransactionResponse withdraw(TransactionRequest transactionRequest);
    TransactionResponse transfer(TransactionRequest transactionRequest);
    List<TransactionResponse> getAccountTransactions(String accountNumber);
    List<TransactionResponse> getAccountTransactionsBetweenDates(
            String accountNumber, LocalDateTime startDate, LocalDateTime endDate);
    BigDecimal getAccountBalance(String accountNumber);
}