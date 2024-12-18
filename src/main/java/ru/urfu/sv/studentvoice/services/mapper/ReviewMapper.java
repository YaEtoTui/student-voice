package ru.urfu.sv.studentvoice.services.mapper;

import org.springframework.stereotype.Component;
import ru.urfu.sv.studentvoice.model.domain.dto.StatisticRating;
import ru.urfu.sv.studentvoice.model.domain.dto.StudentRating;
import ru.urfu.sv.studentvoice.model.domain.dto.response.ReviewResponse;
import ru.urfu.sv.studentvoice.utils.RatingUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Component
public class ReviewMapper {

    public List<ReviewResponse> createReviewResponse(Map<Long, StudentRating> studentRatingMap) {

        final List<ReviewResponse> reviewResponseList = new ArrayList<>();

        for (final Long key : studentRatingMap.keySet()) {
            final StudentRating studentRating = studentRatingMap.getOrDefault(key, new StudentRating());

            final ReviewResponse reviewResponse = new ReviewResponse();
            reviewResponse.setLessonReviewId(studentRating.getLessonReviewId());
            reviewResponse.setFio(studentRating.getFio());
            reviewResponse.setCreateTime(studentRating.getCreateTime());
            reviewResponse.setComment(studentRating.getComment());


            final List<StatisticRating> statisticRatingList = studentRating.getReviewCategoryList()
                    .stream()
                    .filter(val -> Objects.nonNull(val.getRating()) && Objects.nonNull(val.getCategoryName()))
                    .collect(Collectors.toList());

            reviewResponse.setStatisticRatingList(statisticRatingList);
            reviewResponse.setRatingResult(RatingUtil.calculateRatingForLesson(studentRating));

            reviewResponseList.add(reviewResponse);
        }

        return reviewResponseList;
    }
}