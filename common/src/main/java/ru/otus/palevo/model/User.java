package ru.otus.palevo.model;

import java.io.Serializable;
import java.util.Set;

import javax.persistence.*;
import javax.validation.constraints.Email;

import lombok.*;

/**
 * User
 *
 * @author A.Osipov
 * @since 27 апр. 2020 г.
 */
@Builder
@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "tbl_user")
public class User implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column
    private String name;
    @Email
    @Column(unique = true)
    private String email;
    @Column
    private String password;
    @OneToMany(fetch = FetchType.LAZY)
    private Set<UserRole> roles;
}
