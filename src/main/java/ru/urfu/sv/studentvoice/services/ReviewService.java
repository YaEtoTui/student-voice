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
import ru.urfu.sv.studentvoice.model.domain.dto.CategoryReviews;
import ru.urfu.sv.studentvoice.model.domain.dto.response.ReviewResponse;
import ru.urfu.sv.studentvoice.model.domain.dto.response.StudentResponse;
import ru.urfu.sv.studentvoice.model.domain.dto.review.ReviewDto;
import ru.urfu.sv.studentvoice.model.domain.entity.*;
import ru.urfu.sv.studentvoice.model.query.ReviewQuery;
import ru.urfu.sv.studentvoice.model.repository.ReviewRepository;
import ru.urfu.sv.studentvoice.services.mapper.ReviewMapper;

import java.util.List;

@Slf4j
@Service
public class ReviewService {

    @Autowired
    private LessonService sessionService;
    @Autowired
    private ReportService reportService;
    @Autowired
    private ReviewRepository repository;
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

//    public List<Review> findReviewsBySessionId(UUID sessionId) {
//        return repository.findAllBySessionId(sessionId);
//    }

    /**
     * Ищем отзывы студентов по паре
     */
    @PreAuthorize("@RolesAC.isProfessor()")
    public Page<ReviewResponse> findReviewsByLessonId(Long lessonId, Pageable pageable) {

        final QReview review = new QReview("review");
        final QLessonsReview lessonsReview = new QLessonsReview("lessonsReview");
        final QComment comment = new QComment("comment");

        final JPQLQuery<?> query = reviewQuery.findReviewsByLessonId(lessonId, CategoryReviews.REVIEW_STUDENT);
        final long count = query.select(lessonsReview.id).fetchCount();

        final JPQLQuery<?> queryPageable = new Querydsl(entityManager, new PathBuilderFactory().create(ReviewDto.class)).applyPagination(pageable, query);

        final List<ReviewDto> reviewList = queryPageable.select(
                        Projections.bean(ReviewDto.class,
                                lessonsReview.studentFullName.as("fio"),
                                review.value.as("value"),
                                review.createTimestamp.as("createTime"),
                                comment.value.as("review")
                        ))
                .fetch();

        final List<ReviewResponse> reviewResponseList = reviewMapper.createReviewResponseListFromReviewDtoList(reviewList);
        return new PageImpl<>(reviewResponseList, pageable, count);
    }

    /**
     * Ищем отзывы студентов по паре
     */
    @PreAuthorize("@RolesAC.isProfessor()")
    public Page<StudentResponse> findStudentsByLessonId(Long lessonId, Pageable pageable) {

        final QLessonsReview lessonsReview = new QLessonsReview("lessonsReview");

        final JPQLQuery<?> query = reviewQuery.findReviewsByLessonId(lessonId, CategoryReviews.REVIEW_STUDENT);
        final long count = query.select(lessonsReview.id).fetchCount();

        final JPQLQuery<?> queryPageable = new Querydsl(entityManager, new PathBuilderFactory().create(ReviewDto.class)).applyPagination(pageable, query);

        final List<StudentResponse> reviewList = queryPageable.select(
                        Projections.bean(StudentResponse.class,
                                lessonsReview.studentFullName.as("fio")
                        ))
                .fetch();
        /* ToDo не понятно как искать всех и отмечать */
        reviewList.forEach(review -> review.setVisitedLesson(true));
        return new PageImpl<>(reviewList, pageable, count);
    }
}