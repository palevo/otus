package ru.otus.service.security;

import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.authentication.AuthenticationWebFilter;

import reactor.core.publisher.Mono;
import ru.otus.common.service.JWTService;
import ru.otus.service.security.bearer.ServerHttpBearerAuthenticationConverter;

import static org.springframework.security.config.web.server.SecurityWebFiltersOrder.AUTHENTICATION;

/**
 * Webflux security configuration
 *
 * @author A.Osipov
 * @since 19 июнь 2020 г.
 */
@EnableWebFluxSecurity
public class SecurityConfiguration {

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

                .authorizeExchange()
                .pathMatchers("/openapi/**", "/actuator/**").permitAll()
                .pathMatchers("/**").authenticated()

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
