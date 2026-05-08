package com.turkcell.user_service.service;

import com.turkcell.user_service.service.JwtAuthFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    // JWT filter'ı inject ediyoruz
    private final JwtAuthFilter jwtAuthFilter;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // CSRF'yi kapat — REST API'lerde gerekmez
                .csrf(csrf -> csrf.disable())

                // Hangi endpoint'ler açık, hangisi korumalı
                .authorizeHttpRequests(auth -> auth
                // register ve login herkese açık
                .requestMatchers("/api/users/register", "/api/users/login").permitAll()
                // sadece admin tüm kullanıcıları görebilir
                .requestMatchers("/api/users").hasRole("ADMIN")
                // diğer her şey token gerektirir
                .anyRequest().authenticated()

                )

                // Session tutma — JWT stateless çalışır
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )

                // JWT filter'ı Spring Security'nin önüne ekle
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}