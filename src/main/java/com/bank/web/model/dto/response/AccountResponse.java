package com.bank.web.model.dto.response;

import com.bank.web.model.Account;
import lombok.Builder;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Builder
public record AccountResponse(
    Long id,
    String accountNumber,
    Long userId,
    Account.AccountType accountType,
    BigDecimal balance,
    Account.AccountStatus status,
    LocalDateTime createdAt,
    LocalDateTime updatedAt
) {
    public static AccountResponse fromAccount(Account account) {
        return AccountResponse.builder()
                .id(account.getId())
                .accountNumber(account.getAccountNumber())
                .userId(account.getUser().getId())
                .accountType(account.getAccountType())
                .balance(account.getBalance())
                .status(account.getStatus())
                .createdAt(account.getCreatedAt())
                .updatedAt(account.getUpdatedAt())
                .build();
    }
}