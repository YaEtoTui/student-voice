package ru.urfu.sv.studentvoice.controllers.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import ru.urfu.sv.studentvoice.controllers.links.Links;
import ru.urfu.sv.studentvoice.model.domain.dto.course.CourseInfo;
import ru.urfu.sv.studentvoice.services.CourseService;

@RestController
@RequestMapping(Links.BASE_API + Links.COURSES)
@PreAuthorize("@RolesAC.isAdminOrProfessor()")
public class CourseApiController {

    @Autowired
    private CourseService courseService;

    @RequestMapping(path = "/create", method = RequestMethod.POST)
    public ResponseEntity<Void> createCourse(@RequestBody CourseInfo courseInfo) {
        courseService.createCourse(courseInfo);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

//    @GetMapping("find")
//    @Parameters(value = {
//            @Parameter(name = "courseId", in = ParameterIn.QUERY, required = true)
//    })
//    public ResponseEntity<Map<String, Object>> findCourse(HttpServletRequest request) {
//        ExtendedModelMap model = new ExtendedModelMap();
//        courseController.coursePage(UUID.fromString(request.getParameter(COURSE_ID)), model);
//        ActionResultResponse result = fromActionResult(model.getAttribute(RESULT));
//
//        return ResponseEntity.ok().body(
//                Map.ofEntries(Map.entry(RESULT, result),
//                        Map.entry(CLASS_SESSIONS_LIST, orNull(model.getAttribute(CLASS_SESSIONS_LIST))),
//                        Map.entry(COURSE_DETAILS, orNull(model.getAttribute(COURSE_DETAILS)))
//                ));
//    }
}