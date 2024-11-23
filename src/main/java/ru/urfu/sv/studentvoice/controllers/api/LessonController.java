package ru.urfu.sv.studentvoice.controllers.api;

import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import ru.urfu.sv.studentvoice.controllers.ClassSessionController;
import ru.urfu.sv.studentvoice.controllers.links.Links;
import ru.urfu.sv.studentvoice.model.domain.dto.response.LessonResponse;
import ru.urfu.sv.studentvoice.services.LessonService;
import ru.urfu.sv.studentvoice.services.user.ProfessorService;

@RestController
@RequestMapping(Links.BASE_API + Links.LESSONS)
@PreAuthorize("@RolesAC.isAdminOrProfessor()")
public class LessonController {

    @Autowired
    private ProfessorService professorService;
    @Autowired
    private ClassSessionController sessionController;
    @Autowired
    private LessonService lessonService;

    /**
     * Ищем список пар ПАГИНИРОВАННЫЙ у преподавателя
     */
    @Operation(summary = "Поиск пар у преподавателя")
    @RequestMapping(path = "/list/pair", method = RequestMethod.GET)
    public ResponseEntity<Page<LessonResponse>> findListPair(@PageableDefault(size = 10000) Pageable pageable) {
        return new ResponseEntity<>(lessonService.findListPair(pageable), HttpStatus.OK);
    }

//    @PostMapping("create")
//    @Parameters(value = {
//            @Parameter(name = "courseId", in = ParameterIn.QUERY, required = true),
//            @Parameter(name = "professorName", in = ParameterIn.QUERY, required = true),
//            @Parameter(name = "startSession", in = ParameterIn.QUERY, required = true),
//            @Parameter(name = "endSession", in = ParameterIn.QUERY, required = true),
//            @Parameter(name = "roomName", in = ParameterIn.QUERY, required = true),
//            @Parameter(name = "sessionName", in = ParameterIn.QUERY, required = true)
//    })
//    public ResponseEntity<Map<String, Object>> createSession(HttpServletRequest request) {
//        ExtendedModelMap model = new ExtendedModelMap();
//        sessionController.createSession(null, request, model);
//
//        ActionResultResponse result = fromActionResult(model.getAttribute(RESULT));
//        return ResponseEntity.ok().body(Map.of(RESULT, result));
//    }

//    @GetMapping("find")
//    @Parameters(value = {
//            @Parameter(name = "sessionId", in = ParameterIn.QUERY, required = true)
//    })
//    public ResponseEntity<Map<String, Object>> findSession(HttpServletRequest request) {
//        ExtendedModelMap model = new ExtendedModelMap();
//        sessionController.sessionPage(request.getParameter(CLASS_SESSION_ID), model);
//
//        Map<String, Object> timerInfo = null;
//        if (model.getAttribute(CLASS_SESSION) != null) {
//            ClassSession session = (ClassSession) model.getAttribute(CLASS_SESSION);
//            if (session != null && session.getDisableAfterTimestamp() != null) {
//                if (Instant.now().isAfter(session.getDisableAfterTimestamp())) {
//                    timerInfo = Map.of("isActive", false, "message", "Время действия кода для отзыва вышло");
//                } else {
//                    //Костыль для зоны UTC+5
//                    OffsetDateTime disableTime = session.getDisableAfterTimestamp().atOffset(ZoneOffset.ofHours(5));
//                    String message = "Таймер успешно запущен. Ссылка перестанет работать %s".formatted(
//                            TemporalFormatter.formatToDateTime(disableTime));
//                    timerInfo = Map.of("isActive", true, "message", message);
//                }
//            }
//        }
//        return ResponseEntity.ok().body(
//                Map.ofEntries(
//                        Map.entry("timerInfo", orNull(timerInfo)),
//                        Map.entry(SESSION_QR, orNull(model.getAttribute(SESSION_QR))),
//                        Map.entry(CLASS_SESSION, orNull(model.getAttribute(CLASS_SESSION))),
//                        Map.entry(CLASS_SESSION_DATE, orNull(model.getAttribute(CLASS_SESSION_DATE))),
//                        Map.entry(REVIEW_URL, orNull(model.getAttribute(REVIEW_URL)))
//                )
//        );
//    }

//    @PostMapping("start-timer")
//    @Parameters(value = {
//            @Parameter(name = "sessionId", in = ParameterIn.QUERY, required = true),
//            @Parameter(name = "time", in = ParameterIn.QUERY, required = true)
//    })
//    public ResponseEntity<Map<String, Object>> startTimer(HttpServletRequest request) {
//        UUID sessionId = UUID.fromString(request.getParameter(CLASS_SESSION_ID));
//        Optional<ClassSession> sessionOpt = sessionService.findSessionById(sessionId);
//        if (sessionOpt.isEmpty()) {
//            return ResponseEntity.ok().body(
//                    Map.of(RESULT, fromActionResult(ActionResultFactory.sessionNotExist())));
//        }
//
//        ClassSession session = sessionOpt.get();
//
//        LocalTime time = LocalTime.parse(request.getParameter("time"));
//        sessionService.setDisableTimestamp(session, time);
//
//        return ResponseEntity.ok().body(
//                Map.of(RESULT, new ActionResultResponse(true,
//                        "Таймер успешно запущен. Ссылка перестанет работать %s".formatted(TemporalFormatter.formatToDateTime(time)))));
//    }

//    @GetMapping("reviews-list")
//    @Parameters(value = {
//            @Parameter(name = "sessionId", in = ParameterIn.QUERY, required = true)
//    })
//    public ResponseEntity<Map<String, Object>> getSessionStudents(HttpServletRequest request) {
//        ExtendedModelMap model = new ExtendedModelMap();
//        sessionController.sessionStudentsPage(request.getParameter(CLASS_SESSION_ID), model);
//
//        ActionResultResponse result = fromActionResult(model.getAttribute(RESULT));
//        return ResponseEntity.ok().body(
//                Map.of(RESULT, result,
//                        REVIEWS_LIST, orNull(model.getAttribute(REVIEWS_LIST))
//                )
//        );
//    }

//    @PostMapping("change-professor")
//    @Parameters(value = {
//            @Parameter(name = "sessionId", in = ParameterIn.QUERY, required = true),
//            @Parameter(name = "newProfessor", in = ParameterIn.QUERY, required = true)
//    })
//    public ResponseEntity<Map<String, Object>> changeProfessor(HttpServletRequest request) {
//        ExtendedModelMap model = new ExtendedModelMap();
//        sessionController.updateSession(request, model);
//
//        ActionResultResponse result = fromActionResult(model.getAttribute(RESULT));
//        return ResponseEntity.ok().body(Map.of(RESULT, result));
//    }
}