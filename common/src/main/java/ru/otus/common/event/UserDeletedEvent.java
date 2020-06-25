package ru.otus.common.event;

import ru.otus.common.domain.User;

/**
 * User deleted event
 *
 * @author A.Osipov
 * @since 03 май 2020 г.
 */
public class UserDeletedEvent extends UserEvent {

    public UserDeletedEvent(User user) {
        super(user, "deleted");
    }
}
