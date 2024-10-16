package com.taidev198.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Getter;

@ConfigurationProperties(prefix = "application")
@Getter
public class JwtApplicationProperty {
    @Value("${application.auth.access-token-secret-key}")
    private String accessTokenSecret;

    @Value("${application.auth.access-token-expiration-ms}")
    private Long accessTokenExpirationMs;

    @Value("${application.auth.refresh-token-secret-key}")
    private String refreshTokenSecret;

    @Value("${application.auth.refresh-token-expiration-ms}")
    private Long refreshTokenExpirationMs;
}
