package ru.urfu.sv.studentvoice.services.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.urfu.sv.studentvoice.model.domain.dto.user.UserDto;
import ru.urfu.sv.studentvoice.model.domain.dto.user.UsernameAndRole;
import ru.urfu.sv.studentvoice.model.query.UserQuery;
import ru.urfu.sv.studentvoice.services.jwt.JwtUserDetailsService;

@Service
public class RoleService {

    @Autowired
    private JwtUserDetailsService jwtUserDetailsService;
    @Autowired
    private UserQuery userQuery;

    /**
     * Ищем Роль пользователя для проверки
     */
    public UsernameAndRole findRoleForCheck() {

        final String username = jwtUserDetailsService.findUsername();
        return userQuery.findUsernameAndRoleByUsername(username);
    }

    public UserDto findUserDto() {
        final String username = jwtUserDetailsService.findUsername();
        return userQuery.findUserDto(username);
    }
}