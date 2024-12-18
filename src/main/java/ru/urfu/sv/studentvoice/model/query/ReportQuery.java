package ru.urfu.sv.studentvoice.model.query;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import org.springframework.stereotype.Repository;
import ru.urfu.sv.studentvoice.model.domain.dto.report.ReviewReport;
import ru.urfu.sv.studentvoice.model.domain.entity.*;
import ru.urfu.sv.studentvoice.utils.model.ReviewInfo;

import java.util.List;

@Repository
public class ReportQuery extends AbstractQuery {

    private final static QReview review = new QReview("review");
    private final static QLesson lesson = new QLesson("lesson");
    private final static QLessonsReview lessonsReview = new QLessonsReview("lessonsReview");
    private final static QComment comment = new QComment("comment");
    private final static QCourse course = new QCourse("course");
    private final static QInstitute institute = new QInstitute("institute");

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

    public List<ReviewReport> findDataForReportReviews() {

        return query()
                .from(lessonsReview)
                .leftJoin(lesson).on(lessonsReview.lessonId.eq(lesson.id))
                .leftJoin(course).on(lesson.courseId.eq(course.id))
                .leftJoin(institute).on(lesson.instituteId.eq(institute.id))
                .leftJoin(comment).on(comment.lessonReviewId.eq(lessonsReview.id))
                .select(Projections.bean(ReviewReport.class,
                        course.name.as("courseName"),
                        lesson.name.as("lessonName"),
                        lesson.startDateTime.as("startDateTime"),
                        lesson.endDateTime.as("endDateTime"),
                        institute.fullName.as("instituteName"),
                        lessonsReview.studentFullName.as("studentName"),
                        lessonsReview.createTimestamp.as("createReview")
                ))
                .fetch();
    }
}