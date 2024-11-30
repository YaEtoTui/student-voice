package ru.urfu.sv.studentvoice.model.query;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.JPQLQuery;
import org.springframework.stereotype.Repository;
import ru.urfu.sv.studentvoice.model.domain.dto.course.CourseInfo;
import ru.urfu.sv.studentvoice.model.domain.dto.course.CourseRedaction;
import ru.urfu.sv.studentvoice.model.domain.dto.response.CourseResponse;
import ru.urfu.sv.studentvoice.model.domain.entity.*;

import java.util.Collection;
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

    public void updateCourse(Long courseId, CourseRedaction courseRedaction) {

        final BooleanExpression exp = course.id.eq(courseId);
        /*Todo доделать */
        query()
                .update(course)
//                .set(course.name, courseRedaction.getCourseName())
//                .set(course.address, courseRedaction.getAddress())
//                .set(course.instituteId, courseRedaction.getInstituteId())
//                .set(course., courseRedaction.getInstituteId())
                .where(exp)
                .execute();
    }
}