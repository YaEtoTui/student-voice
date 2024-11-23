package ru.urfu.sv.studentvoice.model.domain.dto.course;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CourseDto {

    private Long courseId;
    private String name;
    private String address;
}