package ru.urfu.sv.studentvoice.model.domain.dto.lesson;

import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
public class LessonWithCourse {

    private String status;
    private String courseName;
    private Instant dateStart;
    private Instant dateEnd;
}