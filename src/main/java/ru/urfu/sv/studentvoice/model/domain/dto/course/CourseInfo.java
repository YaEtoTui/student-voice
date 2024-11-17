package ru.urfu.sv.studentvoice.model.domain.dto.course;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CourseInfo {

    @JsonProperty("course_name")
    private String courseName;

    @JsonProperty("institute_id")
    private Long instituteId;

    @JsonProperty("professor_id")
    private Long professorId;
}