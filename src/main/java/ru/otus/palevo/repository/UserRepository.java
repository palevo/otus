package ru.otus.palevo.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import ru.otus.palevo.model.User;

/**
 * User repository
 *
 * @author A.Osipov
 * @since 27 апр. 2020 г.
 */
public interface UserRepository extends JpaRepository<User, Long> {

}
