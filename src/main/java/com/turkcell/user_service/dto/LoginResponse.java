package com.turkcell.user_service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

// Başarılı girişte kullanıcıya dönen token
@Data
@AllArgsConstructor
public class LoginResponse {
    private String token;// sonraki işlemlerde aynı tokenle devam edebilmesi için
    private String email;
}