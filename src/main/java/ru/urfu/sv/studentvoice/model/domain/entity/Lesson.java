package ru.urfu.sv.studentvoice.model.domain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "lessons")
public class Lesson extends AbstractEntity {

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "course_id", nullable = false)
    private Long courseId;

    @Column(name = "institute_id", nullable = false)
    private Long instituteId;

    @Column(name = "start_date_time", nullable = false)
    private Instant startDateTime;

    @Column(name = "end_date_time", nullable = false)
    private Instant endDateTime;

    @Column(name = "cabinet", nullable = false)
    private String cabinet;

    @Column(name = "disable_timestamp")
    private Instant disableTimestamp;

    @Column(name = "create_timestamp", nullable = false)
    private Instant createTimestamp;

    @Column(name = "address", nullable = false)
    private String address;

    @Column(name = "created_qr", nullable = false)
    private boolean createdQR;

    @PrePersist
    protected void onCreate() {
        createTimestamp = Instant.now();
    }
}