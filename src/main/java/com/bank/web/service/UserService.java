package com.bank.web.service;

import java.util.List;

import com.bank.web.model.User;
import com.bank.web.model.dto.request.SignupRequest;
import com.bank.web.model.dto.response.UserResponse;

import jakarta.transaction.Transactional;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public interface UserService {
    UserResponse registerUser(SignupRequest signUpRequest);
    UserResponse getUserDetails(Long userId);
    UserResponse updateUserDetails(Long userId, User userDetails);
    void deleteUser(Long userId);
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
    List<UserResponse> getAllUsers();
    UserResponse getUserById(Long id);
}