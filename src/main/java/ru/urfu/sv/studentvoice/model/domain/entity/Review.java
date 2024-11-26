package ru.urfu.sv.studentvoice.model.domain.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "reviews")
public class Review extends AbstractEntity {

    @Column(name = "lesson_review_id", nullable = false)
    private Long lessonReviewId;

    @Column(name = "category_id", nullable = false, unique = true)
    private Long categoryId;

    @Column(name = "value", nullable = false)
    private Long value;

    @Column(name = "create_timestamp", nullable = false)
    private LocalDateTime createTimestamp;

    @PrePersist
    protected void onCreate() {
        createTimestamp = LocalDateTime.now();
    }
}