package com.turkcell.user_service.controller;

import com.turkcell.user_service.dto.LoginRequest;
import com.turkcell.user_service.dto.LoginResponse;
import com.turkcell.user_service.dto.RegisterRequest;
import com.turkcell.user_service.dto.UserResponse;
import com.turkcell.user_service.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    // POST /api/users/register
    // Artık RegisterRequest alıyor — User entity değil
    @PostMapping("/register")
    public ResponseEntity<UserResponse> register(@Valid @RequestBody RegisterRequest request) {
        UserResponse response = userService.register(request);
        return ResponseEntity.status(201).body(response);
    }

    // GET /api/users
    // Artık UserResponse listesi döndürüyor — şifreler gizli
    @GetMapping
    public ResponseEntity<List<UserResponse>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    // POST /api/users/login → giriş yap, token al
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request) {
        LoginResponse response = userService.login(request);
        return ResponseEntity.ok(response);
    }


}
