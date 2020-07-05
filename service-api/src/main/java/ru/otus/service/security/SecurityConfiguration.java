package ru.otus.service.security;

import java.nio.charset.StandardCharsets;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.authentication.AuthenticationWebFilter;

import reactor.core.publisher.Mono;
import ru.otus.common.service.JWTService;
import ru.otus.service.security.bearer.ServerHttpBearerAuthenticationConverter;

import static org.springframework.http.HttpMethod.OPTIONS;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;
import static org.springframework.security.config.web.server.SecurityWebFiltersOrder.AUTHENTICATION;

import static reactor.core.publisher.Mono.just;

/**
 * Webflux security configuration
 *
 * @author A.Osipov
 * @since 19 июнь 2020 г.
 */
@EnableWebFluxSecurity
public class SecurityConfiguration {

    private static final byte[] UNAUTHORIZED_VALUE = "UNAUTHORIZED".getBytes(StandardCharsets.UTF_8);
    private static final byte[] FORBIDDEN_VALUE = "FORBIDDEN".getBytes(StandardCharsets.UTF_8);

    @Value("${springdoc.swagger-ui.path}")
    private String openapiBasePath;
    private final JWTService jwt;

    /**
     * @param jwt JWT service
     */
    public SecurityConfiguration(JWTService jwt) {
        this.jwt = jwt;
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http) {
        return http
                .csrf().disable()
                .httpBasic().disable()
                .formLogin().disable()
                .logout().disable()

                .exceptionHandling()
                .authenticationEntryPoint((swe, e) -> {
                    ServerHttpResponse response = swe.getResponse();
                    response.setStatusCode(UNAUTHORIZED);
                    return response.writeWith(just(response.bufferFactory().wrap(UNAUTHORIZED_VALUE)));
                })
                .accessDeniedHandler((swe, e) -> {
                    ServerHttpResponse response = swe.getResponse();
                    response.setStatusCode(FORBIDDEN);
                    return response.writeWith(just(response.bufferFactory().wrap(FORBIDDEN_VALUE)));
                })

                .and()
                .authorizeExchange()
                .pathMatchers(OPTIONS).permitAll()
                .pathMatchers("/", openapiBasePath + "/**", "/actuator/**").permitAll()
                .anyExchange().authenticated()

                .and()
                .addFilterAt(bearerAuthenticationFilter(), AUTHENTICATION)

                .build();
    }

    private AuthenticationWebFilter bearerAuthenticationFilter() {
        AuthenticationWebFilter filter = new AuthenticationWebFilter((ReactiveAuthenticationManager) Mono::just);
        filter.setServerAuthenticationConverter(new ServerHttpBearerAuthenticationConverter(jwt));
        return filter;
    }
}
