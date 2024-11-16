package ru.urfu.sv.studentvoice.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.urfu.sv.studentvoice.model.domain.dto.auth.UserInfoDto;
import ru.urfu.sv.studentvoice.model.domain.dto.response.UserInfoResponse;
import ru.urfu.sv.studentvoice.model.domain.entity.Authority;
import ru.urfu.sv.studentvoice.model.domain.entity.User;
import ru.urfu.sv.studentvoice.model.repository.UserRepository;
import ru.urfu.sv.studentvoice.utils.consts.Roles;

@Service
@Slf4j
public class UserService {

    @Autowired
    private PasswordEncoder encoder;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ProfessorService professorService;
    @Autowired
    private AuthorityService authorityService;

    /**
     * Создаем нового пользователя
     *
     * @param userInfoDto Dto по пользователю
     */
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @PreAuthorize("@UserAC.isCreateNewUser(#userInfoDto.username)")
    public void createUser(UserInfoDto userInfoDto) {

        final String username = userInfoDto.getUsername();

        final User user = new User();
        user.setUsername(username);
        user.setPassword(encoder.encode(userInfoDto.getPassword()));
        user.setActive(true);
        user.setName(userInfoDto.getName());
        user.setSurname(userInfoDto.getSurname());
        user.setPatronymic(userInfoDto.getPatronymic());

        /*To Do назначить роль */

        userRepository.save(user);
    }

//    @Transactional
//    public ActionResult updateUser(String username, User newUser) {
//        if (!isUserExists(username)) {
//            return ActionResultFactory.userNotExist(username);
//        }
//
//        UserDetails current = userDetailsManager.loadUserByUsername(username);
//
//        userDetailsManager.updateUser(org.springframework.security.core.userdetails.User
//                .builder()
//                .username(username)
//                .authorities(current.getAuthorities())
//                .passwordEncoder(encoder::encode)
//                .password(newUser.getPassword().isBlank() ? current.getPassword() : newUser.getPassword())
//                .build());
//
//        if (newUser.getAdditionalData() != null && newUser.getAdditionalData().get(Parameters.PROFESSOR_NAME) != null) {
//            ActionResult professorResult = professorService
//                    .updateProfessor(username, newUser.getAdditionalData().get(Parameters.PROFESSOR_NAME));
//            if (!professorResult.isSuccess()) return professorResult;
//        }
//
//        return new ActionResult(true, "Пользователь успешно обновлен");
//    }
//
//    public List<User> findAllUsers() {
//        List<String> usernames = userRepository.findAllUsernames();
//        return usernames.stream().map(username -> User.fromUserDetails(userDetailsManager.loadUserByUsername(username))).toList();
//    }
//
//    public Optional<User> findUserByUsername(String username) {
//        if (!isUserExists(username)) return Optional.empty();
//
//        return Optional.of(User.fromUserDetails(userDetailsManager.loadUserByUsername(username)));
//    }

//    @Transactional
//    public ActionResult deleteUser(String username) {
//        userDetailsManager.deleteUser(username);
//        return new ActionResult(true, "Пользователь %s успешно удален", username);
//    }

    public UserInfoResponse getInfoForUser() {
        final Authority authority = authorityService.findAuthorityForCheck();

        final UserInfoResponse userInfoResponse = new UserInfoResponse();
        userInfoResponse.setUsername(authority.getUsername());
        userInfoResponse.setUserRole(authority.getAuthority());
        return userInfoResponse;
    }
}