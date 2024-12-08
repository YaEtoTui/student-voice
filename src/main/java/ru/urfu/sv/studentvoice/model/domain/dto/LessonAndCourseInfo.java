package ru.urfu.sv.studentvoice.model.domain.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class LessonAndCourseInfo {

    private String lessonName;
    private String courseName;
    private String instituteName;
    private LocalDateTime dateStart;
    private LocalDateTime dateEnd;
}