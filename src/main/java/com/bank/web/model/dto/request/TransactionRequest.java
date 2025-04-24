package com.bank.web.model.dto.request;

import jakarta.validation.constraints.*;
import java.math.BigDecimal;

public record TransactionRequest(
    @NotBlank String accountNumber,
    String fromAccountNumber,
    String toAccountNumber,
    @NotNull @Positive BigDecimal amount,
    String description
) {}