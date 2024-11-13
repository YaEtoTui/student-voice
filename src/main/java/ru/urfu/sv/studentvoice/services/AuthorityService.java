package ru.urfu.sv.studentvoice.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.urfu.sv.studentvoice.model.domain.entity.Authority;
import ru.urfu.sv.studentvoice.model.repository.AuthorityRepository;
import ru.urfu.sv.studentvoice.services.jwt.JwtUserDetailsService;

@Service
public class AuthorityService {

//    @Autowired
//    private AuthorityRepository authorityRepository;
    @Autowired
    private JwtUserDetailsService jwtUserDetailsService;

    /**
     * Ищем Роль пользователя для проверки
     */
    public Authority findAuthorityForCheck() {

        final String username = jwtUserDetailsService.findUsername();
//        return authorityRepository.findAuthorityByUsername(username);
        return new Authority();
    }
}