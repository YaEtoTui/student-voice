package ru.urfu.sv.studentvoice.model.domain.dto.user;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Professor {

    private Long professorId;
    private String professorName;
    private String professorSurname;
    private String professorPatronymic;
}