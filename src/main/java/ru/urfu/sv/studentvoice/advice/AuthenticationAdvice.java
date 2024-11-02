package ru.urfu.sv.studentvoice.advice;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.urfu.sv.studentvoice.utils.exceptions.InvalidLoginException;

/**
 * Advice, который ловит exception при неправильном аутентификации
 *
 * @author Egor Sazhin
 * @since 03.11.2024
 */
@RestControllerAdvice
public class AuthenticationAdvice {

    @ExceptionHandler(InvalidLoginException.class)
    public ResponseEntity<String> handleInvalid(Exception e) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body(e.getMessage());
    }
}