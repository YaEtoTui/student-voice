package ru.urfu.sv.studentvoice.model.domain.dto;

import lombok.Getter;

/**
 * Категория отзыва
 * //ToDo переделать
 *
 * @author Egor Sazhin
 * @since 25.11.2024
 */
public enum CategoryReviews {

    REVIEW_STUDENT("review_student");

    @Getter
    private String name;

    CategoryReviews(String name) {
        this.name = name;
    }
}