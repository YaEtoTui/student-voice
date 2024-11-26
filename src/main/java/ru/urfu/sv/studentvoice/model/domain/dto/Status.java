package ru.urfu.sv.studentvoice.model.domain.dto;

import lombok.Getter;

/**
 * Статус занятия
 *
 * @author Egor Sazhin
 * @since 22.11.2024
 */
public enum Status {

    PLANNED("Запланировано"),
    CONDUCTED("Завершено");

    @Getter
    private String titleStatus;

    Status(String titleStatus) {
        this.titleStatus = titleStatus;
    }
}