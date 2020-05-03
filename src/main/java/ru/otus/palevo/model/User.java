package ru.otus.palevo.model;

import javax.persistence.*;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * User
 *
 * @author A.Osipov
 * @since 27 апр. 2020 г.
 */
@Getter
@Setter
@Entity
@NoArgsConstructor
@Table(name = "tbl_user")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column
    private String name;
    @Column
    private String email;

    /**
     * @param name  user name
     * @param email user email
     */
    public User(String name, String email) {
        this.name = name;
        this.email = email;
    }
}
