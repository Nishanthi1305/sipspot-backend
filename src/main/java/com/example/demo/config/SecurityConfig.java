package com.example.demo.config;

import com.example.demo.security.JwtFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SecurityConfig {

    @Autowired
    private JwtFilter jwtFilter;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            // ❌ Disable CSRF (for APIs)
            .csrf(AbstractHttpConfigurer::disable)

            // ❌ No sessions (JWT based)
            .sessionManagement(session -> session
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            )

            // 🔐 Authorization rules
            .authorizeHttpRequests(auth -> auth

                // ✅ PUBLIC ENDPOINTS (IMPORTANT)
                .requestMatchers("/", "/test").permitAll()
                .requestMatchers("/api/auth/**").permitAll()

                // 🔐 ROLE-BASED ACCESS
                .requestMatchers("/api/admin/**").hasAuthority("ROLE_ADMIN")
                .requestMatchers("/api/owner/**").hasAuthority("ROLE_CAFE_OWNER")
                .requestMatchers("/api/chef/**").hasAuthority("ROLE_CHEF")
                .requestMatchers("/api/waiter/**").hasAuthority("ROLE_WAITER")
                .requestMatchers("/api/customer/**").hasAuthority("ROLE_CUSTOMER")

                // 🔐 AUTHENTICATED USERS
                .requestMatchers("/api/payment/**").authenticated()
                .requestMatchers("/api/booking/**").authenticated()
                .requestMatchers("/api/order/**").authenticated()

                // ❗ EVERYTHING ELSE REQUIRES LOGIN
                .anyRequest().authenticated()
            )

            // ✅ Add JWT filter
            .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    // 🔑 Password encoder
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}