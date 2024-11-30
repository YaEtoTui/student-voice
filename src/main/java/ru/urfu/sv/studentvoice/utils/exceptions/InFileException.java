package ru.urfu.sv.studentvoice.utils.exceptions;

/**
 * При отправке файла не правильно заполнили поля
 *
 * @author Egor Sazhin
 * @since 30.11.2024
 */
public class InFileException extends RuntimeException {

    public InFileException(String message) {
        super(message);
    }
}