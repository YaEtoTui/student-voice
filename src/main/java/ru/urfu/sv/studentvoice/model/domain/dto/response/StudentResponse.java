package ru.urfu.sv.studentvoice.model.domain.dto.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StudentResponse {

    private String fio;
    private boolean visitedLesson;
}