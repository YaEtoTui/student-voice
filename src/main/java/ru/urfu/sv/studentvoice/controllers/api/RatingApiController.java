package ru.urfu.sv.studentvoice.controllers.api;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.ui.ExtendedModelMap;
import org.springframework.web.bind.annotation.*;
import ru.urfu.sv.studentvoice.controllers.RatingController;
import ru.urfu.sv.studentvoice.utils.result.ActionResultResponse;

import java.util.Map;
import java.util.UUID;

import static ru.urfu.sv.studentvoice.utils.consts.Parameters.*;
import static ru.urfu.sv.studentvoice.utils.model.ModelUtils.orNull;
import static ru.urfu.sv.studentvoice.utils.result.ActionResultResponse.fromActionResult;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/rating")
@PreAuthorize("@AuthoritiesAC.isAdmin()")
public class RatingApiController {
    private final RatingController ratingController;

    @GetMapping("institutes")
    public ResponseEntity<Map<String, Object>> institutesRating() {
        ExtendedModelMap model = new ExtendedModelMap();
        ratingController.institutesRating(model);

        return ResponseEntity.ok().body(Map.of(INSTITUTES_LIST, orNull(model.getAttribute(INSTITUTES_LIST))));
    }

    @GetMapping("/institute/{instituteId}")
    @Parameters(value = {
            @Parameter(name = "instituteId", in = ParameterIn.PATH, required = true)
    })
    public ResponseEntity<Map<String, Object>> instituteRating(@PathVariable(INSTITUTE_ID) Integer instituteId) {
        ExtendedModelMap model = new ExtendedModelMap();
        ratingController.instituteRating(instituteId, model);

        ActionResultResponse result = fromActionResult(model.getAttribute(RESULT));
        return ResponseEntity.ok().body(Map.of(RESULT, result,
                COURSES_LIST, orNull(model.getAttribute(COURSES_LIST))));
    }

    @GetMapping("/course/{courseId}")
    @Parameters(value = {
            @Parameter(name = "courseId", in = ParameterIn.PATH, required = true)
    })
    public ResponseEntity<Map<String, Object>> instituteRating(@PathVariable(COURSE_ID) UUID courseId) {
        ExtendedModelMap model = new ExtendedModelMap();
        ratingController.courseRating(courseId, model);

        ActionResultResponse result = fromActionResult(model.getAttribute(RESULT));
        return ResponseEntity.ok().body(Map.of(RESULT, result,
                CLASS_SESSIONS_LIST, orNull(model.getAttribute(CLASS_SESSIONS_LIST))));
    }

    @GetMapping("/session/{sessionId}")
    @Parameters(value = {
            @Parameter(name = "sessionId", in = ParameterIn.PATH, required = true)
    })
    public ResponseEntity<Map<String, Object>> instituteRating(@PathVariable(CLASS_SESSION_ID) String sessionIdStr) {
        ExtendedModelMap model = new ExtendedModelMap();
        ratingController.sessionRating(sessionIdStr, model);

        ActionResultResponse result = fromActionResult(model.getAttribute(RESULT));
        return ResponseEntity.ok().body(Map.of(RESULT, result,
                REVIEWS_LIST, orNull(model.getAttribute(REVIEWS_LIST))));
    }
}