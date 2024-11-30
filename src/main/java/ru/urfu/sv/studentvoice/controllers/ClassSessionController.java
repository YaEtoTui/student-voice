package ru.urfu.sv.studentvoice.controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.urfu.sv.studentvoice.services.*;
import ru.urfu.sv.studentvoice.services.user.ProfessorService;

@Controller
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/sessions")
@PreAuthorize("@RolesAC.isAdminOrProfessor()")
public class ClassSessionController {

    private final LessonService sessionService;
    private final ReviewService reviewService;
    private final CourseService courseService;
    private final ProfessorService professorService;
    private final InstituteService instituteService;
    private final QRCodeService qrCodeService;

    @Value("${application.host}")
    private String applicationHost;

//    @GetMapping("/create")
//    public String createSessionPage(@RequestParam(value = COURSE_ID, required = false) UUID courseId,
//                                    @AuthenticationPrincipal UserDetails userDetails,
//                                    Model model) {
//        model.addAttribute(INSTITUTES_LIST, instituteService.findAllInstitutes());
//        model.addAttribute(COURSES_LIST, courseService.findAll());
//        addProfessorName(professorService, model, userDetails);
//
//        if (courseId != null) {
//            Optional<Course> courseOpt = courseService.findCourseById(courseId);
//            if (courseOpt.isEmpty()) {
//                model.addAttribute(RESULT, ActionResultFactory.courseNotExist());
//                return CREATE_SESSION;
//            }
//            model.addAttribute("requestedCourse", courseOpt.get());
//        }
//
//        return CREATE_SESSION;
//    }

//    @GetMapping("/{sessionId}/students")
//    public String sessionStudentsPage(@PathVariable("sessionId") String sessionIdStr, Model model) {
//        Optional<ClassSession> sessionOpt = sessionService.findSessionById(UUID.fromString(sessionIdStr));
//        if (sessionOpt.isEmpty()) {
//            model.addAttribute(RESULT, ActionResultFactory.sessionNotExist());
//            return SESSION_STUDENTS;
//        }
//        ClassSession session = sessionOpt.get();
//        List<Review> reviews = reviewService.findReviewsBySessionId(session.getSessionId());
//        model.addAttribute(REVIEWS_LIST, reviews);
//        model.addAttribute(CLASS_SESSION, session);
//        model.addAttribute(CLASS_SESSION_DATE, TemporalFormatter.formatToSessionDateTime(session));
//        return SESSION_STUDENTS;
//    }

//    @GetMapping("/{sessionId}/update")
//    public String updateSessionPage(@PathVariable("sessionId") String sessionIdStr, Model model) {
//        Optional<ClassSession> session = sessionService.findSessionById(UUID.fromString(sessionIdStr));
//        if (session.isEmpty()) {
//            model.addAttribute(RESULT, ActionResultFactory.sessionNotExist());
//            return UPDATE_SESSION;
//        }
//        List<Professor> professors = professorService.getAllProfessors();
//        model.addAttribute(PROFESSORS_LIST, professors);
//        model.addAttribute(CLASS_SESSION, session.get());
//        model.addAttribute(CLASS_SESSION_DATE, TemporalFormatter.formatToSessionDateTime(session.get()));
//        return UPDATE_SESSION;
//    }

//    @PostMapping("update")
//    public String updateSession(HttpServletRequest request, Model model) {
//        UUID sessionId = UUID.fromString(request.getParameter(CLASS_SESSION_ID));
//        Optional<ClassSession> session = sessionService.findSessionById(sessionId);
//        if (session.isEmpty()) {
//            model.addAttribute(RESULT, ActionResultFactory.sessionNotExist());
//            return UPDATE_SESSION;
//        }
//        String newProfessor = request.getParameter("newProfessor");
//        ActionResult result = sessionService.updateSessionProfessor(sessionId, newProfessor);
//        model.addAttribute(RESULT, result);
//        return REDIRECT.concat("/professor-home");
//    }
}