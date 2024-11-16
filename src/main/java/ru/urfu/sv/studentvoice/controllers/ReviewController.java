package ru.urfu.sv.studentvoice.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import ru.urfu.sv.studentvoice.services.ClassSessionService;
import ru.urfu.sv.studentvoice.services.CourseService;
import ru.urfu.sv.studentvoice.services.ReviewService;

@Controller
@Slf4j
@RequestMapping("/reviews")
public class ReviewController {

    @Autowired
    private ReviewService reviewService;
    @Autowired
    private ClassSessionService sessionService;
    @Autowired
    private CourseService courseService;

//    @GetMapping("/create")
//    public String createReviewPage(@RequestParam("sessionId") String sessionIdStr, Model model) {
//        UUID sessionId = UUID.fromString(sessionIdStr);
//        Optional<ClassSession> sessionOpt = sessionService.findSessionById(sessionId);
//        if (sessionOpt.isEmpty()) {
//            model.addAttribute(RESULT, ActionResultFactory.sessionNotExist());
//            return Templates.REVIEW_STATUS;
//        }
//
//        if (sessionOpt.get().getDisableAfterTimestamp() != null && Instant.now().isAfter(sessionOpt.get().getDisableAfterTimestamp())) {
//            model.addAttribute(RESULT, new ActionResult(false, "Отзыв на эту пару больше нельзя оставить"));
//            return Templates.REVIEW_STATUS;
//        }
//
//        Optional<Course> courseOpt = courseService.findCourseBySession(sessionOpt.get());
//        if (courseOpt.isEmpty()) {
//            model.addAttribute(RESULT, ActionResultFactory.courseNotExist());
//            return Templates.REVIEW_STATUS;
//        }
//
//        Course course = courseOpt.get();
//        ClassSession session = sessionOpt.get();
//        model.addAttribute(COURSE_NAME, course.getCourseDetails().getCourseName());
//        model.addAttribute("address", "%s / %s".formatted(course.getCourseDetails().getInstituteAddress(), session.getRoomName()));
//        model.addAttribute(CLASS_SESSION_DATE, TemporalFormatter.formatToDateTime(session.getStartDateTime()));
//        model.addAttribute(PROFESSOR_NAME, session.getProfessorName());
//
//        return Templates.CREATE_REVIEW;
//    }

//    @PostMapping("/save")
//    public String saveReview(HttpServletRequest request, Model model) {
//        Optional<ClassSession> sessionOpt = sessionService.findSessionById(UUID.fromString(request.getParameter(CLASS_SESSION_ID)));
//        if(sessionOpt.isEmpty()){
//            model.addAttribute(RESULT, ActionResultFactory.sessionNotExist());
//            return Templates.REVIEW_STATUS;
//        }
//        if (sessionOpt.get().getDisableAfterTimestamp() != null && Instant.now().isAfter(sessionOpt.get().getDisableAfterTimestamp())) {
//            model.addAttribute(RESULT, new ActionResult(false, "Отзыв на эту пару больше нельзя оставить"));
//            return Templates.REVIEW_STATUS;
//        }
//
//        ActionResult result = saveReview(request.getParameterMap());
//        model.addAttribute(RESULT, result);
//
//        if (result.isSuccess()) {
//            return Templates.REVIEW_STATUS;
//        } else {
//            return Templates.CREATE_REVIEW;
//        }
//    }
//
//    private ActionResult saveReview(Map<String, String[]> parameters) {
//        Review review = Review
//                .builder()
//                .sessionId(UUID.fromString(parameters.get(CLASS_SESSION_ID)[0]))
//                .studentFullName(parameters.get(STUDENT_FULL_NAME)[0])
//                .value(Byte.valueOf(parameters.get(REVIEW_VALUE)[0]))
//                .comment(parameters.get(COMMENT)[0])
//                .timestamp(Instant.now())
//                .build();
//
//        try {
//            ActionResult result = reviewService.saveReview(review);
//            log.info(result.getFormattedMessage());
//            return result;
//        } catch (IllegalArgumentException e) {
//            log.error("Отзыв не был сохранен", e);
//            return ActionResultFactory.reviewCreatingError();
//        }
//    }
}
