package ru.urfu.sv.studentvoice.services.mapper;

import org.springframework.stereotype.Component;
import ru.urfu.sv.studentvoice.model.domain.dto.response.PairResponse;
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
public class PairMapper {

    /**
     * Переделываем List<LessonWithCourse> в List<PairResponse>
     *
     * @param lessonWithCourseList Список пар
     */
    public List<PairResponse> createPairResponseListFromLessonWithCourseList(Collection<LessonWithCourse> lessonWithCourseList) {
        return lessonWithCourseList.stream()
                .map(this::createPairResponse)
                .collect(Collectors.toList());
    }

    /**
     * Переделываем LessonWithCourse в PairResponse
     *
     * @param lessonWithCourse Пара
     */
    public PairResponse createPairResponse(LessonWithCourse lessonWithCourse) {
        final PairResponse pairResponse = new PairResponse();
        pairResponse.setStatus(lessonWithCourse.getStatus());
        pairResponse.setCourseName(lessonWithCourse.getCourseName());
        final LocalDateTime startDateTime = LocalDateTime.ofInstant(lessonWithCourse.getDateStart(), ZoneId.systemDefault());
        pairResponse.setDateStart(startDateTime);
        final LocalDateTime endDateTime = LocalDateTime.ofInstant(lessonWithCourse.getDateEnd(), ZoneId.systemDefault());
        pairResponse.setDateEnd(endDateTime);

        return pairResponse;
    }
}