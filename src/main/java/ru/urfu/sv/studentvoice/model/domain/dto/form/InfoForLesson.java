package ru.urfu.sv.studentvoice.model.domain.dto.form;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class InfoForLesson {

    private Long lessonId;
    private LocalDateTime startDateTime;
    private LocalDateTime endDateTime;
    private String courseName;
}