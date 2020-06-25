package ru.otus.common.service;

import java.util.Date;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import ru.otus.common.config.JWTConfig;

import static java.lang.System.currentTimeMillis;
import static java.util.stream.Collectors.toList;

/**
 * JWT service.
 * Generate & validate token
 *
 * @author A.Osipov
 * @since 19 июнь 2020 г.
 */
@Component
public class JWTService {

    private final JWTConfig config;

    /**
     * Constructor
     */
    public JWTService(JWTConfig config) {
        this.config = config;
    }

    /**
     * Generate json web token
     *
     * @param authentication spring authentication
     * @return JWT
     */
    public String generate(Authentication authentication) {
        long currentTime = currentTimeMillis();
        return Jwts.builder()
                .setSubject(authentication.getName())
                .claim("authorities", authentication.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(toList()))
                .setIssuedAt(new Date(currentTime))
                .setExpiration(new Date(currentTime + config.getExpirationSeconds() * 1000))
                .signWith(SignatureAlgorithm.HS512, config.getSecret().getBytes())
                .compact();
    }

    /**
     * Get claims by json web token
     *
     * @param jwt json web token
     * @return a valid claims
     */
    public Claims parseClaims(String jwt) {
        return Jwts.parser()
                .setSigningKey(config.getSecret().getBytes())
                .parseClaimsJws(jwt)
                .getBody();
    }
}
