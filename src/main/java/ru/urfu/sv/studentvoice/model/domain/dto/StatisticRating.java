package ru.urfu.sv.studentvoice.model.domain.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StatisticRating {

    private Long rating;

    @JsonProperty("category_name")
    private String categoryName;

    public StatisticRating(Long rating, String categoryName) {
        this.rating = rating;
        this.categoryName = categoryName;
    }
}