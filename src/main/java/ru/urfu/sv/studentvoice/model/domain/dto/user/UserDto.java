package ru.urfu.sv.studentvoice.model.domain.dto.user;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserDto {

    private String username;
    private String role;
    private String name;
    private String surname;
    private String patronymic;
}