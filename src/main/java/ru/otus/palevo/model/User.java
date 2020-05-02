package ru.otus.palevo.model;

import javax.persistence.*;

/**
 * User
 *
 * @author A.Osipov
 * @since 27 апр. 2020 г.
 */
@Entity
@Table(name = "tbl_user")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column
    private String name;
    @Column
    private String email;
}
