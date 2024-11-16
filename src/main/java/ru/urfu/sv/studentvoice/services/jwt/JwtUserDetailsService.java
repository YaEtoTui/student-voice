package ru.urfu.sv.studentvoice.services.jwt;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.urfu.sv.studentvoice.model.domain.entity.User;
import ru.urfu.sv.studentvoice.model.query.UserQuery;

import java.util.ArrayList;

import static java.util.Objects.isNull;

/**
 * Сервис для работы с user
 *
 * @author Sazhin Egor
 * @since 20.10.2024
 */
@Service
@Transactional
public class JwtUserDetailsService implements UserDetailsService {

    @Autowired
    private UserQuery userQuery;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        final User user = userQuery.findUserByUsername(username);
        if (isNull(user)) {
            throw new UsernameNotFoundException("User not found with username: " + username);
        }

        return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), new ArrayList<>());
    }

    /**
     * Ищем username пользователя, если он уже существует
     */
    public String findUsername() {

        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (!authentication.isAuthenticated()) {
            throw new AuthenticationCredentialsNotFoundException("Current user not authenticated");
        }

        String username;
        if (authentication.getPrincipal() instanceof org.springframework.security.core.userdetails.User) {
            username = ((org.springframework.security.core.userdetails.User) authentication.getPrincipal()).getUsername();
        } else {
            username = (String) authentication.getPrincipal();
        }

        if (isNull(username)) {
            return null;
        }

        return username;
    }
}