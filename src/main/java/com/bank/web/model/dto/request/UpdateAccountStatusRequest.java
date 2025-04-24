package com.bank.web.model.dto.request;

import com.bank.web.model.Account;
import jakarta.validation.constraints.NotNull;

public record UpdateAccountStatusRequest(
    @NotNull Account.AccountStatus status
) {}