package ru.urfu.sv.studentvoice.model.domain.dto.lesson;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class LessonDetailsDto {

    private Long lessonId;
    private String courseName;
    private String address;
    private LocalDateTime dateStart;
    private LocalDateTime dateEnd;
}