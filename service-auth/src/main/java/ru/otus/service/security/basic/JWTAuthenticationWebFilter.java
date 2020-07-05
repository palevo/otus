package ru.otus.service.security.basic;

import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.web.server.WebFilterExchange;
import org.springframework.security.web.server.authentication.ServerAuthenticationConverter;
import org.springframework.security.web.server.context.SecurityContextServerWebExchange;
import org.springframework.security.web.server.util.matcher.ServerWebExchangeMatcher;
import org.springframework.security.web.server.util.matcher.ServerWebExchangeMatchers;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;

import reactor.core.publisher.Mono;

/**
 * Append json web token to headers
 *
 * @author A.Osipov
 * @since 25 июнь 2020 г.
 */
public class JWTAuthenticationWebFilter implements WebFilter {

    private static final ServerAuthenticationConverter authenticationConverter;

    static {
        authenticationConverter = exchange -> new SecurityContextServerWebExchange(exchange, ReactiveSecurityContextHolder.getContext()).getPrincipal();
    }

    private ServerWebExchangeMatcher matcher = ServerWebExchangeMatchers.anyExchange();
    private AuthenticationSuccessHandler onSuccessHandler;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        return matcher.matches(exchange)
                .filter(ServerWebExchangeMatcher.MatchResult::isMatch)
                .flatMap(mr -> authenticationConverter.convert(exchange))
                .switchIfEmpty(chain.filter(exchange).then(Mono.empty()))
                .flatMap(authentication -> {
                    onSuccessHandler.onAuthenticationSuccess(new WebFilterExchange(exchange, chain), authentication);
                    return chain.filter(exchange).then(Mono.empty());
                });
    }

    public void setAuthenticationSuccessHandler(AuthenticationSuccessHandler onSuccessHandler) {
        this.onSuccessHandler = onSuccessHandler;
    }

    public void setRequiresAuthenticationMatcher(ServerWebExchangeMatcher pathMatchers) {
        matcher = pathMatchers;
    }
}
