package ru.otus.palevo.web;

import javax.validation.Valid;

import org.springframework.web.bind.annotation.*;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.otus.palevo.AppConfiguration;
import ru.otus.palevo.model.Response;
import ru.otus.palevo.model.User;
import ru.otus.palevo.service.UserService;

import static ru.otus.palevo.model.Response.StatusType.OK;

/**
 * API controller
 *
 * @author A.Osipov
 * @since 19 апр. 2020 г.
 */
@RestController
public class Router {

    private final AppConfiguration config;
    private final UserService service;

    /**
     * Constructor
     */
    public Router(AppConfiguration config, UserService service) {
        this.config = config;
        this.service = service;
    }

    @GetMapping(path = { "/", "/health" })
    public Mono<Response> health() {
        return Mono.justOrEmpty(new Response(config.getVersion(), OK));
    }

    @PostMapping(path = "/users")
    public Mono<User> saveUser(@Valid @ModelAttribute User user) {
        return Mono.justOrEmpty(service.save(user));
    }

    @DeleteMapping(path = "/users/{id}")
    public void deleteUser(@PathVariable Long id) {
        service.delete(id);
    }

    @GetMapping(path = "/users/{id}")
    public Mono<User> getUser(@PathVariable Long id) {
        return Mono.justOrEmpty(service.one(id));
    }

    @GetMapping(path = "/users")
    public Flux<User> getUsers() {
        return Flux.fromIterable(service.all());
    }
}
