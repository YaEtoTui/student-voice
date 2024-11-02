package ru.urfu.sv.studentvoice.utils.exceptions;

/**
 * Исключение при неправильной авторизации ->
 * введение неверного логина или пароля
 */
public class InvalidLoginException extends RuntimeException {

    public InvalidLoginException(String message, Throwable cause) {
        super(message, cause);
    }
}