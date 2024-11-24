package ru.urfu.sv.studentvoice.controllers.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import ru.urfu.sv.studentvoice.controllers.links.Links;
import ru.urfu.sv.studentvoice.model.domain.dto.course.CourseInfo;
import ru.urfu.sv.studentvoice.model.domain.dto.response.CourseResponse;
import ru.urfu.sv.studentvoice.services.CourseService;

@RestController
@SecurityRequirement(name = "Bearer Authentication")
@RequestMapping(Links.BASE_API + Links.COURSES)
@PreAuthorize("@RolesAC.isAdminOrProfessor()")
public class CourseApiController {

    @Autowired
    private CourseService courseService;

    @Operation(summary = "Создание курса")
    @RequestMapping(path = "/create", method = RequestMethod.POST)
    public ResponseEntity<Void> createCourse(@RequestBody CourseInfo courseInfo) {
        courseService.createCourse(courseInfo);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    /**
     * Ищем список предметов ПАГИНИРОВАННЫЙ у преподавателя
     */
    @Operation(summary = "Поиск предметов у преподавателя")
    @RequestMapping(path = "/list", method = RequestMethod.GET)
    public ResponseEntity<Page<CourseResponse>> findCourseList(@RequestParam(required = false, name = "search-text") String searchText,
                                                               @PageableDefault(size = 10000) Pageable pageable) {
        return new ResponseEntity<>(courseService.findCourseList(searchText, pageable), HttpStatus.OK);
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