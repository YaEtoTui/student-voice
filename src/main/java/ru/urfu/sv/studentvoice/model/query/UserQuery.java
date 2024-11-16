package ru.urfu.sv.studentvoice.model.query;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.sql.SQLExpressions;
import com.querydsl.sql.SQLQuery;
import jakarta.persistence.criteria.Subquery;
import org.springframework.stereotype.Repository;
import ru.urfu.sv.studentvoice.model.domain.dto.user.UsernameAndRole;
import ru.urfu.sv.studentvoice.model.domain.entity.QRole;
import ru.urfu.sv.studentvoice.model.domain.entity.QUser;
import ru.urfu.sv.studentvoice.model.domain.entity.QUserRole;
import ru.urfu.sv.studentvoice.model.domain.entity.User;

import java.util.Collection;

@Repository
public class UserQuery extends AbstractQuery {

    private final static QUser user = new QUser("user");
    private final static QUserRole userRole = new QUserRole("userRole");
    private final static QRole role = new QRole("role");

    public User findUserByUsername(String username) {

        final BooleanExpression exp = user.username.eq(username);

        return query()
                .selectFrom(user)
                .where(exp)
                .fetchOne();
    }

    /**
     * Проверяем, существует ли пользователь с таким username
     */
    public boolean isExistUser(String username) {

        final BooleanExpression exp = user.username.eq(username);

        final Collection<User> users = query()
                .selectFrom(user)
                .where(exp)
                .fetch();

        return !users.isEmpty();
    }

    public UsernameAndRole findUsernameAndRoleByUsername(String username) {

        final BooleanExpression exp = user.username.eq(username);

        return query()
                .from(user)
                .join(userRole).on(userRole.userId.eq(user.id))
                .join(role).on(userRole.roleId.eq(role.id))
                .where(exp)
                .select(Projections.bean(UsernameAndRole.class,
                        user.username.as("username"),
                        role.name.as("role")
                ))
                .fetchFirst();
    }

    public void insertUserRole(String username, String roleName) {

        //To Do

//        final SQLQuery<?> query = SQLExpressions.select(
//                user.id,
//                role.id
//        )
//
//        query()
//                .insert(userRole)
//                .columns(
//                        userRole.userId,
//                        userRole.roleId
//                )
//                .select(query)
    }
}