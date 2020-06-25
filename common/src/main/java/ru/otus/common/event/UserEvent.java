package ru.otus.common.event;

import java.text.SimpleDateFormat;

import org.springframework.context.ApplicationEvent;

import ru.otus.common.domain.User;

import static java.lang.String.format;

/**
 * User event
 *
 * @author A.Osipov
 * @since 03 май 2020 г.
 */
public abstract class UserEvent extends ApplicationEvent {

    private final String operation;

    public UserEvent(User user, String operation) {
        super(user);
        this.operation = operation;
    }

    @Override
    public User getSource() {
        return (User) super.getSource();
    }

    public String getMessage() {
        return format("User #%s, %s, %s was %s, time %s", getSource().getId(), getSource().getName(), getSource().getEmail(),
                operation, new SimpleDateFormat("dd.MM.yyyy HH:mm:ss").format(getTimestamp()));
    }
}
