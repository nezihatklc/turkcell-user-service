package com.turkcell.user_service.service;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

// Her HTTP isteğinde bir kez çalışan filtre
// Token varsa doğrular ve role bilgisini Spring Security'ye verir
@Component
@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {

    private final JwtService jwtService;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain) throws ServletException, IOException {

        // İstek header'ından Authorization bilgisini al
        // Örnek: "Bearer eyJhbGci..."
        String authHeader = request.getHeader("Authorization");

        // Header yoksa veya Bearer ile başlamıyorsa filtreyi geç
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        // "Bearer " kısmını atıp sadece token'ı al
        String token = authHeader.substring(7);

        // Token'dan email'i çıkar
        String email = jwtService.extractEmail(token);

        // Email var ve henüz authenticate edilmemişse
        if (email != null && SecurityContextHolder.getContext().getAuthentication() == null) {

            // Token geçerli mi kontrol et
            if (jwtService.isTokenValid(token, email)) {

                // Token'dan role'ü çıkar — örnek: "ADMIN" veya "USER"
                String role = jwtService.extractRole(token);

                // Spring Security için role'ü "ROLE_ADMIN" formatına çevir
                // Spring Security hasRole("ADMIN") ile eşleşmesi için "ROLE_" prefix'i zorunlu
                List<SimpleGrantedAuthority> authorities =
                        List.of(new SimpleGrantedAuthority("ROLE_" + role));

                // Spring Security'ye "bu kullanıcı doğrulandı ve şu role sahip" de
                UsernamePasswordAuthenticationToken authToken =
                        new UsernamePasswordAuthenticationToken(email, null, authorities);
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }

        // Bir sonraki filtreye geç
        filterChain.doFilter(request, response);
    }
}