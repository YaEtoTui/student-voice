package ru.urfu.sv.studentvoice.model.domain.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "courses")
public class Course extends AbstractEntity {

    @Column(name = "name", nullable = false)
    private String name;

//    @Column(name = "institute_id", nullable = false)
//    private Long instituteId;


    @Column(name = "create_timestamp", nullable = false)
    private Instant createTimestamp;

    @PrePersist
    protected void onCreate() {
        createTimestamp = Instant.now();
    }
}