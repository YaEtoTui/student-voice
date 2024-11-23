package ru.urfu.sv.studentvoice.services.mapper;

import org.springframework.stereotype.Component;
import ru.urfu.sv.studentvoice.model.domain.dto.lesson.LessonByCourse;
import ru.urfu.sv.studentvoice.model.domain.dto.response.LessonByCourseResponse;
import ru.urfu.sv.studentvoice.model.domain.dto.response.LessonResponse;
import ru.urfu.sv.studentvoice.model.domain.dto.lesson.LessonWithCourse;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class LessonMapper {

    /**
     * Переделываем List<LessonWithCourse> в List<LessonResponse>
     *
     * @param lessonWithCourseList Список пар
     */
    public List<LessonResponse> createLessonResponseListFromLessonWithCourseList(Collection<LessonWithCourse> lessonWithCourseList) {
        return lessonWithCourseList.stream()
                .map(this::createLessonResponse)
                .collect(Collectors.toList());
    }

    /**
     * Переделываем LessonWithCourse в LessonResponse
     *
     * @param lessonWithCourse Пара
     */
    public LessonResponse createLessonResponse(LessonWithCourse lessonWithCourse) {
        final LessonResponse lessonResponse = new LessonResponse();
        lessonResponse.setStatus(lessonWithCourse.getStatus());
        lessonResponse.setCourseName(lessonWithCourse.getCourseName());
        final LocalDateTime startDateTime = LocalDateTime.ofInstant(lessonWithCourse.getDateStart(), ZoneId.systemDefault());
        lessonResponse.setDateStart(startDateTime);
        final LocalDateTime endDateTime = LocalDateTime.ofInstant(lessonWithCourse.getDateEnd(), ZoneId.systemDefault());
        lessonResponse.setDateEnd(endDateTime);

        return lessonResponse;
    }

    /**
     * Переделываем List<LessonByCourse> в List<LessonResponse>
     *
     * @param lessonByCourseList Список пар
     */
    public List<LessonByCourseResponse> createLessonResponseListFromLessonByCourseList(Collection<LessonByCourse> lessonByCourseList) {
        return lessonByCourseList.stream()
                .map(this::createLessonByCourseResponse)
                .collect(Collectors.toList());
    }

    /**
     * Переделываем LessonWithCourse в LessonByCourseResponse
     *
     * @param lesson Пара
     */
    public LessonByCourseResponse createLessonByCourseResponse(LessonByCourse lesson) {
        final LessonByCourseResponse lessonResponse = new LessonByCourseResponse();
        lessonResponse.setStatus(lesson.getStatus());
        lessonResponse.setCourseName(lesson.getCourseName());
        final LocalDateTime startDateTime = LocalDateTime.ofInstant(lesson.getDateStart(), ZoneId.systemDefault());
        lessonResponse.setDateStart(startDateTime);
        final LocalDateTime endDateTime = LocalDateTime.ofInstant(lesson.getDateEnd(), ZoneId.systemDefault());
        lessonResponse.setDateEnd(endDateTime);

        return lessonResponse;
    }
}