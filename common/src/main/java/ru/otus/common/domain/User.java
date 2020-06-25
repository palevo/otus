package ru.otus.common.domain;

import java.io.Serializable;
import java.util.Set;

import javax.persistence.*;

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
@Table(name = "users", catalog = "otus")
public class User implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column
    private String name;
    @Column(unique = true)
    private String email;
    @Column
    private String password;
    @OneToMany(fetch = FetchType.LAZY)
    private Set<UserRole> roles;
}
