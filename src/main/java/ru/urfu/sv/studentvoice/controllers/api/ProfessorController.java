package ru.urfu.sv.studentvoice.controllers.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import ru.urfu.sv.studentvoice.controllers.links.Links;
import ru.urfu.sv.studentvoice.model.domain.dto.response.ProfessorResponse;
import ru.urfu.sv.studentvoice.services.user.UserService;

import java.util.List;

/**
 * В данном контроллере получаем данные о преподавателе
 */
@RestController
@SecurityRequirement(name = "Bearer Authentication")
@RequestMapping(Links.BASE_API + Links.PROFESSORS)
public class ProfessorController {

    @Autowired
    private UserService userService;

    @Operation(summary = "Вывод всех преподавателей")
    @RequestMapping(path = "/list", method = RequestMethod.GET)
    public ResponseEntity<List<ProfessorResponse>> findProfessorList() {
        return new ResponseEntity<>(userService.findProfessorList(), HttpStatus.OK);
    }
}