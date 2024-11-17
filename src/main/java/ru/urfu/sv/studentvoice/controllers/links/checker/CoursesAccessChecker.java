package ru.urfu.sv.studentvoice.controllers.links.checker;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.urfu.sv.studentvoice.model.domain.dto.course.CourseInfo;
import ru.urfu.sv.studentvoice.model.query.CourseQuery;

@Component("CoursesAC")
public class CoursesAccessChecker {

    @Autowired
    private CourseQuery courseQuery;

    public boolean isExistCourse(CourseInfo courseInfo) {
        return courseQuery.isExistCourse(courseInfo);
    }

    /**
     * Можно ли создавать нового пользователя с таким username
     */
    public boolean isCreateNewCourse(CourseInfo courseInfo) {
        return !isExistCourse(courseInfo);
    }
}