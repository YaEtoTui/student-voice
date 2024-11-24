package ru.urfu.sv.studentvoice.controllers.user;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import ru.urfu.sv.studentvoice.controllers.links.Links;
import ru.urfu.sv.studentvoice.model.domain.dto.response.UserInfoResponse;
import ru.urfu.sv.studentvoice.services.user.UserService;

/**
 * Контроллер отвечает за информацию по пользователю
 *
 * @author Egor Sazhin
 * @since 05.11.2024
 */
@RestController
@SecurityRequirement(name = "Bearer Authentication")
@RequestMapping(Links.BASE_API + Links.USER)
public class UserInfoController {

    @Autowired
    private UserService userService;

    /**
     * Получаем информацию о пользователе
     */
    @Operation(summary = "Информация о текущем пользователе")
    @RequestMapping(path = "/info", method = RequestMethod.GET)
    public ResponseEntity<UserInfoResponse> getInfoForUser() {

        final UserInfoResponse userInfoResponse = userService.getInfoForUser();
        return new ResponseEntity<>(userInfoResponse, HttpStatus.OK);
    }
}