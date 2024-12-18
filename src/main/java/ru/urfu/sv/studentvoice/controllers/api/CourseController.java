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
public class CourseController {

    @Autowired
    private CourseService courseService;

    @Operation(summary = "Создание предмета")
    @RequestMapping(path = "/create", method = RequestMethod.POST)
    public ResponseEntity<Void> createCourse(@RequestBody CourseInfo courseInfo) {
        courseService.createCourse(courseInfo);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @Operation(summary = "Редактирование предмета")
    @RequestMapping(path = "{courseId}", method = RequestMethod.PUT)
    public ResponseEntity<Void> updateCourse(@PathVariable Long courseId,
                                             @RequestBody CourseInfo courseInfo) {
        courseService.updateCourse(courseId, courseInfo);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Operation(summary = "Удаление предмета")
    @RequestMapping(path = "{courseId}", method = RequestMethod.DELETE)
    public ResponseEntity<Void> deleteCourse(@PathVariable Long courseId) {
        courseService.deleteCourse(courseId);
        return new ResponseEntity<>(HttpStatus.OK);
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

    /**
     * Подробнее отдаем информацию по предмету
     */
    @Operation(summary = "Подробнее отдаем информацию по предмету")
    @RequestMapping(path = "{courseId}", method = RequestMethod.GET)
    public ResponseEntity<CourseResponse> findCourseDetailsById(@PathVariable Long courseId) {
        return new ResponseEntity<>(courseService.findCourseDetailsById(courseId), HttpStatus.OK);
    }
}