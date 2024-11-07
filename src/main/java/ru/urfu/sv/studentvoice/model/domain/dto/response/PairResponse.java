package ru.urfu.sv.studentvoice.model.domain.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * Пара, которую мы возвращаем в ответ
 */
@Getter
@Setter
public class PairResponse {
    private String status;
    @JsonProperty("course_name")
    private String courseName;
    @JsonProperty("date_start")
    private LocalDateTime dateStart;
    @JsonProperty("date_end")
    private LocalDateTime dateEnd;
}