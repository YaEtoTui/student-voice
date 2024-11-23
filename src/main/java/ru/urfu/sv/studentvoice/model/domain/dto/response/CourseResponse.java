package ru.urfu.sv.studentvoice.model.domain.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CourseResponse {

    @JsonProperty("course_id")
    private Long courseId;
    private String name;
    private String address;
}