package ru.otus.palevo.web;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import reactor.core.publisher.Mono;
import ru.otus.palevo.AppConfiguration;
import ru.otus.palevo.model.Response;

import static ru.otus.palevo.model.Response.StatusType.OK;

/**
 * API controller
 *
 * @author A.Osipov
 * @since 19 апр. 2020 г.
 */
@RestController
public class MainController {

    private final AppConfiguration config;

    /**
     * Constructor
     */
    public MainController(AppConfiguration config) {
        this.config = config;
    }

    @GetMapping(path = { "/", "/health" })
    public Mono<Response> health() {
        return Mono.justOrEmpty(new Response(config.getVersion(), OK));
    }
}
