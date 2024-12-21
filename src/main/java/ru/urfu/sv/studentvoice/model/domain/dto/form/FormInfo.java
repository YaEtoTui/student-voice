package ru.urfu.sv.studentvoice.model.domain.dto.form;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class FormInfo {

    private Long lessonId;
    private LocalDateTime startDateTime;
    private LocalDateTime endDateTime;
    private String courseName;
    private Long professorId;
    private String professorName;
    private String professorSurname;
    private String professorPatronymic;
}