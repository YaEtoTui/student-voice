package ru.urfu.sv.studentvoice.model.query;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.JPQLQuery;
import org.springframework.stereotype.Repository;
import ru.urfu.sv.studentvoice.model.domain.dto.course.CourseInfo;
import ru.urfu.sv.studentvoice.model.domain.dto.response.CourseResponse;
import ru.urfu.sv.studentvoice.model.domain.entity.*;

import java.util.Collection;
import java.util.List;
import java.util.Objects;

@Repository
public class CourseQuery extends AbstractQuery {

    private final static QCourse course = new QCourse("course");
    private final static QInstitute institute = new QInstitute("institute");
    private final static QUser user = new QUser("user");
    private final static QUserCourse userCourse = new QUserCourse("userCourse");

    /**
     * Проверяем, существует ли институт с courseInfo
     */
    public boolean isExistCourse(CourseInfo courseInfo) {

        /* Тут у преподавателя смотрим */
        final BooleanExpression exp = course.instituteId.eq(courseInfo.getInstituteId())
                .and(course.name.eq(courseInfo.getCourseName()))
                .and(user.id.in(courseInfo.getProfessorIds()));

        final Collection<Course> courses = query()
                .from(course)
                .join(userCourse).on(userCourse.courseId.eq(course.id))
                .join(user).on(userCourse.userId.eq(user.id))
                .where(exp)
                .select(course)
                .fetch();

        return !courses.isEmpty();
    }

    public boolean isExistCourseByProfId(Long professorId, String courseName) {

        /* Тут у преподавателя смотрим */
        final BooleanExpression exp = course.name.eq(courseName)
                .and(user.id.eq(professorId));

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

    /**
     * Ищем список предметов для преподавателя
     */
    public JPQLQuery<?> findAllCourseBySearchTextAndProfName(String searchText, String username) {

        BooleanExpression exp = user.username.eq(username);

        if (Objects.nonNull(searchText)) {
            exp = exp.and(course.name.likeIgnoreCase("%" + searchText + "%"));
        }

        return query()
                .from(course)
                .join(institute).on(course.instituteId.eq(institute.id))
                .join(userCourse).on(userCourse.courseId.eq(course.id))
                .join(user).on(userCourse.userId.eq(user.id))
                .where(exp);
    }

    public CourseResponse findCourseDetailsById(Long courseId) {

        final BooleanExpression exp = course.id.eq(courseId);

        return query()
                .from(course)
                .where(exp)
                .select(Projections.bean(CourseResponse.class,
                        course.id.as("courseId"),
                        course.name.as("name"),
                        course.address.as("address")
                ))
                .fetchFirst();
    }

    public void updateCourse(Long courseId, CourseInfo courseInfo) {

        final BooleanExpression exp = course.id.eq(courseId);

        query()
                .update(course)
                .set(course.name, courseInfo.getCourseName())
                .set(course.address, courseInfo.getAddress())
                .set(course.instituteId, courseInfo.getInstituteId())
                .where(exp)
                .execute();
    }

    public void deleteProfessors(Long courseId, Collection<Long> professorIds) {

        BooleanExpression exp = userCourse.courseId.eq(courseId);
        if (Objects.nonNull(professorIds)) {
            exp = exp.and(userCourse.userId.in(professorIds));
        }

        query().delete(userCourse)
                .where(exp)
                .execute();
    }

    public void deleteCourse(Long courseId) {

        BooleanExpression exp = course.id.eq(courseId);

        query().delete(course)
                .where(exp)
                .execute();
    }

    public List<Long> findProfessorsByCourseId(Long courseId) {

        final BooleanExpression exp = userCourse.courseId.eq(courseId);

        return query()
                .from(userCourse)
                .select(userCourse.userId)
                .where(exp)
                .fetch();
    }
}