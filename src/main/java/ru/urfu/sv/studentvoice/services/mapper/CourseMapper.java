package ru.urfu.sv.studentvoice.services.mapper;

import org.springframework.stereotype.Component;
import ru.urfu.sv.studentvoice.model.domain.dto.course.CourseDto;
import ru.urfu.sv.studentvoice.model.domain.dto.response.CourseResponse;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class CourseMapper {

    /**
     * Переделываем List<CourseDto> в List<CourseResponse>
     *
     * @param courseDtos Список предметов
     */
    public List<CourseResponse> createCourseResponseList(Collection<CourseDto> courseDtos) {
        return courseDtos.stream()
                .map(this::createCourseResponse)
                .collect(Collectors.toList());
    }

    /**
     * Переделываем CourseDto в CourseResponse
     *
     * @param courseDto Предмет
     */
    public CourseResponse createCourseResponse(CourseDto courseDto) {
        final CourseResponse courseResponse = new CourseResponse();
        courseResponse.setCourseId(courseDto.getCourseId());
        courseResponse.setName(courseDto.getName());
        courseResponse.setAddress(courseDto.getAddress());

        return courseResponse;
    }
}