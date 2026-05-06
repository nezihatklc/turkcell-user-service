package com.turkcell.user_service;

import com.turkcell.user_service.dto.ErrorResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

// @RestControllerAdvice → tüm controller'lardaki hataları yakalar
// Tek yerden tüm hata yönetimi — kod tekrarı yok
@RestControllerAdvice
public class GlobalExceptionHandler {

    // RuntimeException fırlatılınca bu metod devreye girer
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ErrorResponse> handleRuntimeException(RuntimeException ex) {
        ErrorResponse error = new ErrorResponse(ex.getMessage(), 400);
        return ResponseEntity.status(400).body(error);
    }
}