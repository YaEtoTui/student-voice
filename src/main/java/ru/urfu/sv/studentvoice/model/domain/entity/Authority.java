package ru.urfu.sv.studentvoice.model.domain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "authorities")
@Getter
@NoArgsConstructor
public class Authority {
    @Id
    @Column(name = "username")
    private String username;

    @Column(name = "authority")
    private String authority;
}
