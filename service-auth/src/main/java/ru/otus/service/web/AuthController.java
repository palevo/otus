package ru.otus.service.web;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ServerWebExchange;

import reactor.core.publisher.Mono;
import ru.otus.common.domain.User;
import ru.otus.common.service.UserService;

import static java.util.stream.Collectors.toList;

import static reactor.core.publisher.Mono.just;

/**
 * Auth api controller
 *
 * @author A.Osipov
 * @since 19 апр. 2020 г.
 */
@RestController
@RequestMapping("auth")
public class AuthController {

    private static final Mono<ResponseEntity<User>> MONO_BAD_REQUEST = just(ResponseEntity.badRequest().build());

    private final UserService service;
    private final BCryptPasswordEncoder encoder;
    private final WebClient web;

    @Value("${otus.version}")
    private String version;

    /**
     * Constructor
     */
    public AuthController(UserService service, BCryptPasswordEncoder encoder, WebClient web) {
        this.service = service;
        this.encoder = encoder;
        this.web = web;
    }

    @GetMapping
    public Mono<ResponseEntity<String>> auth() {
        return just(ResponseEntity.ok(version));
    }

    @RequestMapping(path = "login", method = RequestMethod.POST)
    public Mono<ResponseEntity<Void>> login(@RequestParam String email, @RequestParam String password) {
        return web.post().uri("/login")
                .body(BodyInserters.fromFormData("username", email).with("password", password))
                .retrieve().toBodilessEntity();
    }

    @RequestMapping(path = "logout", method = RequestMethod.POST)
    public Mono<ResponseEntity<Void>> logout(ServerWebExchange exchange) {
        return web.post().uri("/logout")
                .cookies(map -> {
                    for (Map.Entry<String, List<HttpCookie>> entry : exchange.getRequest().getCookies().entrySet()) {
                        map.put(entry.getKey(), entry.getValue().stream().map(HttpCookie::getValue).collect(toList()));
                    }
                })
                .retrieve().toBodilessEntity();
    }

    @PutMapping("register")
    public Mono<ResponseEntity<User>> register(@RequestParam String email, @RequestParam String password, @RequestParam String name) {
        return service.byEmail(email)
                .flatMap(dbUser -> MONO_BAD_REQUEST)
                .switchIfEmpty(
                        service.create(email, encoder.encode(password), name)
                                .flatMap(u -> just(ResponseEntity.ok(u)))
                                .defaultIfEmpty(ResponseEntity.badRequest().build())
                );
    }
}
