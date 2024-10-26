package ru.urfu.sv.studentvoice.utils.model;

import com.opencsv.bean.CsvBindByPosition;
import lombok.Value;

@Value
public class CreatedUser {
    @CsvBindByPosition(position = 0)
    String fullName;
    @CsvBindByPosition(position = 1)
    String username;
    @CsvBindByPosition(position = 2)
    String password;
}
