package ru.urfu.sv.studentvoice.model.domain.dto.user;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
public class UserInfoDto {

    private String username;
    private String password;
    private String name;
    private String surname;
    private String patronymic;

    @JsonProperty("role_name")
    private String roleName;
}