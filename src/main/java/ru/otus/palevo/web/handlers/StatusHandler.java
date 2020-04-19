package ru.otus.palevo.web.handlers;

import java.io.Serializable;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import reactor.core.publisher.Mono;

/**
 * Status handler
 *
 * @author A.Osipov
 * @since 19 апр. 2020 г.
 */
@Component
public class StatusHandler {

    public enum StatusType {

        OK
    }

    @Getter
    @Setter
    @AllArgsConstructor
    public static class Response implements Serializable {

        private StatusType status;
    }

    public Mono<ServerResponse> health(ServerRequest request) {
        return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(new Response(StatusType.OK)));
    }
}
