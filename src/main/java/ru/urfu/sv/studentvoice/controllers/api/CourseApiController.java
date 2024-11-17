package ru.urfu.sv.studentvoice.controllers.api;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.ui.ExtendedModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.urfu.sv.studentvoice.controllers.CourseController;
import ru.urfu.sv.studentvoice.utils.result.ActionResultResponse;

import java.util.Map;
import java.util.UUID;

import static ru.urfu.sv.studentvoice.utils.consts.Parameters.*;
import static ru.urfu.sv.studentvoice.utils.model.ModelUtils.orNull;
import static ru.urfu.sv.studentvoice.utils.result.ActionResultResponse.fromActionResult;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/courses")
@PreAuthorize("@RolesAC.isAdminOrProfessor()")
public class CourseApiController {
    private final CourseController courseController;

//    @PostMapping("create")
//    @Parameters(value = {
//            @Parameter(name = "instituteId", in = ParameterIn.QUERY, required = true),
//            @Parameter(name = "courseName", in = ParameterIn.QUERY, required = true),
//            @Parameter(name = "professorsNames", in = ParameterIn.QUERY, required = true)
//    })
//    public ResponseEntity<Map<String, Object>> createCourse(HttpServletRequest request) {
//        ExtendedModelMap model = new ExtendedModelMap();
//        courseController.createCourse(null, request, model);
//        ActionResultResponse result = fromActionResult(model.getAttribute(RESULT));
//
//        return ResponseEntity.ok().body(Map.of(RESULT, result));
//    }
//
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
