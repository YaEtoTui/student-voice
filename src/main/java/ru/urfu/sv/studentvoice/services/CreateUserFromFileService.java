package ru.urfu.sv.studentvoice.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.homyakin.iuliia.Schemas;
import ru.homyakin.iuliia.Translator;
import ru.urfu.sv.studentvoice.model.domain.entity.UserInfo;
import ru.urfu.sv.studentvoice.utils.PasswordGenerator;
import ru.urfu.sv.studentvoice.utils.consts.Parameters;
import ru.urfu.sv.studentvoice.utils.consts.Roles;
import ru.urfu.sv.studentvoice.utils.formatters.CsvFormatter;
import ru.urfu.sv.studentvoice.utils.model.CreatedUser;
import ru.urfu.sv.studentvoice.utils.result.ActionResult;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class CreateUserFromFileService {
    private final UserService userService;
    private final ProfessorService professorService;

    @Transactional
    public ActionResult createUsersFromFile(InputStream inputStream) {
        List<String> usersNames = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.split(" ").length != 3) {
                    return new ActionResult(false, "В каждой строке должны быть ФИО. Ошибка в строке %s", line);
                }
                usersNames.add(line);
            }
        } catch (IOException e) {
            return new ActionResult(false, "Произошла ошибка при создании пользователей - %s", e.getMessage());
        }

        List<CreatedUser> createdUsers = new ArrayList<>();
        Translator translator = new Translator(Schemas.GOST_52535);
        for (String fullName : usersNames) {
            String[] names = fullName.split(" ");
            String login = translator.translate(names[0])
                    .concat(translator.translate(names[1]).substring(0, 1))
                    .concat(translator.translate(names[2]).substring(0, 1))
                    .toLowerCase();
            String password = PasswordGenerator.generateRandom(10);
            createdUsers.add(new CreatedUser(fullName, login, password));
        }

        for (CreatedUser user : createdUsers) {
            UserInfo userInfo = UserInfo.builder()
                    .username(user.getUsername())
                    .password(user.getPassword())
                    .role(Roles.PROFESSOR)
                    .additionalData(Map.of(Parameters.PROFESSOR_NAME, user.getFullName()))
                    .build();
            ActionResult result = userService.createUser(userInfo);
            if (!result.isSuccess()) {
                return result;
            }
            result = professorService.createProfessor(user.getUsername(), user.getFullName());
            if (!result.isSuccess()) {
                return result;
            }
        }

        return new ActionResult(true, CsvFormatter.toCsv(createdUsers, "fullName;username;password\n"));
    }
}
