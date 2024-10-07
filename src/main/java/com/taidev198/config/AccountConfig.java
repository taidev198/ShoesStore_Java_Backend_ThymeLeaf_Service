package com.taidev198.config;

import com.taidev198.repository.AccountRepository;
import com.taidev198.util.exception.NotFoundObjectException;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@RequiredArgsConstructor
public class AccountConfig {
    private final AccountRepository repo;

    @Bean
    public UserDetailsService userDetailsService() {
        return email -> {
            if (email != null && !email.isBlank()) {
                return repo.findByEmail(email)
                    .orElseThrow(() -> new NotFoundObjectException("Tài khoản không tồn tại"));
            }
            throw new NotFoundObjectException("Tài khoản không tồn tại");
        };
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService());
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }
}
