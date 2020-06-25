package ru.otus.service.security;

import java.nio.charset.StandardCharsets;

import org.springframework.context.annotation.Bean;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UserDetailsRepositoryReactiveAuthenticationManager;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.authentication.AuthenticationWebFilter;
import org.springframework.security.web.server.authentication.ServerFormLoginAuthenticationConverter;
import org.springframework.security.web.server.authentication.logout.LogoutWebFilter;
import org.springframework.security.web.server.context.SecurityContextServerWebExchange;
import org.springframework.security.web.server.context.WebSessionServerSecurityContextRepository;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;
import ru.otus.common.service.JWTService;
import ru.otus.common.service.UserService;
import ru.otus.service.security.basic.AuthenticationSuccessHandler;
import ru.otus.service.security.basic.BasicUserDetailService;

import static org.springframework.http.HttpMethod.OPTIONS;
import static org.springframework.http.HttpMethod.POST;
import static org.springframework.http.HttpStatus.*;
import static org.springframework.security.config.web.server.SecurityWebFiltersOrder.AUTHENTICATION;
import static org.springframework.security.web.server.util.matcher.ServerWebExchangeMatchers.pathMatchers;

import static reactor.core.publisher.Mono.just;

/**
 * Webflux security configuration
 *
 * @author A.Osipov
 * @since 19 июнь 2020 г.
 */
@Slf4j
@EnableWebFluxSecurity
public class SecurityConfiguration {

    private static final byte[] UNAUTHORIZED_VALUE = "UNAUTHORIZED".getBytes(StandardCharsets.UTF_8);
    private static final byte[] FORBIDDEN_VALUE = "FORBIDDEN".getBytes(StandardCharsets.UTF_8);

    private final UserService users;
    private final JWTService jwt;

    /**
     * @param users user service
     * @param jwt   JWT service
     */
    public SecurityConfiguration(UserService users, JWTService jwt) {
        this.users = users;
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
                .pathMatchers("/auth").authenticated()
                .pathMatchers("/", "/auth/**", "/openapi/**", "/actuator/**").permitAll()
                .anyExchange().authenticated()

                .and()
                .addFilterAt(getLoginFilter(), AUTHENTICATION)
                .addFilterAt(getLogoutFilter(), AUTHENTICATION)
                .addFilterAt(getAuthenticationFilter(), AUTHENTICATION)

                .build();
    }

    private AuthenticationWebFilter getLoginFilter() {
        AuthenticationWebFilter filter = new AuthenticationWebFilter(
                new UserDetailsRepositoryReactiveAuthenticationManager(new BasicUserDetailService(users)) {{
                    setPasswordEncoder(passwordEncoder());
                }}
        );
        filter.setSecurityContextRepository(new WebSessionServerSecurityContextRepository());
        filter.setServerAuthenticationConverter(new ServerFormLoginAuthenticationConverter());
        filter.setAuthenticationSuccessHandler(new AuthenticationSuccessHandler(jwt));
        filter.setRequiresAuthenticationMatcher(pathMatchers(POST, "/login"));
        return filter;
    }

    private LogoutWebFilter getLogoutFilter() {
        LogoutWebFilter filter = new LogoutWebFilter();
        filter.setLogoutSuccessHandler((exchange, authentication) -> {
            ServerHttpResponse response = exchange.getExchange().getResponse();
            response.setStatusCode(OK);
            return response.setComplete();
        });
        filter.setRequiresLogoutMatcher(pathMatchers(POST, "/logout"));
        return filter;
    }

    private AuthenticationWebFilter getAuthenticationFilter() {
        AuthenticationWebFilter filter = new AuthenticationWebFilter((ReactiveAuthenticationManager) Mono::just);
        filter.setSecurityContextRepository(new WebSessionServerSecurityContextRepository());
        filter.setServerAuthenticationConverter(exchange -> new SecurityContextServerWebExchange(
                exchange, ReactiveSecurityContextHolder.getContext()).getPrincipal());
        filter.setAuthenticationSuccessHandler(new AuthenticationSuccessHandler(jwt));
        return filter;
    }
}
