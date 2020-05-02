package ru.otus.palevo.service;

import java.util.List;

import org.springframework.stereotype.Service;

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

    private final UserRepository userRepository;

    /**
     * Constructor
     */
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * @param user user for save
     * @return saved user
     */
    public User save(User user) {
        return userRepository.save(user);
    }

    /**
     * @param id user id
     */
    public void delete(Long id) {
        userRepository.deleteById(id);
    }

    /**
     * @return founded user
     */
    public User one(Long id) {
        return userRepository.getOne(id);
    }

    /**
     * @return users
     */
    public List<User> all() {
        return userRepository.findAll();
    }
}
