package ru.urfu.sv.studentvoice.model.query;

import com.querydsl.jpa.JPQLQueryFactory;
import org.springframework.beans.factory.annotation.Autowired;

public abstract class AbstractQuery {

    @Autowired
    private JPQLQueryFactory query;

    public JPQLQueryFactory query() {
        return query;
    }
}