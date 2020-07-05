package ru.otus.service.security.basic;

import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.server.WebFilterExchange;
import org.springframework.security.web.server.authentication.ServerAuthenticationSuccessHandler;
import org.springframework.web.server.ServerWebExchange;

import reactor.core.publisher.Mono;
import ru.otus.common.service.JWTService;

/**
 * On success authentication a signed JWT object is serialized and added
 * in the authorization header as a bearer token
 *
 * @author A.Osipov
 * @since 19 июнь 2020 г.
 */
public class AuthenticationSuccessHandler implements ServerAuthenticationSuccessHandler {

    private final JWTService jwt;

    /**
     * @param jwt JWT service
     */
    public AuthenticationSuccessHandler(JWTService jwt) {
        this.jwt = jwt;
    }

    @Override
    public Mono<Void> onAuthenticationSuccess(WebFilterExchange wfe, Authentication authentication) {
        ServerWebExchange exchange = wfe.getExchange();
        ServerHttpResponse response = exchange.getResponse();
        response.getHeaders().add(HttpHeaders.AUTHORIZATION, "Bearer " + jwt.generate(authentication));
        return Mono.empty();
    }
}
