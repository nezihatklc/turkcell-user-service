package com.turkcell.user_service.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class RegisterRequest {

    // Boş olamaz
    @NotBlank(message = "İsim boş olamaz")
    private String firstName;

    // Boş olamaz
    @NotBlank(message = "Soyisim boş olamaz")
    private String lastName;

    // Boş olamaz ve geçerli email formatında olmalı
    @NotBlank(message = "Email boş olamaz")
    @Email(message = "Geçerli bir email giriniz")
    private String email;

    // En az 6 karakter olmalı
    @NotBlank(message = "Şifre boş olamaz")
    @Size(min = 6, message = "Şifre en az 6 karakter olmalı")
    private String password;

    // Opsiyonel — boş gelirse varsayılan USER atanır
    private String role;
}