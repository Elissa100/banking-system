// com.bank.web.service/impl/TransactionServiceImpl.java
package com.bank.web.service.impl;

import com.bank.web.model.*;
import com.bank.web.model.dto.request.TransactionRequest;
import com.bank.web.model.dto.response.TransactionResponse;
import com.bank.web.repository.AccountRepository;
import com.bank.web.repository.TransactionRepository;
import com.bank.web.service.TransactionService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TransactionServiceImpl implements TransactionService {
    private final TransactionRepository transactionRepository;
    private final AccountRepository accountRepository;

    @Override
    @Transactional
    public TransactionResponse deposit(TransactionRequest transactionRequest) {
        Account account = accountRepository.findByAccountNumber(transactionRequest.accountNumber())
                .orElseThrow(() -> new RuntimeException("Error: Account not found."));

        if (account.getStatus() != Account.AccountStatus.ACTIVE) {
            throw new RuntimeException("Error: Account is not active.");
        }

        Transaction transaction = new Transaction();
        transaction.setTransactionId(generateTransactionId());
        transaction.setToAccount(account);
        transaction.setAmount(transactionRequest.amount());
        transaction.setTransactionType(Transaction.TransactionType.DEPOSIT);
        transaction.setDescription(transactionRequest.description());
        transaction.setStatus(Transaction.TransactionStatus.COMPLETED);

        account.setBalance(account.getBalance().add(transactionRequest.amount()));
        accountRepository.save(account);
        transactionRepository.save(transaction);

        return TransactionResponse.fromTransaction(transaction);
    }

    @Override
    @Transactional
    public TransactionResponse withdraw(TransactionRequest transactionRequest) {
        Account account = accountRepository.findByAccountNumber(transactionRequest.accountNumber())
                .orElseThrow(() -> new RuntimeException("Error: Account not found."));

        if (account.getStatus() != Account.AccountStatus.ACTIVE) {
            throw new RuntimeException("Error: Account is not active.");
        }

        if (account.getBalance().compareTo(transactionRequest.amount()) < 0) {
            throw new RuntimeException("Error: Insufficient funds.");
        }

        Transaction transaction = new Transaction();
        transaction.setTransactionId(generateTransactionId());
        transaction.setFromAccount(account);
        transaction.setAmount(transactionRequest.amount());
        transaction.setTransactionType(Transaction.TransactionType.WITHDRAWAL);
        transaction.setDescription(transactionRequest.description());
        transaction.setStatus(Transaction.TransactionStatus.COMPLETED);

        account.setBalance(account.getBalance().subtract(transactionRequest.amount()));
        accountRepository.save(account);
        transactionRepository.save(transaction);

        return TransactionResponse.fromTransaction(transaction);
    }

    @Override
    @Transactional
    public TransactionResponse transfer(TransactionRequest transactionRequest) {
        Account fromAccount = accountRepository.findByAccountNumber(transactionRequest.accountNumber())
                .orElseThrow(() -> new RuntimeException("Error: From account not found."));

        Account toAccount = accountRepository.findByAccountNumber(transactionRequest.accountNumber())
                .orElseThrow(() -> new RuntimeException("Error: To account not found."));

        if (fromAccount.getStatus() != Account.AccountStatus.ACTIVE || 
            toAccount.getStatus() != Account.AccountStatus.ACTIVE) {
            throw new RuntimeException("Error: One or both accounts are not active.");
        }

        if (fromAccount.getBalance().compareTo(transactionRequest.amount()) < 0) {
            throw new RuntimeException("Error: Insufficient funds.");
        }

        Transaction transaction = new Transaction();
        transaction.setTransactionId(generateTransactionId());
        transaction.setFromAccount(fromAccount);
        transaction.setToAccount(toAccount);
        transaction.setAmount(transactionRequest.amount());
        transaction.setTransactionType(Transaction.TransactionType.TRANSFER);
        transaction.setDescription(transactionRequest.description());
        transaction.setStatus(Transaction.TransactionStatus.COMPLETED);

        fromAccount.setBalance(fromAccount.getBalance().subtract(transactionRequest.amount()));
        toAccount.setBalance(toAccount.getBalance().add(transactionRequest.amount()));

        accountRepository.save(fromAccount);
        accountRepository.save(toAccount);
        transactionRepository.save(transaction);

        return TransactionResponse.fromTransaction(transaction);
    }

    @Override
    public List<TransactionResponse> getAccountTransactions(String accountNumber) {
        Account account = accountRepository.findByAccountNumber(accountNumber)
                .orElseThrow(() -> new RuntimeException("Error: Account not found."));

        List<Transaction> transactions = transactionRepository.findByFromAccountOrToAccount(account, account);
        return transactions.stream()
                .map(TransactionResponse::fromTransaction)
                .collect(Collectors.toList());
    }

    @Override
    public List<TransactionResponse> getAccountTransactionsBetweenDates(
            String accountNumber, LocalDateTime startDate, LocalDateTime endDate) {
        Account account = accountRepository.findByAccountNumber(accountNumber)
                .orElseThrow(() -> new RuntimeException("Error: Account not found."));

        List<Transaction> transactions = transactionRepository
                .findByFromAccountOrToAccountAndCreatedAtBetween(account, account, startDate, endDate);
        return transactions.stream()
                .map(TransactionResponse::fromTransaction)
                .collect(Collectors.toList());
    }

    @Override
    public BigDecimal getAccountBalance(String accountNumber) {
        Account account = accountRepository.findByAccountNumber(accountNumber)
                .orElseThrow(() -> new RuntimeException("Error: Account not found."));
        return account.getBalance();
    }

    private String generateTransactionId() {
        return "TXN" + UUID.randomUUID().toString().replace("-", "").substring(0, 10).toUpperCase();
    }
}