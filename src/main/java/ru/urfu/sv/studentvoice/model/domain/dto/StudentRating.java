package ru.urfu.sv.studentvoice.model.domain.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class StudentRating {

    private Long lessonReviewId;
    private String fio;
    private LocalDateTime createTime;
    private String comment;
    private List<StatisticRating> reviewCategoryList;
}