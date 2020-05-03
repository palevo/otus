package ru.otus.palevo.service;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.otus.palevo.event.UserCreatedEvent;
import ru.otus.palevo.event.UserDeletedEvent;
import ru.otus.palevo.model.User;
import ru.otus.palevo.repository.UserRepository;

/**
 * User service
 *
 * @author A.Osipov
 * @since 27 апр. 2020 г.
 */
@Service
public class UserService {

    private final ApplicationEventPublisher publisher;
    private final UserRepository userRepository;

    /**
     * Constructor
     */
    public UserService(ApplicationEventPublisher publisher, UserRepository userRepository) {
        this.publisher = publisher;
        this.userRepository = userRepository;
    }

    /**
     * @return users
     */
    public Flux<User> all() {
        return Flux.fromIterable(userRepository.findAll());
    }

    /**
     * @return founded user
     */
    public Mono<User> one(Long id) {
        return Mono.justOrEmpty(userRepository.findById(id));
    }

    /**
     * @param name  user name
     * @param email user email
     * @return saved user
     */
    public Mono<User> create(String name, String email) {
        return Mono.justOrEmpty(userRepository.save(new User(name, email)))
                .doOnSuccess(createdUser -> publisher.publishEvent(new UserCreatedEvent(createdUser)));
    }

    /**
     * @param name  user name
     * @param email user email
     * @return saved user
     */
    public Mono<User> update(Long id, String name, String email) {
        return Mono.justOrEmpty(userRepository.findById(id).map(u -> {
            u.setName(name);
            u.setEmail(email);
            return userRepository.save(u);
        }));
    }

    /**
     * @param id user id
     * @return deleted user
     */
    public Mono<User> delete(Long id) {
        return Mono.justOrEmpty(userRepository.findById(id).map(u -> {
            userRepository.deleteById(id);
            return u;
        })).doOnSuccess(deletedUser -> publisher.publishEvent(new UserDeletedEvent(deletedUser)));
    }
}
