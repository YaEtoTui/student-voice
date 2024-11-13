package ru.urfu.sv.studentvoice.model.query;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.JPQLQueryFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import ru.urfu.sv.studentvoice.model.domain.entity.QUser;
import ru.urfu.sv.studentvoice.model.domain.entity.User;

import java.util.Collection;

import static java.util.Objects.nonNull;

@Repository
public class UserQuery {

    @Autowired
    private JPQLQueryFactory query;

    private final static QUser user = new QUser("user");

    public User findUserByUsername(String username) {

        final BooleanExpression exp = user.username.eq(username);

        return query
                .selectFrom(user)
                .where(exp)
                .fetchOne();
    }

    /**
     * Проверяем, существует ли пользователь с таким username
     */
    public boolean isExistUser(String username) {

        final BooleanExpression exp = user.username.eq(username);

        final Collection<User> users = query
                .selectFrom(user)
                .where(exp)
                .fetch();

        return !users.isEmpty();
    }
}