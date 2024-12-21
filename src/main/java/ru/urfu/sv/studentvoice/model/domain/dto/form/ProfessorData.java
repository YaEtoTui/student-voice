package ru.urfu.sv.studentvoice.model.domain.dto.form;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProfessorData {

    @JsonProperty("professor_id")
    private Long professorId;

    @JsonProperty("professor_fio")
    private String professorFio;
}