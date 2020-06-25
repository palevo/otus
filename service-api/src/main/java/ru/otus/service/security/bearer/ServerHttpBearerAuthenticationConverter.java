package ru.otus.service.security.bearer;

import org.springframework.http.HttpHeaders;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.server.authentication.ServerAuthenticationConverter;
import org.springframework.web.server.ServerWebExchange;

import reactor.core.publisher.Mono;
import ru.otus.common.service.JWTService;

import static reactor.core.publisher.Mono.justOrEmpty;

/**
 * Authentication by authorization header (JWT)
 *
 * @author A.Osipov
 * @since 19 июнь 2020 г.
 */
public class ServerHttpBearerAuthenticationConverter implements ServerAuthenticationConverter {

    private static final String BEARER = "Bearer ";
    private final JWTService jwt;

    /**
     * @param jwt JWT service
     */
    public ServerHttpBearerAuthenticationConverter(JWTService jwt) {
        this.jwt = jwt;
    }

    @Override
    public Mono<Authentication> convert(ServerWebExchange exchange) {
        return justOrEmpty(exchange)
                // get authorization header
                .flatMap(swe -> justOrEmpty(swe.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION)))
                // get authorization header value (JWT)
                .filter(authorization -> authorization.length() > BEARER.length())
                .flatMap(authorization -> justOrEmpty(authorization.substring(BEARER.length())))
                // verification JWT
                .map(jwt::parseClaims)
                // return authentication
                .flatMap(UsernamePasswordAuthenticationBearer::create).log();
    }
}
