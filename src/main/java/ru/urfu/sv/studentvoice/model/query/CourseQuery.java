package ru.urfu.sv.studentvoice.model.query;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import org.springframework.stereotype.Repository;
import ru.urfu.sv.studentvoice.model.domain.dto.course.CourseInfo;
import ru.urfu.sv.studentvoice.model.domain.entity.*;
import ru.urfu.sv.studentvoice.model.domain.dto.lesson.LessonWithCourse;

import java.util.Collection;
import java.util.List;

@Repository
public class CourseQuery extends AbstractQuery {

    private final static QCourse course = new QCourse("course");
    private final static QLesson lesson = new QLesson("lesson");
    private final static QUser user = new QUser("user");
    private final static QUserCourse userCourse = new QUserCourse("userCourse");

    /**
     * Ищем список пар для преподавателя
     */
    public List<LessonWithCourse> findAllLessonsByProfessorUsername(String username) {

        //To Do связоки нет с преподом
        final BooleanExpression exp = null;

        return query()
                .from(course)
                .join(lesson).on(lesson.courseId.eq(course.id))
                .where(exp)
                .select(Projections.bean(LessonWithCourse.class,
                        /* Статус добавить */
                        course.name.as("courseName"),
                        lesson.startDateTime.as("dateStart"),
                        lesson.endDateTime.as("dateEnd")
                ))
                .fetch();
    }

    /**
     * Проверяем, существует ли институт с courseInfo
     */
    public boolean isExistCourse(CourseInfo courseInfo) {

        /* Тут у преподавателя смотрим */
        final BooleanExpression exp = course.instituteId.eq(courseInfo.getInstituteId())
                .and(course.name.eq(courseInfo.getCourseName()))
                .and(user.id.eq(courseInfo.getProfessorId()));

        final Collection<Course> courses = query()
                .from(course)
                .join(userCourse).on(userCourse.courseId.eq(course.id))
                .join(user).on(userCourse.userId.eq(user.id))
                .where(exp)
                .select(course)
                .fetch();

        return !courses.isEmpty();
    }

    /**
     * Создаем связь между преподавателем и курсом
     *
     * @param userId   индентификатор пользователя
     * @param courseId индентификатор курса
     */
    public void insertUserCourse(Long userId, Long courseId) {

        query()
                .insert(userCourse)
                .columns(userCourse.userId, userCourse.courseId)
                .values(userId, courseId)
                .execute();
    }
}