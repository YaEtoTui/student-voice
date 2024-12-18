package ru.urfu.sv.studentvoice.model.domain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.PrePersist;
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
@Table(name = "lessons_review")
public class LessonsReview extends AbstractEntity {

    @Column(name = "lesson_id", nullable = false)
    private Long lessonId;

    @Column(name = "value")
    private String value;

    @Column(name = "create_timestamp", nullable = false)
    private LocalDateTime createTimestamp;

    @Column(name = "student_full_name", nullable = false)
    private String studentFullName;

    @PrePersist
    protected void onCreate() {
        createTimestamp = LocalDateTime.now();
    }
}