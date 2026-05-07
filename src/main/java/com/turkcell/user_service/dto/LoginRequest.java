package com.turkcell.user_service.dto;

import lombok.Data;

// Kullanıcıdan gelen giriş isteği
@Data
public class LoginRequest {
    private String email;
    private String password;
}