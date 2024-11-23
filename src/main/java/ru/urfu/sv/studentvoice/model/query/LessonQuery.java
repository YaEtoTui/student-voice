package ru.urfu.sv.studentvoice.model.query;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.JPQLQuery;
import org.springframework.stereotype.Repository;
import ru.urfu.sv.studentvoice.model.domain.entity.QCourse;
import ru.urfu.sv.studentvoice.model.domain.entity.QLesson;

@Repository
public class LessonQuery extends AbstractQuery {

    private final static QLesson lesson = new QLesson("lesson");
    private final static QCourse course = new QCourse("course");

    /**
     * Ищем список пар для преподавателя
     */
    public JPQLQuery<?> findAllLessonsByProfessorUsername(String username) {

        //To Do связоки нет с преподом
        final BooleanExpression exp = null;

        return query()
                .from(lesson)
                .join(course).on(lesson.courseId.eq(course.id))
                .where(exp);
    }

    /**
     * Ищем список пар для преподавателя по предмету
     */
    public JPQLQuery<?> findAllLessonsByProfNameAndCourseId(String username, Long courseId) {

        //To Do связоки нет с преподом
        final BooleanExpression exp = course.id.eq(courseId);

        return query()
                .from(lesson)
                .join(course).on(lesson.courseId.eq(course.id))
                .where(exp);
    }
}