package ru.urfu.sv.studentvoice.controllers.user;

import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import ru.urfu.sv.studentvoice.controllers.links.Links;
import ru.urfu.sv.studentvoice.model.domain.dto.user.UserInfoDto;
import ru.urfu.sv.studentvoice.services.user.UserService;

@RestController
@RequestMapping(Links.BASE_API + Links.USERS)
public class UsersApiController {

    @Autowired
    private UserService userService;

    @Operation(summary = "Создание пользователя с ролью")
    @RequestMapping(path = "create", method = RequestMethod.POST)
    public ResponseEntity<Void> createUser(@RequestBody UserInfoDto userInfoDto) {
        userService.createUser(userInfoDto);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    //ToDo
//    @Operation(summary = "Создание/обновление данных о пользователях с помощью файла")
//    @RequestMapping(path = "/create-from-file", method = RequestMethod.POST)
//    public ResponseEntity<Void> uploadUsers(@RequestBody MultipartFile file) {
//        userService.createUsersFromFile(file);
//        return new ResponseEntity(HttpStatus.CREATED);
//    }
}