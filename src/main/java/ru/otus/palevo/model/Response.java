package ru.otus.palevo.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Health indicator
 *
 * @author A.Osipov
 * @since 19 апр. 2020 г.
 */
@Getter
@AllArgsConstructor
public class Response {

    public enum StatusType {

        OK
    }

    private final String version;
    private final StatusType status;
}
