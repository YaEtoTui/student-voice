package ru.urfu.sv.studentvoice.model.query;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.JPQLQuery;
import org.springframework.stereotype.Repository;
import ru.urfu.sv.studentvoice.model.domain.entity.*;

@Repository
public class ReviewQuery extends AbstractQuery {

    private final static QReview review = new QReview("review");
    private final static QLessonsReview lessonsReview = new QLessonsReview("lessonsReview");
    private final static QLesson lesson = new QLesson("lesson");
    private final static QCategoryReview categoryReview = new QCategoryReview("categoryReview");
    private final static QComment comment = new QComment("comment");


    public JPQLQuery<?> findReviewsByLessonId(Long lessonId) {

        final BooleanExpression exp = lesson.id.eq(lessonId);

        return query()
                .from(lessonsReview)
                .leftJoin(lesson).on(lessonsReview.lessonId.eq(lesson.id))
                .leftJoin(review).on(review.lessonReviewId.eq(lessonsReview.id))
                .leftJoin(comment).on(comment.lessonReviewId.eq(lessonsReview.id))
                .where(exp);
    }
}