package ru.urfu.sv.studentvoice.model.domain.dto.institute;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class InstituteInfo {

    @JsonProperty("institute_full_name")
    private String instituteFullName;

    @JsonProperty("institute_short_name")
    private String instituteShortName;

    @JsonProperty("institute_address")
    private String instituteAddress;
}