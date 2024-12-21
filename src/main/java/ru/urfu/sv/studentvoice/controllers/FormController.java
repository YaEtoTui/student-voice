package ru.urfu.sv.studentvoice.controllers;

import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import ru.urfu.sv.studentvoice.controllers.links.Links;
import ru.urfu.sv.studentvoice.model.domain.dto.form.FormInfoDto;
import ru.urfu.sv.studentvoice.services.form.FormService;

/**
 * Контроллер работы с формой для заполнения людьми,
 * которые не имеют доступа в studentVoice
 *
 * @author Egor Sazhin
 * @since 20.12.2024
 */
@RestController
@RequestMapping(Links.BASE_API + Links.FORMS)
public class FormController {

    @Autowired
    private FormService formService;

    @Operation(summary = "Получаем данные для формы. При этом проверяем qr-код на актуальность")
    @RequestMapping(path = "check-qr-code/{lessonId}", method = RequestMethod.GET)
    public ResponseEntity<FormInfoDto> getDataForm(@PathVariable Long lessonId) {
        return new ResponseEntity<>(formService.getDataForm(lessonId), HttpStatus.OK);
    }
}