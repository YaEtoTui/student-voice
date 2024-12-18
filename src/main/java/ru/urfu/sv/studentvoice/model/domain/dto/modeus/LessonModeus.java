package ru.urfu.sv.studentvoice.model.domain.dto.modeus;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class LessonModeus {

    private String name;
    private String status;
    private String courseName;
    private String instituteName;
    private LocalDateTime startDateTime;
    private LocalDateTime endDateTime;
    private String cabinet;
    private String address;
    private Long professorId;
}