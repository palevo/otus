package ru.otus.common.service;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.otus.common.domain.User;
import ru.otus.common.event.UserCreatedEvent;
import ru.otus.common.event.UserDeletedEvent;
import ru.otus.common.repository.UserRepository;

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
     * @param id user id
     * @return founded user
     */
    public Mono<User> one(Long id) {
        return Mono.justOrEmpty(userRepository.findById(id));
    }

    /**
     * @param email user email
     * @return founded user
     */
    public Mono<User> byEmail(String email) {
        return Mono.justOrEmpty(userRepository.findByEmail(email));
    }

    /**
     * @param email    user email
     * @param password user password
     * @return founded user
     */
    public Mono<User> login(String email, String password) {
        return Mono.justOrEmpty(userRepository.findByEmailAndPassword(email, password));
    }

    /**
     * @param email    user email
     * @param password user password
     * @param name     user name
     * @return saved user
     */
    public Mono<User> create(String email, String password, String name) {
        return Mono.justOrEmpty(userRepository.save(User.builder().email(email).password(password).name(name).build()))
                .doOnNext(createdUser -> publisher.publishEvent(new UserCreatedEvent(createdUser)));
    }

    /**
     * @param id    user id
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
        })).doOnNext(deletedUser -> publisher.publishEvent(new UserDeletedEvent(deletedUser)));
    }
}
