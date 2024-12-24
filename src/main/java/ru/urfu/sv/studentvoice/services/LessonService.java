package ru.urfu.sv.studentvoice.services;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.PathBuilderFactory;
import com.querydsl.jpa.JPQLQuery;
import jakarta.persistence.EntityManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.Querydsl;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.urfu.sv.studentvoice.model.domain.dto.*;
import ru.urfu.sv.studentvoice.model.domain.dto.json.JLesson;
import ru.urfu.sv.studentvoice.model.domain.dto.lesson.LessonByCourse;
import ru.urfu.sv.studentvoice.model.domain.dto.lesson.LessonDetailsDto;
import ru.urfu.sv.studentvoice.model.domain.dto.lesson.LessonWithCourse;
import ru.urfu.sv.studentvoice.model.domain.dto.modeus.LessonModeus;
import ru.urfu.sv.studentvoice.model.domain.dto.response.LessonByCourseResponse;
import ru.urfu.sv.studentvoice.model.domain.dto.response.LessonDetailsResponse;
import ru.urfu.sv.studentvoice.model.domain.dto.response.LessonResponse;
import ru.urfu.sv.studentvoice.model.domain.entity.*;
import ru.urfu.sv.studentvoice.model.query.CourseQuery;
import ru.urfu.sv.studentvoice.model.query.LessonQuery;
import ru.urfu.sv.studentvoice.model.query.UserQuery;
import ru.urfu.sv.studentvoice.model.repository.LessonRepository;
import ru.urfu.sv.studentvoice.services.jwt.JwtUserDetailsService;
import ru.urfu.sv.studentvoice.services.mapper.LessonMapper;
import ru.urfu.sv.studentvoice.utils.exceptions.ModeusException;

import java.time.*;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
@Service
public class LessonService {

    @Autowired
    private CourseService courseService;
    @Autowired
    private InstituteService instituteService;
    @Autowired
    private ModeusService modeusService;
    @Autowired
    private QRCodeService qrCodeService;
    @Autowired
    private JwtUserDetailsService jwtUserDetailsService;
    @Autowired
    private LessonRepository lessonRepository;
    @Autowired
    private UserQuery userQuery;
    @Autowired
    private LessonQuery lessonQuery;
    @Autowired
    private CourseQuery courseQuery;
    @Autowired
    private LessonMapper lessonMapper;
    @Autowired
    protected EntityManager entityManager;

    @Value("${application.front.host}")
    private String applicationFrontHost;

    /**
     * Создание пары
     * @param jLesson Объект пары json
     */
    @Transactional
    @PreAuthorize("@LessonsAC.isCreateNewLesson(#jLesson)")
    public void createLesson(JLesson jLesson) {

        final Lesson lesson = new Lesson();
        lesson.setName(jLesson.getNameLesson());
        lesson.setCourseId(jLesson.getCourseId());
        lesson.setStartDateTime(jLesson.getStartDateTime());
        lesson.setEndDateTime(jLesson.getEndDateTime());
        lesson.setStatus(Status.PLANNED.getTitleStatus());

        if (jLesson.isFullTime()) {
            lesson.setAddress(jLesson.getAddress());
            lesson.setInstituteId(jLesson.getInstituteId());
            lesson.setCabinet(jLesson.getCabinet());
        } else {
            lesson.setCabinet(jLesson.getLink());
            lesson.setAddress("Дистант");

            final Long instituteId = courseQuery.findInstituteIdByCourseId(jLesson.getCourseId());
            lesson.setInstituteId(instituteId);
        }

        lessonRepository.save(lesson);
    }

    @Transactional
    public void updateLesson(Long lessonId, JLesson jLesson) {

        final Lesson lesson = lessonQuery.findLessonById(lessonId);
        lesson.setName(jLesson.getNameLesson());
        lesson.setCourseId(jLesson.getCourseId());
        lesson.setStartDateTime(jLesson.getStartDateTime());
        lesson.setEndDateTime(jLesson.getEndDateTime());
        lesson.setStatus(Status.PLANNED.getTitleStatus());

        if (jLesson.isFullTime()) {
            lesson.setAddress(jLesson.getAddress());
            lesson.setInstituteId(jLesson.getInstituteId());
            lesson.setCabinet(jLesson.getCabinet());
        } else {
            lesson.setCabinet(jLesson.getLink());
            lesson.setAddress("Дистант");
        }

        lessonRepository.save(lesson);
    }

    /**
     * Ищем список пар для преподавателя
     */
    @PreAuthorize("@RolesAC.isProfessor()")
    public Page<LessonResponse> findLessonList(String searchText, Pageable pageable) {
        final String username = jwtUserDetailsService.findUsername();
        final User professor = userQuery.findProfessorByUsername(username);

        if (Objects.nonNull(professor)) {
            final String professorName = professor.getUsername();

            final QCourse course = new QCourse("course");
            final QLesson lesson = new QLesson("lesson");

            final JPQLQuery<?> query = lessonQuery.findAllLessonsBySearchTextAndProfName(searchText, professorName);
            final long count = query.select(course.name).fetchCount();

            final JPQLQuery<?> queryPageable = new Querydsl(entityManager, new PathBuilderFactory().create(LessonWithCourse.class)).applyPagination(pageable, query);

            final List<LessonWithCourse> lessonWithCourseList = queryPageable.select(
                            Projections.bean(LessonWithCourse.class,
                                    course.id.as("courseId"),
                                    course.name.as("courseName"),
                                    lesson.id.as("lessonId"),
                                    lesson.status.as("status"),
                                    lesson.startDateTime.as("dateStart"),
                                    lesson.endDateTime.as("dateEnd"))
                    )
                    .fetch();

            final List<LessonResponse> lessonResponseList = lessonMapper.createLessonResponseListFromLessonWithCourseList(lessonWithCourseList);
            return new PageImpl<>(lessonResponseList, pageable, count);
        } else {
            throw new IllegalArgumentException("Not found professor");
        }
    }

    /**
     * Ищем список пар для преподавателя по предмету
     */
    @PreAuthorize("@RolesAC.isProfessor()")
    public Page<LessonByCourseResponse> findLessonListByCourseId(Long courseId, Pageable pageable) {
        final QCourse course = new QCourse("course");
        final QLesson lesson = new QLesson("lesson");

        final JPQLQuery<?> query = lessonQuery.findAllLessonsByCourseId(courseId);
        final long count = query.select(course.name).fetchCount();

        final JPQLQuery<?> queryPageable = new Querydsl(entityManager, new PathBuilderFactory().create(LessonWithCourse.class)).applyPagination(pageable, query);

        final List<LessonByCourse> lessonList = queryPageable.select(
                        Projections.bean(LessonByCourse.class,
                                course.name.as("courseName"),
                                lesson.id.as("lessonId"),
                                lesson.status.as("status"),
                                lesson.startDateTime.as("dateStart"),
                                lesson.endDateTime.as("dateEnd"))
                )
                .fetch();

        final List<LessonByCourseResponse> lessonResponseList = lessonMapper.createLessonResponseListFromLessonByCourseList(lessonList);
        return new PageImpl<>(lessonResponseList, pageable, count);
    }

    @PreAuthorize("@RolesAC.isProfessor()")
    public LessonDetailsResponse findLessonDetailsById(Long lessonId) {

        final LessonDetailsDto lessonDetailsDto = lessonQuery.findLessonDetailsById(lessonId);
        return lessonMapper.createLessonDetailsResponse(lessonDetailsDto);
    }

    /**
     * Создаем время истечения ссылки
     */
    @PreAuthorize("@RolesAC.isAdminOrProfessor()")
    @Transactional
    public void createDisableTimestamp(Long lessonId, int hours, int minutes) {

        final LocalDateTime startLesson = lessonQuery.findStartLessonById(lessonId);

        final Instant disableTimestamp = startLesson.atZone(ZoneId.systemDefault())
                .toInstant()
                .plus(Duration.ofHours(hours))
                .plus(Duration.ofMinutes(minutes));

        final LocalDateTime disableDate = LocalDateTime.ofInstant(disableTimestamp, ZoneId.systemDefault());
        lessonQuery.createDisableTimestamp(lessonId, disableDate);
    }

    /**
     * Получаем QR-соde
     */
    @PreAuthorize("@RolesAC.isAdminOrProfessor()")
    public String getQrCode(Long lessonId) {
        final String reviewUrl = String.format("%s/form/%d", applicationFrontHost, lessonId);
        return qrCodeService.getEncodedCode(reviewUrl, 256, 256);
    }

    /**
     * Подгружаем пары из Модеуса
     *
     * @param dateFrom Дата начало выгрузки
     * @param dateTo   Дата окончания выгрузки
     */
    @PreAuthorize("@RolesAC.isProfessor()")
    @Transactional
    public void importLessonsForModeus(LocalDate dateFrom, LocalDate dateTo) throws ModeusException {

        //TO DO пока препод имеет право, так как подгружаются пары для него (старая логика)
        final String username = jwtUserDetailsService.findUsername();
        final User professor = userQuery.findProfessorByUsername(username);

        final List<LessonAndCourseInfo> savedLessonList = lessonQuery.findAllLessonInfoByProfessorId(professor.getId())
                .stream()
                .collect(Collectors.toList());

        final List<String> savedLessonNameList = savedLessonList.stream()
                .map(LessonAndCourseInfo::getLessonName)
                .collect(Collectors.toList());
        final List<String> savedCourseNameList = savedLessonList.stream()
                .map(LessonAndCourseInfo::getCourseName)
                .collect(Collectors.toList());
        final List<String> savedInstituteNameList = savedLessonList.stream()
                .map(LessonAndCourseInfo::getInstituteName)
                .collect(Collectors.toList());

        final List<LessonModeus> lessonListFromModeus = modeusService.findJLessonListOfProfessor(professor, dateFrom, dateTo);

        /* To Do Тут поправить */
        final List<LessonModeus> unsavedLessonListFromModeus = lessonListFromModeus.stream()
                .filter(jLesson -> !savedLessonNameList.contains(jLesson.getName())
                        && !savedCourseNameList.contains(jLesson.getCourseName())
                        && !savedInstituteNameList.contains(jLesson.getInstituteName())
                )
                .collect(Collectors.toList());

        if (!unsavedLessonListFromModeus.isEmpty()) {
            final String newListLesson = String.join("\n", unsavedLessonListFromModeus.stream().map(LessonModeus::toString).toList());
            log.info("Для преподавателя найдены новые пары {}", newListLesson);
            final List<Institute> instituteList = instituteService.createInstitutesByJLessonList(unsavedLessonListFromModeus);
            final List<Course> courseList = courseService.createCoursesByJLessonList(professor.getId(), unsavedLessonListFromModeus);
            saveLessonList(unsavedLessonListFromModeus, courseList, instituteList);
        }

        final List<Lesson> allLessonList = lessonQuery.findAllLessonByProfessorId(professor.getId());
        final String lessonListAsText = String.join("\n", allLessonList.stream()
                .map(Lesson::toString)
                .collect(Collectors.toList()));

        log.info("Для преподавателя {} найдены следующие пары: {}", username, lessonListAsText);
    }

    private void saveLessonList(List<LessonModeus> lessonList, List<Course> courseList, List<Institute> instituteList) {

        for (final LessonModeus lessonModeus : lessonList) {

            final Lesson lesson = new Lesson();
            lesson.setName(lessonModeus.getName());
            lesson.setStatus(lessonModeus.getStatus());

            final Long courseId = courseList.stream()
                    .filter(course -> course.getName().equals(lessonModeus.getCourseName()))
                    .map(Course::getId)
                    .findFirst()
                    .orElse(null);
            if (Objects.nonNull(courseId)) {
                lesson.setCourseId(courseId);
            }

            final Long instituteId = instituteList.stream()
                    .filter(institute -> institute.getFullName().equals(lessonModeus.getInstituteName()))
                    .map(Institute::getId)
                    .findFirst()
                    .orElse(null);
            if (Objects.nonNull(instituteId)) {
                lesson.setInstituteId(instituteId);
            }

            lessonRepository.save(lesson);
        }
    }
}