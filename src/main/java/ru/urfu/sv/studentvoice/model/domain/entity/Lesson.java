package ru.urfu.sv.studentvoice.model.domain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "lessons")
public class Lesson extends AbstractEntity {

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "status", nullable = false)
    private String status;

    @Column(name = "course_id", nullable = false)
    private Long courseId;

    @Column(name = "institute_id", nullable = false)
    private Long instituteId;

    @Column(name = "start_date_time", nullable = false)
    private LocalDateTime startDateTime;

    @Column(name = "end_date_time", nullable = false)
    private LocalDateTime endDateTime;

    @Column(name = "cabinet", nullable = false)
    private String cabinet;

    @Column(name = "disable_timestamp")
    private LocalDateTime disableTimestamp;

    @Column(name = "create_timestamp")
    private LocalDateTime createTimestamp;

    @Column(name = "address", nullable = false)
    private String address;

    @Column(name = "created_qr", nullable = false)
    private boolean createdQR;
}