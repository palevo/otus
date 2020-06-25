package ru.otus.service.security.bearer;

import java.util.List;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import io.jsonwebtoken.Claims;
import reactor.core.publisher.Mono;

import static java.util.stream.Collectors.toList;

/**
 * This converter takes a SignedJWT and extracts all information
 * contained to build an Authentication Object
 * The signed JWT has already been verified.
 *
 * @author A.Osipov
 * @since 19 июнь 2020 г.
 */
public class UsernamePasswordAuthenticationBearer {

    /**
     * Create authentication by JWT claims
     *
     * @param claims JWT claims
     * @return spring authentication
     */
    @SuppressWarnings("unchecked")
    public static Mono<Authentication> create(Claims claims) {
        List<String> authorities = (List<String>) claims.get("authorities");
        return Mono.justOrEmpty(new UsernamePasswordAuthenticationToken(claims.getSubject(), null,
                authorities.stream().map(SimpleGrantedAuthority::new).collect(toList())));
    }
}
