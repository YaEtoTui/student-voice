package ru.urfu.sv.studentvoice.controllers.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.urfu.sv.studentvoice.controllers.links.Links;
import ru.urfu.sv.studentvoice.model.domain.dto.ScheduleByDay;
import ru.urfu.sv.studentvoice.model.domain.dto.response.LessonByCourseResponse;
import ru.urfu.sv.studentvoice.model.domain.dto.response.LessonDetailsResponse;
import ru.urfu.sv.studentvoice.model.domain.dto.response.LessonResponse;
import ru.urfu.sv.studentvoice.services.LessonService;
import ru.urfu.sv.studentvoice.services.ScheduleService;

import java.util.List;

@RestController
@SecurityRequirement(name = "Bearer Authentication")
@RequestMapping(Links.BASE_API + Links.LESSONS)
public class LessonController {

    @Autowired
    private LessonService lessonService;
    @Autowired
    private ScheduleService scheduleService;

    /**
     * Ищем список пар ПАГИНИРОВАННЫЙ у преподавателя
     */
    @Operation(summary = "Поиск пар у преподавателя")
    @RequestMapping(path = "/list", method = RequestMethod.GET)
    public ResponseEntity<Page<LessonResponse>> findLessonList(@RequestParam(required = false, name = "search-text") String searchText,
                                                               @PageableDefault(size = 10000) Pageable pageable) {
        return new ResponseEntity<>(lessonService.findLessonList(searchText,pageable), HttpStatus.OK);
    }

    /**
     * Ищем список пар ПАГИНИРОВАННЫЙ у преподавателя по предмету
     */
    @Operation(summary = "Поиск пар у преподавателя по предмету")
    @RequestMapping(path = "/list/by-course/{courseId}", method = RequestMethod.GET)
    public ResponseEntity<Page<LessonByCourseResponse>> findLessonListByCourse(@PathVariable Long courseId,
                                                                               @PageableDefault(size = 10000) Pageable pageable) {
        return new ResponseEntity<>(lessonService.findLessonListByCourseId(courseId, pageable), HttpStatus.OK);
    }

    /**
     * Подробнее отдаем информацию по паре
     */
    @Operation(summary = "Подробнее отдаем информацию по паре")
    @RequestMapping(path = "{lessonId}", method = RequestMethod.GET)
    public ResponseEntity<LessonDetailsResponse> findLessonById(@PathVariable Long lessonId) {
        return new ResponseEntity<>(lessonService.findLessonDetailsById(lessonId), HttpStatus.OK);
    }

    @Operation(summary = "Ищем расписание на три дня: сегодня, завтра и послезавтра")
    @RequestMapping(path = "/schedule-short", method = RequestMethod.GET)
    public ResponseEntity<List<ScheduleByDay>> findScheduleShort() {
        return new ResponseEntity<>(scheduleService.findScheduleShort(), HttpStatus.OK);
    }

    @Operation(summary = "Ищем расписание на текущую неделю")
    @RequestMapping(path = "/schedule", method = RequestMethod.GET)
    public ResponseEntity<List<ScheduleByDay>> findSchedule() {
        return new ResponseEntity<>(scheduleService.findSchedule(), HttpStatus.OK);
    }

    @Operation(summary = "Получение QR кода на пару")
    @RequestMapping(path = "/qr-code/{lessonId}", method = RequestMethod.GET)
    public ResponseEntity<String> getQrCode(@PathVariable Long lessonId) {
        return new ResponseEntity<>(lessonService.getQrCode(lessonId), HttpStatus.OK);
    }

    @Operation(summary = "Создаем таймер Qr-кода во время пары")
    @RequestMapping(path = "/start-timer/{lessonId}", method = RequestMethod.PUT)
    public ResponseEntity<Void> createStartTimer(@PathVariable Long lessonId,
                                                 @RequestParam(name = "hours") int hours,
                                                 @RequestParam(name = "minutes") int minutes) {
        lessonService.createDisableTimestamp(lessonId, hours, minutes);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}