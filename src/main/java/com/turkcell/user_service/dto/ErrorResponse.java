package com.turkcell.user_service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

// Hata durumunda API'den dönecek standart cevap formatı
@Data
@AllArgsConstructor
public class ErrorResponse {
    private String message;
    private int status;
}