package ru.urfu.sv.studentvoice.model.domain.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class InstituteResponse {

    @JsonProperty("institute_id")
    private Long instituteId;

    @JsonProperty("institute_full_name")
    private String instituteFullName;
}