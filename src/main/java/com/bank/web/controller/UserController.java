package com.bank.web.controller;

import com.bank.web.model.User;
import com.bank.web.model.dto.response.UserResponse;
import com.bank.web.repository.UserRepository;
import com.bank.web.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserRepository userRepository;
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    @GetMapping
    @PreAuthorize("hasRole('ADMIN') or hasRole('CUSTOMER')")
    public List<UserResponse> getAllUsers() {
        return userService.getAllUsers();
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or #id == principal.id")
    public UserResponse getUserById(@PathVariable Long id) {
        return userService.getUserDetails(id);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or #id == principal.id")
    public ResponseEntity<?> updateUser(@PathVariable Long id, @RequestBody User updatedUserData) {
        return userRepository.findById(id).map(user -> {
            user.setUsername(updatedUserData.getUsername());
            user.setPhone(updatedUserData.getPhone());
            user.setEmail(updatedUserData.getEmail());

            if (updatedUserData.getPassword() != null && !updatedUserData.getPassword().isBlank()) {
                user.setPassword(passwordEncoder.encode(updatedUserData.getPassword()));
            }

            try {
                userRepository.save(user);
                return ResponseEntity.ok("User updated successfully");
            } catch (Exception e) {
                return ResponseEntity.status(500).body("Error updating user: " + e.getMessage());
            }

        }).orElse(ResponseEntity.notFound().build());
    }
}
