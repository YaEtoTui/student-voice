package ru.urfu.sv.studentvoice.controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import ru.urfu.sv.studentvoice.services.ClassSessionService;
import ru.urfu.sv.studentvoice.services.CourseService;
import ru.urfu.sv.studentvoice.services.user.ProfessorService;

@Controller
@RequiredArgsConstructor
@Slf4j
@PreAuthorize("@RolesAC.isProfessor()")
public class ProfessorController {
    private final ProfessorService professorService;
    private final CourseService courseService;
    private final ClassSessionService sessionService;

//    @GetMapping("/professor-home")
//    public String professorHomePage(@AuthenticationPrincipal UserDetails userDetails, Model model) {
//        String username = userDetails.getUsername();
//        Optional<Professor> professorOpt = professorService.findProfessorByUsername(username);
//
//        if (professorOpt.isEmpty()) {
//            model.addAttribute(RESULT, ActionResultFactory.professorNotExist(username));
//        } else {
//            String professorName = professorOpt.get().getFullName();
//            model.addAttribute(PROFESSOR_NAME, professorName);
//            model.addAttribute(COURSES_LIST, courseService.findCoursesByProfessorName(professorName));
//            LocalDate currentDate = LocalDate.now();
//            model.addAttribute(CLASS_SESSIONS_LIST,
//                    sessionService.findSavedClassSessionsByProfessorName(professorName,
//                            currentDate.withDayOfMonth(1),
//                            currentDate.plusMonths(1).withDayOfMonth(1).minusDays(1)));
//        }
//
//        return Templates.PROFESSOR_HOME;
//    }

//    @PostMapping("/professor-home/update")
//    public String updateProfessorHomePage(@AuthenticationPrincipal UserDetails userDetails, Model model) {
//        String username = userDetails.getUsername();
//        Optional<Professor> professorOpt = professorService.findProfessorByUsername(username);
//
//        if (professorOpt.isEmpty()) {
//            model.addAttribute(RESULT, ActionResultFactory.professorNotExist(username));
//        } else {
//            String professorName = professorOpt.get().getFullName();
//            LocalDate currentDate = LocalDate.now();
//            List<ClassSession> allSessions;
//            try {
//                allSessions = sessionService.findAllClassSessionsByProfessorName(professorName,
//                        currentDate.withDayOfMonth(1),
//                        currentDate.plusMonths(1).withDayOfMonth(1).minusDays(1));
//            } catch (ModeusException e) {
//                log.error("Во время получения пар из модеуса произошла ошибка", e);
//                model.addAttribute(RESULT,
//                        new ActionResult(false, "Во время обновления данных из Модеуса произошла ошибка"));
//                model.addAttribute(PROFESSOR_NAME, professorName);
//                model.addAttribute(COURSES_LIST, courseService.findCoursesByProfessorName(professorName));
//                model.addAttribute(CLASS_SESSIONS_LIST, sessionService.findSavedClassSessionsByProfessorName(professorName,
//                        currentDate.withDayOfMonth(1),
//                        currentDate.plusMonths(1).withDayOfMonth(1).minusDays(1)));
//                return Templates.REDIRECT.concat("/professor-home");
//            }
//
//            model.addAttribute(PROFESSOR_NAME, professorName);
//            model.addAttribute(COURSES_LIST, courseService.findCoursesByProfessorName(professorName));
//            model.addAttribute(CLASS_SESSIONS_LIST, allSessions);
//        }
//
//        return Templates.REDIRECT.concat("/professor-home");
//    }
}
