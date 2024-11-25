package ru.urfu.sv.studentvoice.model.query;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.JPQLQuery;
import org.springframework.stereotype.Repository;
import ru.urfu.sv.studentvoice.model.domain.dto.CategoryReviews;
import ru.urfu.sv.studentvoice.model.domain.entity.*;

@Repository
public class ReviewQuery extends AbstractQuery {

    private final static QReview review = new QReview("review");
    private final static QLessonsReview lessonsReview = new QLessonsReview("lessonsReview");
    private final static QLesson lesson = new QLesson("lesson");
    private final static QCategoryReview categoryReview = new QCategoryReview("categoryReview");
    private final static QComment comment = new QComment("comment");


    public JPQLQuery<?> findReviewsByLessonId(Long lessonId, CategoryReviews categoryReviews) {

        final BooleanExpression exp = lesson.id.eq(lessonId)
                .and(categoryReview.name.eq(categoryReviews.getName()));

        return query()
                .from(lessonsReview)
                .join(lesson).on(lessonsReview.lessonId.eq(lesson.id))
                .join(review).on(review.lessonReviewId.eq(lessonsReview.id))
                .join(categoryReview).on(review.categoryId.eq(categoryReview.id))
                .join(comment).on(comment.lessonReviewId.eq(lessonsReview.id))
                .where(exp);
    }
}