package com.turkcell.user_service.service;

import com.turkcell.user_service.dto.RegisterRequest;
import com.turkcell.user_service.dto.UserResponse;
import com.turkcell.user_service.entity.User;
import com.turkcell.user_service.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

// @Service → Spring'e "bu sınıf bir servis katmanıdır" diyoruz. Her sınıfta tek tek nesne oluşturmaya gerek kalmıyor.
// Spring bunu otomatik yönetir, her yerden @Autowired ile kullanılabilir
@Service
//UserService userService = new UserService() i her clasta otomatik yapıyor.


// @RequiredArgsConstructor → Lombok, final alanlar için constructor otomatik üretiyor
// Biz yazmak zorunda kalmıyoruz. Controller katmanında UserService'i kullanmak istiyorsun
@RequiredArgsConstructor
public class UserService {

    // final → bu alanlar constructor ile set edilecek (Lombok hallediyor)
    // UserRepository → veritabanı işlemleri için
    private final UserRepository userRepository;

    // PasswordEncoder → şifreleri BCrypt ile hashlemek için
    private final PasswordEncoder passwordEncoder;

    // Kullanıcı kayıt metodu
    public UserResponse register(RegisterRequest request) {

        // Aynı email ile kayıt var mı kontrol et
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Bu email zaten kayıtlı!");
        }

        User user = new User();
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setEmail(request.getEmail());


        // Şifreyi düz metin olarak saklamıyoruz — güvenlik için hashle
        // Örnek: "123456" → "$2a$10$xyz..." şeklinde saklanır
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        // Veritabanına kaydet
        User savedUser = userRepository.save(user);

        // Veritabanına kaydet ve kayıt edilmiş nesneyi döndür
        return convertToResponse(savedUser);
    }


    // Tüm kullanıcıları getir — şifresiz liste döndür
    public List<UserResponse> getAllUsers() {
        return userRepository.findAll()
                .stream()
                .map(this::convertToResponse)
                .toList();
    }

    // User entity'sini UserResponse'a dönüştüren yardımcı metod
    // Bu dönüşümü tek yerden yönetiyoruz — kod tekrarı yok
    private UserResponse convertToResponse(User user) {
        UserResponse response = new UserResponse();
        response.setId(user.getId());
        response.setFirstName(user.getFirstName());
        response.setLastName(user.getLastName());
        response.setEmail(user.getEmail());
        // password set etmiyoruz!
        return response;
    }
}
/* 1. Giriş değişti

Önce: User geliyordu — kullanıcı id veya role gibi alanları manipüle edebilirdi
Şimdi: RegisterRequest geliyor — sadece izin verdiğin alanlar var

2. Çıkış değişti

Önce: User dönüyordu — şifre JSON'da görünüyordu ❌
Şimdi: UserResponse dönüyor — şifre yok ✅

3. convertToResponse metodu eklendi

User'ı UserResponse'a çeviriyor
Şifreyi kasıtlı olarak dışarıda bırakıyor*/