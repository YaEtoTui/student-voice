package ru.urfu.sv.studentvoice.model.domain.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class ReviewResponse {

    private String fio;

    private Long rating;

    @JsonProperty("create_time")
    private LocalDateTime createTime;

    private String comment;
}