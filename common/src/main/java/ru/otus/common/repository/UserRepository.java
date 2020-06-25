package ru.otus.common.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ru.otus.common.domain.User;

/**
 * User repository
 *
 * @author A.Osipov
 * @since 27 апр. 2020 г.
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * Find user by email
     *
     * @param email user email
     * @return user
     */
    User findByEmail(String email);

    /**
     * Find user by email & password
     *
     * @param email    user email
     * @param password user password
     * @return user
     */
    User findByEmailAndPassword(String email, String password);
}
