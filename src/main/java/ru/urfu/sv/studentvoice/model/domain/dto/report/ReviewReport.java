package ru.urfu.sv.studentvoice.model.domain.dto.report;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class ReviewReport {

    /* lesson */
    private String courseName;
    private String lessonName;
    private String instituteName;
    private LocalDateTime startDateTime;
    private LocalDateTime endDateTime;

    /* lesson_review */
    private String studentName;
    private LocalDateTime createReview;
}