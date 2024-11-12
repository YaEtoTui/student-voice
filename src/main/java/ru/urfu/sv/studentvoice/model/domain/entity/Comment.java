package ru.urfu.sv.studentvoice.model.domain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "comments")
public class Comment extends AbstractEntity {

    @Column(name = "lesson_review_id", nullable = false, unique = true)
    private Long lessonReviewId;

    @Column(name = "value", nullable = false)
    private String value;
}