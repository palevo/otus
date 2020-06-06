package ru.otus.palevo.web;

import javax.validation.Valid;

import org.springframework.context.ApplicationListener;
import org.springframework.http.ResponseEntity;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.web.bind.annotation.*;

import reactor.core.publisher.*;
import ru.otus.palevo.event.UserEvent;
import ru.otus.palevo.model.User;
import ru.otus.palevo.service.UserService;

import static org.springframework.http.MediaType.TEXT_EVENT_STREAM_VALUE;

/**
 * Users API controller
 *
 * @author A.Osipov
 * @since 19 апр. 2020 г.
 */
@RequestMapping("users")
@RestController
public class UsersController implements ApplicationListener<UserEvent> {

    private final FluxProcessor<String, String> processor;
    private final FluxSink<String> sink;
    private final UserService service;

    /**
     * Constructor
     */
    public UsersController(UserService service) {
        DirectProcessor<String> direct = DirectProcessor.create();
        this.processor = direct.serialize();
        this.sink = processor.sink();
        this.service = service;
    }

    @GetMapping
    public Flux<User> getUsers() {
        return service.all();
    }

    @GetMapping(path = "/{id}")
    public Mono<ResponseEntity<User>> getUser(@PathVariable Long id) {
        return service.one(id).map(ResponseEntity::ok).defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @PutMapping
    public Mono<ResponseEntity<User>> createUser(@Valid @ModelAttribute User user) {
        return service.create(user.getEmail(), user.getPassword(), user.getName())
                .map(ResponseEntity::ok).defaultIfEmpty(ResponseEntity.badRequest().build());
    }

    @PostMapping(path = "/{id}")
    public Mono<ResponseEntity<User>> updateUser(@PathVariable Long id, @Valid @ModelAttribute User user) {
        return Mono.just(user).flatMap(u -> service.update(id, u.getName(), u.getEmail()))
                .map(ResponseEntity::ok).defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @DeleteMapping(path = "/{id}")
    public Mono<ResponseEntity<User>> deleteUser(@PathVariable Long id) {
        return service.delete(id).map(ResponseEntity::ok).defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @GetMapping(value = "/events", produces = TEXT_EVENT_STREAM_VALUE)
    public Flux<ServerSentEvent<String>> emitEvents() {
        return processor.map(e -> ServerSentEvent.builder(e).build());
    }

    @Override
    public void onApplicationEvent(UserEvent event) {
        sink.next(event.getMessage());
    }
}
