package ru.urfu.sv.studentvoice.controllers.links.checker;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.urfu.sv.studentvoice.model.query.UserQuery;

@Component("UserAC")
public class UserAccessChecker {

    @Autowired
    private UserQuery userQuery;

    public boolean isExistUser(String username) {
        return userQuery.isExistUser(username);
    }

    /**
     * Можно ли создавать нового пользователя с таким username
     */
    public boolean isCreateNewUser(String username) {
        return !isExistUser(username);
    }
}
