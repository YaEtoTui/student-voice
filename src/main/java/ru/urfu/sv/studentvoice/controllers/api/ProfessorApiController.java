package ru.urfu.sv.studentvoice.controllers.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import ru.urfu.sv.studentvoice.controllers.links.Links;
import ru.urfu.sv.studentvoice.services.LessonService;
import ru.urfu.sv.studentvoice.services.CourseService;
import ru.urfu.sv.studentvoice.services.user.ProfessorService;

/**
 * В данном контроллере получаем данные о преподавателе
 */
@RestController
@RequestMapping(Links.BASE_API + Links.PROFESSORS)
@PreAuthorize("@RolesAC.isProfessor()")
public class ProfessorApiController {

    @Autowired
    private ProfessorService professorService;
    @Autowired
    private CourseService courseService;
    @Autowired
    private LessonService sessionService;

//    @GetMapping("current")
//    @Parameters(value = {
//            @Parameter(name = "from", in = ParameterIn.QUERY, required = true),
//            @Parameter(name = "to", in = ParameterIn.QUERY, required = true)
//    })
//    public ResponseEntity<Map<String, Object>> getCurrent(@AuthenticationPrincipal UserDetails userDetails, HttpServletRequest request) {
//        String username = userDetails.getUsername();
//        Optional<Professor> professorOpt = professorService.findProfessorByUsername(username);
//
//        if (professorOpt.isEmpty()) {
//            return ResponseEntity.ok(Map.of(RESULT, ActionResultResponse.fromActionResult(ActionResultFactory.professorNotExist(username))));
//        } else {
//            String professorName = professorOpt.get().getFullName();
//            LocalDate from = LocalDate.parse(request.getParameter("from"));
//            LocalDate to = LocalDate.parse(request.getParameter("to"));
//
//            return ResponseEntity.ok().body(
//                    Map.ofEntries(
//                            Map.entry(PROFESSOR_NAME, professorOpt.get().getFullName()),
//                            Map.entry(COURSES_LIST, courseService.findCoursesByProfessorName(professorName)),
//                            Map.entry(CLASS_SESSIONS_LIST, sessionService.findSavedClassSessionsByProfessorName(professorName, from, to))
//                    )
//            );
//        }
//    }

//    @PostMapping("update-sessions")
//    @Parameters(value = {
//            @Parameter(name = "from", in = ParameterIn.QUERY, required = true),
//            @Parameter(name = "to", in = ParameterIn.QUERY, required = true)
//    })
//    public ResponseEntity<Map<String, Object>> update(@AuthenticationPrincipal UserDetails userDetails, HttpServletRequest request) {
//        String username = userDetails.getUsername();
//        Optional<Professor> professorOpt = professorService.findProfessorByUsername(username);
//
//        if (professorOpt.isEmpty()) {
//            return ResponseEntity.ok(Map.of(RESULT, ActionResultResponse.fromActionResult(ActionResultFactory.professorNotExist(username))));
//        } else {
//            String professorName = professorOpt.get().getFullName();
//            LocalDate from = LocalDate.parse(request.getParameter("from"));
//            LocalDate to = LocalDate.parse(request.getParameter("to"));
//
//            try {
//                List<ClassSession> allSessions = sessionService.findAllClassSessionsByProfessorName(professorName, from, to);
//                return ResponseEntity.ok(Map.of(CLASS_SESSIONS_LIST, allSessions, PROFESSOR_NAME, professorName));
//            } catch (ModeusException e) {
//                return ResponseEntity.ok(Map.of(RESULT, new ActionResultResponse(false, "Во время получения пар из модеуса произошла ошибка")));
//            }
//        }
//    }
}