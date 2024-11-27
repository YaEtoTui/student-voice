package ru.urfu.sv.studentvoice.model.domain.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import ru.urfu.sv.studentvoice.model.domain.dto.StatisticRating;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class ReviewResponse {

    @JsonProperty("lesson_review_id")
    private Long lessonReviewId;

    private String fio;

    @JsonProperty("create_time")
    private LocalDateTime createTime;

    @JsonProperty("rating_result")
    private BigDecimal ratingResult;

    private String comment;

    @JsonProperty("statistic_rating_list")
    private List<StatisticRating> statisticRatingList;
}