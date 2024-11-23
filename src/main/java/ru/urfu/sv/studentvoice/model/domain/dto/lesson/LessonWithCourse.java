package ru.urfu.sv.studentvoice.model.domain.dto.lesson;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class LessonWithCourse {

    private String status;
    private Long courseId;
    private String courseName;
    private LocalDateTime dateStart;
    private LocalDateTime dateEnd;
}