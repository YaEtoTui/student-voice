package ru.urfu.sv.studentvoice.controllers.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.urfu.sv.studentvoice.controllers.links.Links;
import ru.urfu.sv.studentvoice.services.LessonService;

import java.time.LocalDate;

@RestController
@SecurityRequirement(name = "Bearer Authentication")
@RequestMapping(Links.BASE_API + Links.INTEGRATIONS)
public class IntegrationController {

    @Autowired
    private LessonService lessonService;

    @Operation(summary = "Выгрузка пар из модеуса")
    @RequestMapping(path = "/import/modeus", method = RequestMethod.POST)
    @SneakyThrows
    public ResponseEntity<Void> importFromModeus(@RequestParam LocalDate fromDate, @RequestParam LocalDate toDate) {
        lessonService.importLessonsForModeus(fromDate, toDate);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}