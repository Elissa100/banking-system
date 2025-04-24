package com.bank.web.repository;

import com.bank.web.model.Account;
import com.bank.web.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    List<Transaction> findByFromAccountOrToAccount(Account fromAccount, Account toAccount);
    List<Transaction> findByFromAccountOrToAccountAndCreatedAtBetween(
            Account fromAccount, Account toAccount, LocalDateTime startDate, LocalDateTime endDate);
}