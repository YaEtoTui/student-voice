package ru.urfu.sv.studentvoice.controllers.links.checker;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.urfu.sv.studentvoice.model.domain.dto.user.UsernameAndRole;
import ru.urfu.sv.studentvoice.services.RoleService;

import static ru.urfu.sv.studentvoice.model.domain.dto.user.Roles.ROLE_ADMIN;
import static ru.urfu.sv.studentvoice.model.domain.dto.user.Roles.ROLE_PROFESSOR;

@Component("RolesAC")
public class RolesAccessChecker {

    @Autowired
    private RoleService roleService;

    public boolean isAdmin() {
        final UsernameAndRole usernameAndRole = roleService.findRoleForCheck();
        return usernameAndRole.getRole().equals(ROLE_ADMIN.getName());
    }

    public boolean isProfessor() {
        final UsernameAndRole usernameAndRole = roleService.findRoleForCheck();
        return usernameAndRole.getRole().equals(ROLE_PROFESSOR.getName());
    }

    public boolean isAdminOrProfessor() {
        final UsernameAndRole usernameAndRole = roleService.findRoleForCheck();
        return usernameAndRole.getRole().equals(ROLE_ADMIN.getName()) || usernameAndRole.getRole().equals(ROLE_PROFESSOR.getName());
    }
}