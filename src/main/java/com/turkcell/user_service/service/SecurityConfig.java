package com.turkcell.user_service;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;

// @Configuration → Spring'e "bu sınıfta Bean tanımları var" diyoruz
@Configuration
public class SecurityConfig {

    // @Bean → Spring bu metodu çalıştırır ve dönen nesneyi yönetir
    // PasswordEncoder → şifreleri BCrypt algoritmasıyla hashler
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // Tüm endpoint'lere erişime izin ver (geliştirme aşaması)
    // İleride JWT ekleyince buraya güvenlik kuralları ekleyeceğiz
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth.anyRequest().permitAll());
        return http.build();
    }
}