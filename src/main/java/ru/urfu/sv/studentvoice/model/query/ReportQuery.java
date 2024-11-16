package ru.urfu.sv.studentvoice.model.query;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import org.springframework.stereotype.Repository;
import ru.urfu.sv.studentvoice.model.domain.entity.QComment;
import ru.urfu.sv.studentvoice.model.domain.entity.QLesson;
import ru.urfu.sv.studentvoice.model.domain.entity.QLessonsReview;
import ru.urfu.sv.studentvoice.model.domain.entity.QReview;
import ru.urfu.sv.studentvoice.utils.model.ReviewInfo;

import java.util.List;

@Repository
public class ReportQuery extends AbstractQuery {

    private final static QReview review = new QReview("review");
    private final static QLesson lesson = new QLesson("lesson");
    private final static QLessonsReview lessonsReview = new QLessonsReview("lessonsReview");
    private final static QComment comment = new QComment("comment");

    public List<ReviewInfo> findReviewInfo() {

        final BooleanExpression exp = null;

        return query()
                .from(review)
                .leftJoin(lessonsReview).on(review.lessonReviewId.eq(lessonsReview.id))
                .join(lesson).on(lessonsReview.lessonId.eq(lesson.id))
                .join(comment).on(comment.lessonReviewId.eq(comment.id))
                .where(exp)
                .select(Projections.bean(ReviewInfo.class,
                        lessonsReview.studentFullName.as("studentName"),
                        comment.value.as("comment")
                        // To Do
                        ))
                .fetch();
    }
}