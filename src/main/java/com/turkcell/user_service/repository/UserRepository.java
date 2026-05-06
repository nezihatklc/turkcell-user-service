package com.turkcell.user_service.repository;

import com.turkcell.user_service.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

// JpaRepository'den miras alarak temel CRUD işlemlerini (kaydet, getir, sil, güncelle) otomatik kazanıyoruz
// <User, Long> → User tablosu için, ID tipi Long
public interface UserRepository extends JpaRepository<User, Long> {

    // Spring bu metod adını okuyarak otomatik SQL üretiyor:
    // SELECT EXISTS(SELECT 1 FROM users WHERE email = ?)
    // Kayıt sırasında "bu email zaten var mı?" kontrolü için kullanacağız
    boolean existsByEmail(String email);
}