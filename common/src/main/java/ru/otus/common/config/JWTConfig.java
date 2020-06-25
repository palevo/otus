package ru.otus.common.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import lombok.Getter;

/**
 * JWT config
 *
 * @author A.Osipov
 * @since 06 июнь 2020 г.
 */
@Getter
@Component
public class JWTConfig {

    @Value("${security.jwt.expiration:#{24*60*60}}")
    private int expirationSeconds;
    @Value("${security.jwt.secret:JwtSecretKey}")
    private String secret;
}
