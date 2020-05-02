package ru.otus.palevo.web;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.otus.palevo.AppConfiguration;
import ru.otus.palevo.model.Response;
import ru.otus.palevo.model.User;
import ru.otus.palevo.service.UserService;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

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

    @GetMapping(path = { "/", "/health" }, produces = APPLICATION_JSON_VALUE)
    public Mono<Response> health() {
        return Mono.justOrEmpty(new Response(config.getVersion(), OK));
    }

    @GetMapping(path = "/users", produces = APPLICATION_JSON_VALUE)
    public Flux<User> users() {
        return Flux.fromIterable(service.all());
    }
}
