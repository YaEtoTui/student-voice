package ru.urfu.sv.studentvoice.model.query;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.JPQLQuery;
import org.springframework.stereotype.Repository;
import ru.urfu.sv.studentvoice.model.domain.dto.json.JLesson;
import ru.urfu.sv.studentvoice.model.domain.dto.LessonAndCourseInfo;
import ru.urfu.sv.studentvoice.model.domain.dto.lesson.LessonDetailsDto;
import ru.urfu.sv.studentvoice.model.domain.entity.*;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

@Repository
public class LessonQuery extends AbstractQuery {

    private final static QLesson lesson = new QLesson("lesson");
    private final static QCourse course = new QCourse("course");
    private final static QUserCourse userCourse = new QUserCourse("userCourse");
    private final static QUser user = new QUser("user");
    private final static QInstitute institute = new QInstitute("institute");

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

    public void createDisableTimestamp(Long lessonId, LocalDateTime disableDate) {

        final BooleanExpression exp = lesson.id.eq(lessonId);

        query()
                .update(lesson)
                .set(lesson.disableTimestamp, disableDate)
                .set(lesson.createdQR, true)
                .set(lesson.createTimestamp, LocalDateTime.now())
                .where(exp)
                .execute();
    }

    public LocalDateTime findStartLessonById(Long lessonId) {

        final BooleanExpression exp = lesson.id.eq(lessonId);

        return query()
                .from(lesson)
                .where(exp)
                .select(lesson.startDateTime)
                .fetchFirst();
    }

    public boolean isExistLesson(JLesson jLesson) {

        final BooleanExpression exp = lesson.courseId.eq(jLesson.getCourseId())
                .and(lesson.instituteId.eq(jLesson.getInstituteId()))
                .and(lesson.endDateTime.goe(jLesson.getStartDateTime().toLocalDate().atStartOfDay()))
                .and(lesson.startDateTime.goe(jLesson.getEndDateTime().toLocalDate().minusDays(1).atStartOfDay()))
                .and(lesson.startDateTime.loe(jLesson.getEndDateTime().toLocalDate().atStartOfDay().plusDays(1).minusSeconds(1)));

        final Collection<Lesson> lessons = query()
                .selectFrom(lesson)
                .where(exp)
                .fetch();

        return !lessons.isEmpty();
    }

    public List<LessonAndCourseInfo> findAllLessonInfoByProfessorId(Long professorId) {

        final BooleanExpression exp = user.id.eq(professorId);

        return query()
                .from(lesson)
                .join(course).on(lesson.courseId.eq(course.id))
                .join(userCourse).on(userCourse.courseId.eq(course.id))
                .join(user).on(userCourse.userId.eq(user.id))
                .join(institute).on(lesson.instituteId.eq(institute.id))
                .where(exp)
                .select(
                        Projections.bean(LessonAndCourseInfo.class,
                                lesson.name.as("lessonName"),
                                course.name.as("courseName"),
                                institute.fullName.as("instituteName"),
                                lesson.startDateTime.as("dateStart"),
                                lesson.endDateTime.as("dateEnd"))
                )
                .fetch();
    }

    public List<Lesson> findAllLessonByProfessorId(Long professorId) {

        final BooleanExpression exp = user.id.eq(professorId);

        return query()
                .selectFrom(lesson)
                .join(course).on(lesson.courseId.eq(course.id))
                .join(userCourse).on(userCourse.courseId.eq(course.id))
                .join(user).on(userCourse.userId.eq(user.id))
                .join(institute).on(lesson.instituteId.eq(institute.id))
                .where(exp)
                .fetch();
    }
}