package ru.urfu.sv.studentvoice.model.query;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.JPQLQuery;
import org.springframework.stereotype.Repository;
import ru.urfu.sv.studentvoice.model.domain.dto.lesson.LessonDetailsDto;
import ru.urfu.sv.studentvoice.model.domain.entity.QCourse;
import ru.urfu.sv.studentvoice.model.domain.entity.QLesson;
import ru.urfu.sv.studentvoice.model.domain.entity.QUser;
import ru.urfu.sv.studentvoice.model.domain.entity.QUserCourse;

import java.util.List;
import java.util.Objects;

@Repository
public class LessonQuery extends AbstractQuery {

    private final static QLesson lesson = new QLesson("lesson");
    private final static QCourse course = new QCourse("course");
    private final static QUserCourse userCourse = new QUserCourse("userCourse");
    private final static QUser user = new QUser("user");

    /**
     * Ищем список пар для преподавателя
     */
    public JPQLQuery<?> findAllLessonsBySearchTextAndProfName(String searchText, String professorName) {

        BooleanExpression exp = user.username.eq(professorName);

        if (Objects.nonNull(searchText)) {
            exp = exp.and(course.name.likeIgnoreCase("%" + searchText + "%"));
        }

        return query()
                .from(lesson)
                .join(course).on(lesson.courseId.eq(course.id))
                .join(userCourse).on(userCourse.courseId.eq(course.id))
                .join(user).on(userCourse.userId.eq(user.id))
                .where(exp);
    }

    /**
     * Ищем список пар по предмету
     */
    public JPQLQuery<?> findAllLessonsByCourseId(Long courseId) {

        final BooleanExpression exp = course.id.eq(courseId);

        return query()
                .from(lesson)
                .join(course).on(lesson.courseId.eq(course.id))
                .where(exp);
    }

    public LessonDetailsDto findLessonDetailsById(Long lessonId) {

        final BooleanExpression exp = lesson.id.eq(lessonId);

        return query()
                .from(lesson)
                .join(course).on(lesson.courseId.eq(course.id))
                .where(exp)
                .select(
                        Projections.bean(LessonDetailsDto.class,
                                lesson.id.as("lessonId"),
                                course.name.as("courseName"),
                                lesson.address.as("address"),
                                lesson.startDateTime.as("dateStart"),
                                lesson.endDateTime.as("dateEnd"))
                )
                .fetchFirst();
    }

    /**
     * Ищем расписание по name преподавателю
     */
    public List<LessonDetailsDto> findSchedule(String professorName) {

        final BooleanExpression exp = user.username.eq(professorName);

        return query()
                .from(lesson)
                .join(course).on(lesson.courseId.eq(course.id))
                .join(userCourse).on(userCourse.courseId.eq(course.id))
                .join(user).on(userCourse.userId.eq(user.id))
                .where(exp)
                .select(
                        Projections.bean(LessonDetailsDto.class,
                                lesson.id.as("lessonId"),
                                course.name.as("courseName"),
                                lesson.address.as("address"),
                                lesson.startDateTime.as("dateStart"),
                                lesson.endDateTime.as("dateEnd"))
                )
                .fetch();
    }
}