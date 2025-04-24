package com.bank.web.model.dto.response;

public record ErrorResponse(
    int status,
    String message,
    String path,
    String timestamp
) {
    public ErrorResponse(int status, String message) {
        this(status, message, null, null);
    }
}