package ru.urfu.sv.studentvoice.controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.urfu.sv.studentvoice.services.LessonService;
import ru.urfu.sv.studentvoice.services.CourseService;
import ru.urfu.sv.studentvoice.services.InstituteService;
import ru.urfu.sv.studentvoice.services.ReviewService;

@Controller
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/rating")
@PreAuthorize("@RolesAC.isAdmin()")
public class RatingController {
    private final InstituteService instituteService;
    private final CourseService courseService;
    private final LessonService sessionService;
    private final ReviewService reviewService;

//    @GetMapping("/institutes")
//    public String institutesRating(Model model) {
//        List<Institute> instituteList = instituteService.findAllInstitutes();
//
//        instituteList.forEach(inst -> {
//            List<ClassSession> sessions = sessionService.findClassSessionsByInstituteName(inst.getShortName());
//            inst.setAvgRating(reviewService.getAverageRatingBySessions(sessions));
//        });
//
//        model.addAttribute(INSTITUTES_LIST, instituteList);
//
//        return Templates.RATING_INSTITUTES;
//    }

//    @GetMapping("/institute/{instituteId}")
//    public String instituteRating(@PathVariable(INSTITUTE_ID) Integer instituteId,
//                                  Model model) {
//        Optional<Institute> instituteOpt = instituteService.findInstituteById(instituteId);
//        if (instituteOpt.isEmpty()) {
//            model.addAttribute(RESULT, ActionResultFactory.instituteNotExist());
//            return Templates.RATING_INSTITUTE;
//        }
//
//        List<Course> courses = courseService.findCoursesByInstituteName(instituteOpt.get().getShortName());
//        courses.forEach(course -> {
//            List<ClassSession> sessions = sessionService.findClassSessionsByCourseId(course.getCourseId());
//            course.setAvgRating(reviewService.getAverageRatingBySessions(sessions));
//        });
//        model.addAttribute("institute", instituteOpt.get());
//        model.addAttribute(COURSES_LIST, courses);
//        return Templates.RATING_INSTITUTE;
//    }

//    @GetMapping("/course/{courseId}")
//    public String courseRating(@PathVariable(COURSE_ID) UUID courseId,
//                               Model model) {
//        Optional<Course> courseOpt = courseService.findCourseById(courseId);
//        if (courseOpt.isEmpty()) {
//            model.addAttribute(RESULT, ActionResultFactory.courseNotExist());
//            return Templates.RATING_COURSE;
//        }
//
//        List<ClassSession> sessions = sessionService.findClassSessionsByCourseId(courseId);
//        sessions.forEach(session -> session.setAvgRating(reviewService.getAverageRatingBySessions(Collections.singletonList(session))));
//
//        model.addAttribute(COURSE_NAME, courseOpt.get().getCourseDetails().getCourseName());
//        model.addAttribute(CLASS_SESSIONS_LIST, sessions);
//        return Templates.RATING_COURSE;
//    }

//    @GetMapping("/session/{sessionId}")
//    public String sessionRating(@PathVariable(CLASS_SESSION_ID) String sessionIdStr,
//                                Model model) {
//        UUID sessionId = UUID.fromString(sessionIdStr);
//        Optional<ClassSession> sessionOpt = sessionService.findSessionById(sessionId);
//        if (sessionOpt.isEmpty()) {
//            model.addAttribute(RESULT, ActionResultFactory.sessionNotExist());
//            return Templates.RATING_COURSE;
//        }
//
//        List<Review> reviews = reviewService.findReviewsBySessionId(sessionId);
//
//        model.addAttribute(REVIEWS_LIST, reviews);
//        String sessionDate = TemporalFormatter.formatToDateTime(sessionOpt.get().getStartDateTime());
//        CourseDetails details = sessionOpt.get().getCourseDetails();
//        model.addAttribute(CLASS_SESSION, "%s, %s / %s - %s"
//                .formatted(details.getCourseName(), details.getInstituteAddress(), sessionOpt.get().getRoomName(), sessionDate));
//        return Templates.RATING_SESSION;
//    }
}
