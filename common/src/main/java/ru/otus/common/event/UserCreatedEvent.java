package ru.otus.common.event;

import ru.otus.common.domain.User;

/**
 * User created event
 *
 * @author A.Osipov
 * @since 03 май 2020 г.
 */
public class UserCreatedEvent extends UserEvent {

    public UserCreatedEvent(User user) {
        super(user, "created");
    }
}
