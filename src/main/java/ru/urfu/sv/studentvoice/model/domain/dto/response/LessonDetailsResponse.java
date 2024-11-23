package ru.urfu.sv.studentvoice.model.domain.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class LessonDetailsResponse {

    @JsonProperty("lesson_id")
    private Long lessonId;

    @JsonProperty("course_name")
    private String courseName;

    private String address;

    @JsonProperty("date_start")
    private LocalDateTime dateStart;

    @JsonProperty("date_end")
    private LocalDateTime dateEnd;
}