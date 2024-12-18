package ru.urfu.sv.studentvoice.model.domain.dto.course;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class CourseInfo {

    @JsonProperty("course_name")
    private String courseName;

    private String address;

    @JsonProperty("institute_id")
    private Long instituteId;

    @JsonProperty("professor_ids")
    private List<Long> professorIds;

    private Boolean isConstantlyLink;
}