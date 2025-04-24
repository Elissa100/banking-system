package com.bank.web.model.dto.response;

import com.bank.web.model.User;
import lombok.Builder;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.stream.Collectors;

@Builder
public record UserResponse(
    Long id,
    String username,
    String email,
    String firstName,
    String lastName,
    String phone,
    Set<String> roles,
    LocalDateTime createdAt,
    LocalDateTime updatedAt
) {
    public static UserResponse fromUser(User user) {
        return UserResponse.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .phone(user.getPhone())
                .roles(user.getRoles().stream()
                        .map(role -> role.getName().name())
                        .collect(Collectors.toSet()))
                .createdAt(user.getCreatedAt())
                .updatedAt(user.getUpdatedAt())
                .build();
    }
}