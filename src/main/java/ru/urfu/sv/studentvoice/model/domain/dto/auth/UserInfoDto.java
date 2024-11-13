package ru.urfu.sv.studentvoice.model.domain.dto.auth;

import lombok.Getter;

@Getter
public class UserInfoDto {

    private String username;
    private String password;
    private String name;
    private String surname;
    private String patronymic;
}