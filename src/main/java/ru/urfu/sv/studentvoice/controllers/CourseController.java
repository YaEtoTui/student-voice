package ru.urfu.sv.studentvoice.controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import ru.urfu.sv.studentvoice.services.ClassSessionService;
import ru.urfu.sv.studentvoice.services.CourseService;
import ru.urfu.sv.studentvoice.services.InstituteService;
import ru.urfu.sv.studentvoice.services.ProfessorService;

@Controller
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/courses")
@PreAuthorize("@RolesAC.isAdminOrProfessor()")
public class CourseController {
    private final CourseService courseService;
    private final ProfessorService professorService;
    private final ClassSessionService sessionService;
    private final InstituteService instituteService;

//    @GetMapping("/{courseId}")
//    public String coursePage(@PathVariable("courseId") UUID courseId, Model model) {
//        Optional<Course> courseOpt = courseService.findCourseById(courseId);
//        if (courseOpt.isEmpty()) {
//            model.addAttribute(RESULT, ActionResultFactory.courseNotExist());
//            return Templates.COURSE_PAGE;
//        }
//
//        List<ClassSession> sessions = sessionService.findClassSessionsByCourseId(courseId);
//        model.addAttribute(CLASS_SESSIONS_LIST, sessions);
//        model.addAttribute(COURSE_DETAILS, courseOpt.get().getCourseDetails());
//        model.addAttribute(COURSE_ID, courseId);
//        return Templates.COURSE_PAGE;
//    }
}