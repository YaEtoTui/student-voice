package ru.urfu.sv.studentvoice.services.mapper;

import org.springframework.stereotype.Component;
import ru.urfu.sv.studentvoice.model.domain.dto.lesson.LessonByCourse;
import ru.urfu.sv.studentvoice.model.domain.dto.lesson.LessonDetailsDto;
import ru.urfu.sv.studentvoice.model.domain.dto.response.LessonByCourseResponse;
import ru.urfu.sv.studentvoice.model.domain.dto.response.LessonDetailsResponse;
import ru.urfu.sv.studentvoice.model.domain.dto.response.LessonResponse;
import ru.urfu.sv.studentvoice.model.domain.dto.lesson.LessonWithCourse;

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
        lessonResponse.setLessonId(lessonWithCourse.getLessonId());
        lessonResponse.setStatus(lessonWithCourse.getStatus());
        lessonResponse.setCourseId(lessonWithCourse.getCourseId());
        lessonResponse.setCourseName(lessonWithCourse.getCourseName());
        lessonResponse.setDateStart(lessonWithCourse.getDateStart());
        lessonResponse.setDateEnd(lessonWithCourse.getDateEnd());

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
     * Переделываем LessonByCourse в LessonByCourseResponse
     *
     * @param lesson Пара
     */
    public LessonByCourseResponse createLessonByCourseResponse(LessonByCourse lesson) {
        final LessonByCourseResponse lessonResponse = new LessonByCourseResponse();
        lessonResponse.setLessonId(lesson.getLessonId());
        lessonResponse.setStatus(lesson.getStatus());
        lessonResponse.setCourseName(lesson.getCourseName());
        lessonResponse.setDateStart(lesson.getDateStart());
        lessonResponse.setDateEnd(lesson.getDateEnd());

        return lessonResponse;
    }

    /**
     * Переделываем LessonDetailsDto в LessonDetailsResponse
     *
     * @param lesson Пара
     */
    public LessonDetailsResponse createLessonDetailsResponse(LessonDetailsDto lesson) {
        final LessonDetailsResponse lessonResponse = new LessonDetailsResponse();

        lessonResponse.setLessonId(lesson.getLessonId());
        lessonResponse.setCourseName(lesson.getCourseName());
        lessonResponse.setAddress(lesson.getAddress());
        lessonResponse.setDateStart(lesson.getDateStart());
        lessonResponse.setDateEnd(lesson.getDateEnd());
        return lessonResponse;
    }
}