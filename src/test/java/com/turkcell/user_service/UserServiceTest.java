package com.turkcell.user_service;

import com.turkcell.user_service.dto.RegisterRequest;
import com.turkcell.user_service.dto.UserResponse;
import com.turkcell.user_service.entity.User;
import com.turkcell.user_service.repository.UserRepository;
import com.turkcell.user_service.service.JwtService;
import com.turkcell.user_service.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

// Mockito ile unit test — gerçek veritabanı kullanmıyoruz
@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    // Sahte nesneler oluştur — gerçek veritabanına gitmiyor
    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtService jwtService;

    // Test edilecek sınıf — sahte nesneler inject ediliyor
    @InjectMocks
    private UserService userService;

    @Test
    void register_WhenEmailAlreadyExists_ShouldThrowException() {
        // GIVEN — email zaten kayıtlı
        RegisterRequest request = new RegisterRequest();
        request.setEmail("ahmet@turkcell.com");
        request.setPassword("123456");
        request.setFirstName("Ahmet");
        request.setLastName("Yılmaz");

        // UserRepository'nin existsByEmail metodunu sahte olarak ayarla
        when(userRepository.existsByEmail("ahmet@turkcell.com")).thenReturn(true);

        // WHEN & THEN — exception fırlatılmalı
        assertThrows(RuntimeException.class, () -> userService.register(request));
    }

    @Test
    void register_WhenEmailIsNew_ShouldReturnUserResponse() {
        // GIVEN — email yok, kayıt başarılı
        RegisterRequest request = new RegisterRequest();
        request.setEmail("yeni@turkcell.com");
        request.setPassword("123456");
        request.setFirstName("Yeni");
        request.setLastName("Kullanıcı");

        User savedUser = new User();
        savedUser.setId(1L);
        savedUser.setEmail("yeni@turkcell.com");
        savedUser.setFirstName("Yeni");
        savedUser.setLastName("Kullanıcı");
        savedUser.setRole("USER");

        when(userRepository.existsByEmail("yeni@turkcell.com")).thenReturn(false);
        when(passwordEncoder.encode("123456")).thenReturn("hashedPassword");
        when(userRepository.save(any(User.class))).thenReturn(savedUser);

        // WHEN
        UserResponse response = userService.register(request);

        // THEN — response doğru mu?
        assertNotNull(response);
        assertEquals("yeni@turkcell.com", response.getEmail());
        assertEquals("Yeni", response.getFirstName());

        // save metodunun bir kez çağrıldığını doğrula
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void login_WhenEmailNotFound_ShouldThrowException() {
        // GIVEN — email yok
        when(userRepository.findByEmail("yok@turkcell.com")).thenReturn(Optional.empty());

        // WHEN & THEN
        assertThrows(RuntimeException.class, () ->
                userService.login(new com.turkcell.user_service.dto.LoginRequest()));
    }
}