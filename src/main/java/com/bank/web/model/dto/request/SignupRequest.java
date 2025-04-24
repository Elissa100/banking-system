package com.bank.web.model.dto.request;

import jakarta.validation.constraints.*;

public record SignupRequest(
    @NotBlank @Size(min = 3, max = 20) String username,
    @NotBlank @Size(max = 50) @Email String email,
    @NotBlank @Size(min = 6, max = 40) String password,
    @NotBlank @Size(max = 50) String firstName,
    @NotBlank @Size(max = 50) String lastName,
    @NotBlank @Size(max = 20) String phone
) {}