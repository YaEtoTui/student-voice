package ru.urfu.sv.studentvoice.model.query;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.JPQLQuery;
import org.springframework.stereotype.Repository;
import ru.urfu.sv.studentvoice.model.domain.dto.lesson.LessonDetailsDto;
import ru.urfu.sv.studentvoice.model.domain.entity.QCourse;
import ru.urfu.sv.studentvoice.model.domain.entity.QLesson;

import java.util.List;
import java.util.Objects;

@Repository
public class LessonQuery extends AbstractQuery {

    private final static QLesson lesson = new QLesson("lesson");
    private final static QCourse course = new QCourse("course");

    /**
     * Ищем список пар для преподавателя
     */
    public JPQLQuery<?> findAllLessonsBySearchTextAndProfName(String searchText, String professorName) {

        //To Do связоки нет с преподом
        BooleanExpression exp = null;

        if (Objects.nonNull(searchText)) {
            exp = course.name.likeIgnoreCase("%" + searchText + "%");
        }

        return query()
                .from(lesson)
                .join(course).on(lesson.courseId.eq(course.id))
                .where(exp);
    }

    /**
     * Ищем список пар по предмету
     */
    public JPQLQuery<?> findAllLessonsByCourseId(Long courseId) {

        //To Do связоки нет с преподом
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

    public List<LessonDetailsDto> findScheduleShort(String professorName) {

        final BooleanExpression exp = null;

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
                .fetch();
    }
}