package com.bank.web.model.dto.response;

import com.bank.web.model.Transaction;
import lombok.Builder;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Builder
public record TransactionResponse(
    Long id,
    String transactionId,
    String fromAccountNumber,
    String toAccountNumber,
    BigDecimal amount,
    Transaction.TransactionType transactionType,
    String description,
    LocalDateTime createdAt,
    Transaction.TransactionStatus status
) {
    public static TransactionResponse fromTransaction(Transaction transaction) {
        return TransactionResponse.builder()
                .id(transaction.getId())
                .transactionId(transaction.getTransactionId())
                .fromAccountNumber(transaction.getFromAccount() != null ? 
                    transaction.getFromAccount().getAccountNumber() : null)
                .toAccountNumber(transaction.getToAccount() != null ? 
                    transaction.getToAccount().getAccountNumber() : null)
                .amount(transaction.getAmount())
                .transactionType(transaction.getTransactionType())
                .description(transaction.getDescription())
                .createdAt(transaction.getCreatedAt())
                .status(transaction.getStatus())
                .build();
    }
}