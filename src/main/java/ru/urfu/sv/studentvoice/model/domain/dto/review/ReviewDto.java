package ru.urfu.sv.studentvoice.model.domain.dto.review;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class ReviewDto {

    private Long lessonReviewId;
    private String fio;
    private Long rating;
    private LocalDateTime createTime;
    private String comment;
    private String categoryName;
}