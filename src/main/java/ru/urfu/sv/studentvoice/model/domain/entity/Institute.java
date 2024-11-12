package ru.urfu.sv.studentvoice.model.domain.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "institutes")
public class Institute extends AbstractEntity {

    @Column(name = "full_name", nullable = false)
    private String fullName;

    @Column(name = "short_name", nullable = false)
    private String shortName;

    @Column(name = "address", nullable = false)
    private String address;
}