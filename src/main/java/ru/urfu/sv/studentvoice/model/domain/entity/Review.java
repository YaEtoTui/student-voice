package ru.urfu.sv.studentvoice.model.domain.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "reviews")
public class Review extends AbstractEntity {

//    @Column(name = "lesson_review_id", nullable = false)
//    private Long lessonReviewId;

//    @Column(name = "category_id", nullable = false)
//    private Long categoryId;

    @Column(name = "value", nullable = false)
    private String value;

    @Column(name = "create_timestamp", nullable = false)
    private Instant createTimestamp;

    @PrePersist
    protected void onCreate() {
        createTimestamp = Instant.now();
    }
}