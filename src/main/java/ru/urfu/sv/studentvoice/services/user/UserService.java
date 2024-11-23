package ru.urfu.sv.studentvoice.services.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.urfu.sv.studentvoice.model.domain.dto.user.Roles;
import ru.urfu.sv.studentvoice.model.domain.dto.user.UserDto;
import ru.urfu.sv.studentvoice.model.domain.dto.user.UserInfoDto;
import ru.urfu.sv.studentvoice.model.domain.dto.response.UserInfoResponse;
import ru.urfu.sv.studentvoice.model.domain.entity.User;
import ru.urfu.sv.studentvoice.model.query.UserQuery;
import ru.urfu.sv.studentvoice.model.repository.UserRepository;

@Service
@Slf4j
public class UserService {

    @Autowired
    private PasswordEncoder encoder;
    @Autowired
    private RoleService roleService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserQuery userQuery;

    /**
     * Создаем нового пользователя
     *
     * @param userInfoDto Dto по пользователю
     */
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @PreAuthorize("@UserAC.isCreateNewUser(#userInfoDto.username)")
    public void createUser(UserInfoDto userInfoDto) {

        final String username = userInfoDto.getUsername();
        final Roles role = Roles.valueOf(userInfoDto.getRoleName());

        final User user = new User();
        user.setUsername(username);
        user.setPassword(encoder.encode(userInfoDto.getPassword()));
        user.setActive(true);
        user.setName(userInfoDto.getName());
        user.setSurname(userInfoDto.getSurname());
        user.setPatronymic(userInfoDto.getPatronymic());

        final User userResponse = userRepository.save(user);
        userQuery.insertUserRole(userResponse.getId(), role.getName());
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

//    @Transactional
//    public ActionResult deleteUser(String username) {
//        userDetailsManager.deleteUser(username);
//        return new ActionResult(true, "Пользователь %s успешно удален", username);
//    }

    /**
     * Получаем краткую информацию об пользователе,
     * Нужно фронту для понимания вывода на UI
     */
    public UserInfoResponse getInfoForUser() {
        final UserDto userDto = roleService.findUserDto();

        final UserInfoResponse userInfoResponse = new UserInfoResponse();
        userInfoResponse.setUsername(userDto.getUsername());
        userInfoResponse.setUserRole(userDto.getRole());

        final String fio = String.format("%s %s %s", userDto.getSurname(), userDto.getName(), userDto.getPatronymic());
        userInfoResponse.setFio(fio);
        return userInfoResponse;
    }
}