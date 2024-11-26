package ru.urfu.sv.studentvoice.services.mapper;

import org.springframework.stereotype.Component;
import ru.urfu.sv.studentvoice.model.domain.dto.response.ReviewResponse;
import ru.urfu.sv.studentvoice.model.domain.dto.review.ReviewDto;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class ReviewMapper {

    /**
     * Переделываем List<ReviewDto> в List<ReviewResponse>
     *
     * @param reviewDtoList Список Отзывов
     */
    public List<ReviewResponse> createReviewResponseListFromReviewDtoList(Collection<ReviewDto> reviewDtoList) {
        return reviewDtoList.stream()
                .map(this::createReviewResponse)
                .collect(Collectors.toList());
    }

    /**
     * Переделываем ReviewDto в ReviewResponse
     *
     * @param reviewDto Отзыв
     */
    public ReviewResponse createReviewResponse(ReviewDto reviewDto) {
        final ReviewResponse reviewResponse = new ReviewResponse();
        reviewResponse.setFio(reviewDto.getFio());
        reviewResponse.setRating(reviewDto.getRating());
        reviewResponse.setCreateTime(reviewDto.getCreateTime());
        reviewResponse.setComment(reviewDto.getComment());

        return reviewResponse;
    }
}