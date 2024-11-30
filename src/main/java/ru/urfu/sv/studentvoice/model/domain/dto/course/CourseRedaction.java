package ru.urfu.sv.studentvoice.model.domain.dto.course;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CourseRedaction {

    @JsonProperty("course_name")
    private String courseName;

    private String address;

    @JsonProperty("institute_id")
    private Long instituteId;

    @JsonProperty("professor_id")
    private Long professorId;
}