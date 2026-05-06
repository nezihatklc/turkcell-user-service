package com.turkcell.user_service.dto;

import lombok.Data;

// Kullanıcıdan kayıt isteği gelince bu nesneyi kullanıyoruz
// Sadece gerekli alanlar var — id yok çünkü kullanıcı bunu belirleyemez
@Data
public class RegisterRequest {
    private String firstName;
    private String lastName;
    private String email;
    private String password;
}
