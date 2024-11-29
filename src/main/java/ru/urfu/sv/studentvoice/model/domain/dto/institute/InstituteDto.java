package ru.urfu.sv.studentvoice.model.domain.dto.institute;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class InstituteDto {

    private Long instituteId;
    private String instituteFullName;
    private String instituteShortName;
    private String instituteAddress;
}
