package ru.urfu.sv.studentvoice.controllers.links.checker;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.urfu.sv.studentvoice.model.query.InstituteQuery;

@Component("InstitutesAC")
public class InstitutesAccessChecker {

    @Autowired
    private InstituteQuery instituteQuery;
    @Autowired
    private RolesAccessChecker rolesAccessChecker;

    public boolean isExistInstitute(String fullNameInstitute) {
        return instituteQuery.isExistInstitute(fullNameInstitute);
    }

    /**
     * Можно ли создавать нового пользователя с таким username
     */
    public boolean isCreateNewInstitute(String fullNameInstitute) {

        if (!rolesAccessChecker.isAdmin()) {
            return false;
        }

        return !isExistInstitute(fullNameInstitute);
    }
}