package ru.urfu.sv.studentvoice.model.domain.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "courses")
public class Course extends AbstractEntity {

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "institute_id", nullable = false)
    private Long instituteId;

    @Column(name = "create_timestamp", nullable = false)
    private LocalDateTime createTimestamp;

    @Column(name = "rating")
    private BigDecimal rating;

    @Column(name = "address")
    private String address;

    @PrePersist
    protected void onCreate() {
        createTimestamp = LocalDateTime.now();
    }
}