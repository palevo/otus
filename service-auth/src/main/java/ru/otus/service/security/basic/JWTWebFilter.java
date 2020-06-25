package ru.otus.service.security.basic;

import java.util.Objects;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;

import reactor.core.publisher.Mono;
import ru.otus.common.service.JWTService;

/**
 * Append json web token to headers
 *
 * @author A.Osipov
 * @since 25 июнь 2020 г.
 */
public class JWTWebFilter implements WebFilter {

    private final JWTService jwt;

    /**
     * @param jwt JWT service
     */
    public JWTWebFilter(JWTService jwt) {
        this.jwt = jwt;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain wfc) {
        ReactiveSecurityContextHolder.getContext()
                .map(SecurityContext::getAuthentication)
                .filter(Objects::nonNull)
                .map(authentication -> {
                    ServerHttpResponse response = exchange.getResponse();
                    response.setStatusCode(HttpStatus.OK);
                    response.getHeaders().add(HttpHeaders.AUTHORIZATION, "Bearer " + jwt.generate(authentication));
                    return response.setComplete();
                });
        return wfc.filter(exchange);
    }
}
