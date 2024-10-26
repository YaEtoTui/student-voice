package ru.urfu.sv.studentvoice.model.domain.dto.auth;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserClient {
    private String username;
    private String password;
}