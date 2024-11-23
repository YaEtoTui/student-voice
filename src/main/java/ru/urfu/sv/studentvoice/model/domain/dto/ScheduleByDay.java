package ru.urfu.sv.studentvoice.model.domain.dto;

import lombok.Getter;
import lombok.Setter;
import ru.urfu.sv.studentvoice.model.domain.dto.response.LessonDetailsResponse;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class ScheduleByDay {

    private LocalDate date;
    private List<LessonDetailsResponse> listLessons;

    public ScheduleByDay() {
        this.listLessons = new ArrayList<>();
    }
}