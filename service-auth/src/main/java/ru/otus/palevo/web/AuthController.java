package ru.otus.palevo.web;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import reactor.core.publisher.Mono;
import ru.otus.palevo.model.User;
import ru.otus.palevo.service.UserService;

/**
 * Auth controller
 *
 * @author A.Osipov
 * @since 19 апр. 2020 г.
 */
@RequestMapping("auth")
@RestController
public class AuthController {

    private final UserService service;

    /**
     * Constructor
     */
    public AuthController(UserService service) {
        this.service = service;
    }

    @PutMapping("register")
    public Mono<User> register() {
        return null;
    }

    @PostMapping("login")
    public Mono<ResponseEntity<User>> login(@RequestParam String username, @RequestParam String password) {
        return null;
    }

    @PostMapping("logout")
    public Mono<ResponseEntity<User>> logout() {
        return null;
    }
}
