package ru.urfu.sv.studentvoice.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import ru.urfu.sv.studentvoice.controllers.links.Links;
import ru.urfu.sv.studentvoice.services.FormService;

/**
 * Контроллер работы с формой для заполнения людьми,
 * которые не имеют доступа в studentVoice
 *
 * @author Egor Sazhin
 * @since 20.12.2024
 */
@RestController
@SecurityRequirement(name = "Bearer Authentication")
@RequestMapping(Links.BASE_API + Links.FORMS)
public class FormController {

    @Autowired
    private FormService formService;

    @Operation(summary = "Получаем данные для формы. При этом проверяем qr-код на актуальность")
    @RequestMapping(path = "check-qr-code/{lessonId}", method = RequestMethod.GET)
    public ResponseEntity<?> checkQrCodeActual(@PathVariable Long lessonId) {
        final boolean isActualQrCode = formService.checkQrCodeActual(lessonId);
        return new ResponseEntity<>(isActualQrCode, HttpStatus.OK);
    }
}