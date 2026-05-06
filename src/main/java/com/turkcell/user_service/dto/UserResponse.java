package com.turkcell.user_service.dto;

import lombok.Data;

// API'den kullanıcıya dönen cevap — şifre kesinlikle yok!
@Data
public class UserResponse {
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    // password alanı yok — güvenlik!
}
