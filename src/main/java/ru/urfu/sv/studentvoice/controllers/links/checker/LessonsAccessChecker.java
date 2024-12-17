package ru.urfu.sv.studentvoice.controllers.links.checker;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.urfu.sv.studentvoice.model.domain.dto.json.JLesson;
import ru.urfu.sv.studentvoice.model.query.LessonQuery;

@Component("LessonsAC")
public class LessonsAccessChecker {

    @Autowired
    private LessonQuery lessonQuery;

    private boolean isExistLesson(JLesson lesson) {
        return lessonQuery.isExistLesson(lesson);
    }

    public boolean isCreateNewLesson(JLesson lesson) {
        return !isExistLesson(lesson);
    }
}