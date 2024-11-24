package ru.urfu.sv.studentvoice.model.domain.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FullAddress {

    private String building;
    private String room;

    public FullAddress(String building, String room) {
        this.building = building;
        this.room = room;
    }
}
