package ru.urfu.sv.studentvoice.services.mapper;

import org.springframework.stereotype.Component;
import ru.urfu.sv.studentvoice.model.domain.dto.response.LessonResponse;
import ru.urfu.sv.studentvoice.model.domain.dto.lesson.LessonWithCourse;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Mapper по созданию PairResponse
 *
 * @author Egor Sazhin
 * @since 07.11.2024
 */
@Component
public class LessonMapper {

    /**
     * Переделываем List<LessonWithCourse> в List<PairResponse>
     *
     * @param lessonWithCourseList Список пар
     */
    public List<LessonResponse> createPairResponseListFromLessonWithCourseList(Collection<LessonWithCourse> lessonWithCourseList) {
        return lessonWithCourseList.stream()
                .map(this::createPairResponse)
                .collect(Collectors.toList());
    }

    /**
     * Переделываем LessonWithCourse в PairResponse
     *
     * @param lessonWithCourse Пара
     */
    public LessonResponse createPairResponse(LessonWithCourse lessonWithCourse) {
        final LessonResponse lessonResponse = new LessonResponse();
        lessonResponse.setStatus(lessonWithCourse.getStatus());
        lessonResponse.setCourseName(lessonWithCourse.getCourseName());
        final LocalDateTime startDateTime = LocalDateTime.ofInstant(lessonWithCourse.getDateStart(), ZoneId.systemDefault());
        lessonResponse.setDateStart(startDateTime);
        final LocalDateTime endDateTime = LocalDateTime.ofInstant(lessonWithCourse.getDateEnd(), ZoneId.systemDefault());
        lessonResponse.setDateEnd(endDateTime);

        return lessonResponse;
    }
}