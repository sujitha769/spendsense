package com.spendsense.userservice.controller;

import com.spendsense.userservice.dto.LoginRequest;
import com.spendsense.userservice.dto.LoginResponse;
import com.spendsense.userservice.dto.UserRegistrationRequest;
import com.spendsense.userservice.dto.UserResponse;
import com.spendsense.userservice.service.AuthService;
import com.spendsense.userservice.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    private final AuthService authService;

    public UserController(
            UserService userService,
            AuthService authService) {

        this.userService = userService;
        this.authService = authService;
    }

    @PostMapping("/register")
    public ResponseEntity<UserResponse> registerUser(
            @Valid @RequestBody UserRegistrationRequest request) {

        return ResponseEntity.ok(
                userService.registerUser(request)
        );
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(
            @Valid @RequestBody LoginRequest request) {

        return ResponseEntity.ok(
                authService.login(request)
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> getUser(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                userService.getUserById(id)
        );
    }
}