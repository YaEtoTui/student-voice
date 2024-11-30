package ru.urfu.sv.studentvoice.advice;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.urfu.sv.studentvoice.utils.exceptions.InFileException;
import ru.urfu.sv.studentvoice.utils.exceptions.InvalidLoginException;

/**
 * Advice, который ловит exception из-за неправильного ввода данных
 *
 * @author Egor Sazhin
 * @since 03.11.2024
 */
@RestControllerAdvice
public class InputDataAdvice {

    @ExceptionHandler(InvalidLoginException.class)
    public ResponseEntity<String> handleInvalid(Exception e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(e.getMessage());
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleIllegalArgument(Exception e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(e.getMessage());
    }

    @ExceptionHandler(InFileException.class)
    public ResponseEntity<String> handleInFile(Exception e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(e.getMessage());
    }
}