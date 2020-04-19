package ru.otus.palevo.web;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

import ru.otus.palevo.web.handlers.StatusHandler;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RequestPredicates.accept;

/**
 * @author A.Osipov
 * @since 19 апр. 2020 г.
 */
@Configuration
public class Router {

    @Bean
    public RouterFunction<ServerResponse> route() {
        StatusHandler statusHandler = new StatusHandler();
        return RouterFunctions
                .route(GET("/").and(accept(APPLICATION_JSON)), statusHandler::health)
                .andRoute(GET("/health").and(accept(APPLICATION_JSON)), statusHandler::health);
    }
}
