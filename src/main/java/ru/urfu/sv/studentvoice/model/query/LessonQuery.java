package ru.urfu.sv.studentvoice.model.query;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import org.springframework.stereotype.Repository;
import ru.urfu.sv.studentvoice.model.domain.entity.QCourse;
import ru.urfu.sv.studentvoice.model.domain.entity.QLesson;
import ru.urfu.sv.studentvoice.model.domain.dto.lesson.LessonWithCourse;

import java.util.List;

@Repository
public class LessonQuery extends AbstractQuery {

    private final static QLesson lesson = new QLesson("lesson");
    private final static QCourse course = new QCourse("course");

    /**
     * Ищем список пар для преподавателя
     */
    public List<LessonWithCourse> findAllLessonsByProfessorUsername(String username) {

        //To Do связоки нет с преподом
        final BooleanExpression exp = null;

        return query()
                .from(lesson)
                .join(course).on(lesson.courseId.eq(course.id))
                .where(exp)
                .select(Projections.bean(LessonWithCourse.class,
                        /* Статус добавить */
                        course.name.as("courseName"),
                        lesson.startDateTime.as("dateStart"),
                        lesson.endDateTime.as("dateEnd")
                ))
                .fetch();
    }
}