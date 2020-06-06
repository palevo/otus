package ru.otus.palevo.model;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * User role
 *
 * @author A.Osipov
 * @since 27 апр. 2020 г.
 */
@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "tbl_user_role")
public class UserRole implements Serializable {

    @Id
    @JoinColumn
    private String code;
}
