package ru.urfu.sv.studentvoice.model.query;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.JPQLQuery;
import org.springframework.stereotype.Repository;
import ru.urfu.sv.studentvoice.model.domain.dto.user.Roles;
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

        final BooleanExpression exp = user.username.eq(username)
                .and(user.active.isTrue());

        return query()
                .selectFrom(user)
                .where(exp)
                .fetchOne();
    }

    /**
     * Проверяем, существует ли пользователь с таким username
     */
    public boolean isExistUser(String username) {

        final BooleanExpression exp = user.username.eq(username)
                .and(user.active.isTrue());;

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

    /**
     * Ищем преподавателя по username
     */
    public User findProfessorByUsername(String username) {

        final BooleanExpression exp = user.username.eq(username)
                .and(user.active.isTrue())
                .and(role.name.eq(Roles.ROLE_PROFESSOR.getName()));

        return query()
                .selectFrom(user)
                .join(userRole).on(userRole.userId.eq(user.id))
                .join(role).on(userRole.roleId.eq(role.id))
                .where(exp)
                .fetchOne();
    }

    /**
     * Создаем связь между пользователем и его ролью
     * @param userId индентификатор пользователя
     * @param roleName Название роли
     */
    public void insertUserRole(Long userId, String roleName) {

        final JPQLQuery<?> query = query()
                .select(Expressions.numberTemplate(Long.class, userId + "L"), role.id)
                .from(role)
                .where(role.name.eq(roleName));

        query()
                .insert(userRole)
                .columns(userRole.userId, userRole.roleId)
                .select(query)
                .execute();
    }
}