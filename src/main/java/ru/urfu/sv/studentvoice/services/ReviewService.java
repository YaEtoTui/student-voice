package ru.urfu.sv.studentvoice.services;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.PathBuilderFactory;
import com.querydsl.jpa.JPQLQuery;
import jakarta.persistence.EntityManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.Querydsl;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import ru.urfu.sv.studentvoice.model.domain.dto.StatisticRating;
import ru.urfu.sv.studentvoice.model.domain.dto.StudentRating;
import ru.urfu.sv.studentvoice.model.domain.dto.response.ReviewResponse;
import ru.urfu.sv.studentvoice.model.domain.dto.review.ReviewDto;
import ru.urfu.sv.studentvoice.model.domain.entity.QCategoryReview;
import ru.urfu.sv.studentvoice.model.domain.entity.QComment;
import ru.urfu.sv.studentvoice.model.domain.entity.QLessonsReview;
import ru.urfu.sv.studentvoice.model.domain.entity.QReview;
import ru.urfu.sv.studentvoice.model.query.ReviewQuery;
import ru.urfu.sv.studentvoice.services.mapper.ReviewMapper;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
public class ReviewService {

    @Autowired
    private ReviewQuery reviewQuery;
    @Autowired
    private ReviewMapper reviewMapper;
    @Autowired
    protected EntityManager entityManager;

//    @Transactional
//    public ActionResult saveReview(Review review) {
//        Optional<ClassSession> classSession = sessionService.findSessionById(review.getSessionId());
//
//        if (classSession.isEmpty()) {
//            String message = "Пара из отзыва %s не найдена".formatted(review);
//            log.warn(message);
//            throw new IllegalArgumentException(message);
//        }
//
//        Review saved = repository.save(review);
//        return repository.existsById(saved.getReviewId()) ? ActionResultFactory.reviewCreated(saved.toString()) : ActionResultFactory.reviewCreatingError();
//    }

//    public Float getAverageRatingBySessions(List<ClassSession> sessions) {
//        List<UUID> sessionsIds = sessions
//                .stream()
//                .map(ClassSession::getSessionId)
//                .toList();
//
//        if(sessionsIds.isEmpty()) return 0f;
//
//        List<Review> reviews = repository.findAllBySessionsIds(sessionsIds);
//
//        Integer sum = 0;
//        for (Review review : reviews) {
//            sum += review.getValue();
//        }
//
//        return reviews.isEmpty() ? 0f : (float) sum / reviews.size();
//    }

    /**
     * Ищем отзывы студентов по паре
     */
    @PreAuthorize("@RolesAC.isProfessor()")
    public Page<ReviewResponse> findReviewsByLessonId(Long lessonId, Pageable pageable) {

        final QReview review = new QReview("review");
        final QLessonsReview lessonsReview = new QLessonsReview("lessonsReview");
        final QComment comment = new QComment("comment");
        final QCategoryReview categoryReview = new QCategoryReview("categoryReview");

        final JPQLQuery<?> query = reviewQuery.findReviewsByLessonId(lessonId);
        final long count = query.select(lessonsReview.id).fetchCount();

        final JPQLQuery<?> queryPageable = new Querydsl(entityManager, new PathBuilderFactory().create(ReviewDto.class)).applyPagination(pageable, query);

        final List<ReviewDto> reviewList = queryPageable.select(
                Projections.bean(ReviewDto.class,
                        lessonsReview.id.as("lessonReviewId"),
                        lessonsReview.studentFullName.as("fio"),
                        lessonsReview.createTimestamp.as("createTime"),
                        comment.value.as("comment"),
                        review.value.as("rating"),
                        categoryReview.name.as("categoryName")
                )).fetch();

        final Map<Long, StudentRating> studentRatingMap = reviewList.stream()
                .collect(Collectors.groupingBy(
                        ReviewDto::getLessonReviewId,
                        Collectors.collectingAndThen(
                                Collectors.toList(),
                                reviewDtoList -> {
                                    final StudentRating studentRating = new StudentRating();
                                    studentRating.setLessonReviewId(reviewDtoList.get(0).getLessonReviewId());
                                    studentRating.setFio(reviewDtoList.get(0).getFio());
                                    studentRating.setCreateTime(reviewDtoList.get(0).getCreateTime());
                                    studentRating.setComment(reviewDtoList.get(0).getComment());

                                    final List<StatisticRating> reviewCategoryList = reviewDtoList.stream()
                                            .map(reviewDto -> new StatisticRating(reviewDto.getRating(), reviewDto.getCategoryName()))
                                            .collect(Collectors.toList());

                                    studentRating.setReviewCategoryList(reviewCategoryList);
                                    return studentRating;
                                }
                        )
                ));

        final List<ReviewResponse> reviewResponseList = reviewMapper.createReviewResponse(studentRatingMap);
        return new PageImpl<>(reviewResponseList, pageable, count);
    }
}