package ru.urfu.sv.studentvoice.utils;

import ru.urfu.sv.studentvoice.model.domain.dto.StatisticRating;
import ru.urfu.sv.studentvoice.model.domain.dto.StudentRating;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class RatingUtil {

    /**
     * Считаем рейтинг пары по отзывам студента
     *
     * @param studentRating Данные по подсчету рейтинга студента
     * @return Рейтинг преподавателя по паре
     */
    public static BigDecimal calculateRatingForLesson(StudentRating studentRating) {

        final List<StatisticRating> reviewCategoryList = studentRating.getReviewCategoryList();

        if (reviewCategoryList == null || reviewCategoryList.isEmpty()) {
            return BigDecimal.ZERO;
        }

        final List<StatisticRating> nonNullRatings = reviewCategoryList.stream()
                .filter(Objects::nonNull)
                .filter(rating -> rating.getRating() != null)
                .collect(Collectors.toList());

        if (nonNullRatings.isEmpty()) {
            return BigDecimal.ZERO;
        }

        final long sum = nonNullRatings.stream()
                .mapToLong(StatisticRating::getRating)
                .sum();

        return BigDecimal.valueOf(sum).divide(BigDecimal.valueOf(nonNullRatings.size()), 2, RoundingMode.HALF_UP);
    }
}