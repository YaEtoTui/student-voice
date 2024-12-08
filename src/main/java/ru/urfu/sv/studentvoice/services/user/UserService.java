package ru.urfu.sv.studentvoice.services.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.urfu.sv.studentvoice.model.domain.dto.response.ProfessorResponse;
import ru.urfu.sv.studentvoice.model.domain.dto.user.Professor;
import ru.urfu.sv.studentvoice.model.domain.dto.user.Roles;
import ru.urfu.sv.studentvoice.model.domain.dto.user.UserDto;
import ru.urfu.sv.studentvoice.model.domain.dto.user.UserInfoDto;
import ru.urfu.sv.studentvoice.model.domain.dto.response.UserInfoResponse;
import ru.urfu.sv.studentvoice.model.domain.entity.User;
import ru.urfu.sv.studentvoice.model.query.UserQuery;
import ru.urfu.sv.studentvoice.model.repository.UserRepository;
import ru.urfu.sv.studentvoice.services.mapper.ProfessorMapper;

import java.util.List;

@Slf4j
@Service
public class UserService {

    @Autowired
    private PasswordEncoder encoder;
    @Autowired
    private RoleService roleService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserQuery userQuery;
    @Autowired
    private ProfessorMapper professorMapper;

    /**
     * Создаем нового пользователя
     *
     * @param userInfoDto Dto по пользователю
     */
    @PreAuthorize("@UserAC.isCreateNewUser(#userInfoDto.username)")
    @Transactional(propagation = Propagation.REQUIRES_NEW)
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

    /**
     * Получаем краткую информацию об пользователе,
     * Нужно фронту для понимания вывода на UI
     */
    @Transactional(readOnly = true)
    public UserInfoResponse getInfoForUser() {
        final UserDto userDto = roleService.findUserDto();

        final UserInfoResponse userInfoResponse = new UserInfoResponse();
        userInfoResponse.setUsername(userDto.getUsername());
        userInfoResponse.setUserRole(userDto.getRole());

        final String fio = String.format("%s %s %s", userDto.getSurname(), userDto.getName(), userDto.getPatronymic());
        userInfoResponse.setFio(fio);
        return userInfoResponse;
    }

    @PreAuthorize("@RolesAC.isAdminOrProfessor()")
    @Transactional(readOnly = true)
    public List<ProfessorResponse> findProfessorList() {
        final List<Professor> professorList = userQuery.findProfessorList();
        return professorMapper.createListProfessorResponse(professorList);
    }

    //ToDO для Админа доработать
//    @PreAuthorize("@RolesAC.isAdmin()")
//    @Transactional
//    public void createUsersFromFile(MultipartFile file) throws IOException {
//
//        final InputStream inputStream = file.getInputStream();
//
//        final List<String> userNameList = new ArrayList<>();
//        try (final BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
//            String line;
//            while ((line = reader.readLine()) != null) {
//                if (line.split(" ").length != 3) {
//                    throw new InFileException(String.format("В каждой строке должны быть ФИО. Ошибка в строке %s", line));
//                }
//                userNameList.add(line);
//            }
//        } catch (IOException e) {
//            throw new InFileException(String.format("Произошла ошибка при создании пользователей - %s", e.getMessage()));
//        }
//
//        final List<User> createdUserList = new ArrayList<>();
//        final Translator translator = new Translator(Schemas.GOST_52535);
//        for (String fullName : userNameList) {
//            final String[] names = fullName.split(" ");
//            final String login = translator.translate(names[0])
//                    .concat(translator.translate(names[1]).substring(0, 1))
//                    .concat(translator.translate(names[2]).substring(0, 1))
//                    .toLowerCase();
//
//            final String password = PasswordGenerator.generateRandom(10);
//            createdUsers.add(new CreatedUser(fullName, login, password));
//        }
//
//        for (CreatedUser user : createdUsers) {
//            User userInfo = User.builder()
//                    .username(user.getUsername())
//                    .password(user.getPassword())
//                    .role(Roles.PROFESSOR)
//                    .additionalData(Map.of(Parameters.PROFESSOR_NAME, user.getFullName()))
//                    .build();
//            ActionResult result = userService.createUser(userInfo);
//            if (!result.isSuccess()) {
//                return result;
//            }
//            result = professorService.createProfessor(user.getUsername(), user.getFullName());
//            if (!result.isSuccess()) {
//                return result;
//            }
//        }
//
//        return new ActionResult(true, CsvFormatter.toCsv(createdUsers, "fullName;username;password\n"));
//    }
}