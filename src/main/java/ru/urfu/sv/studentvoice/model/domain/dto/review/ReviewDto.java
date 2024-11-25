package ru.urfu.sv.studentvoice.model.domain.dto.review;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class ReviewDto {

    private String fio;
    private String rating;
    private LocalDateTime createTime;
    private String review;
}