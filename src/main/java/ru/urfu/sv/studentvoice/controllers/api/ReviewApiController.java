package ru.urfu.sv.studentvoice.controllers.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import ru.urfu.sv.studentvoice.controllers.links.Links;
import ru.urfu.sv.studentvoice.model.domain.dto.response.ReviewResponse;
import ru.urfu.sv.studentvoice.services.ReviewService;

@RestController
@SecurityRequirement(name = "Bearer Authentication")
@RequestMapping(Links.BASE_API + Links.REVIEWS)
public class ReviewApiController {

    @Autowired
    private ReviewService reviewService;

    @Operation(summary = "Поиск отзывов студентов по паре")
    @RequestMapping(path = "/list/by-lesson/{lessonId}", method = RequestMethod.GET)
    public ResponseEntity<Page<ReviewResponse>> findReviewsByLesson(@PathVariable Long lessonId,
                                                                    @PageableDefault(size = 10000) Pageable pageable) {
        final Page<ReviewResponse> response = reviewService.findReviewsByLessonId(lessonId, pageable);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}