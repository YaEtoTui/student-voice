package ru.urfu.sv.studentvoice.model.domain.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "users")
public class User extends AbstractEntity {

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "username", unique = true, nullable = false)
    private String username;

    @Column(name = "active", nullable = false)
    private boolean active;

    @Column(name = "name")
    private String name;

    @Column(name = "surname")
    private String surname;

    @Column(name = "patronymic")
    private String patronymic;

    @Column(name = "rating")
    private BigDecimal rating;
}