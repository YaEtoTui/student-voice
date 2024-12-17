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
import ru.urfu.sv.studentvoice.model.domain.dto.Status;
import ru.urfu.sv.studentvoice.model.domain.dto.json.JLesson;
import ru.urfu.sv.studentvoice.model.domain.dto.lesson.LessonByCourse;
import ru.urfu.sv.studentvoice.model.domain.dto.lesson.LessonDetailsDto;
import ru.urfu.sv.studentvoice.model.domain.dto.lesson.LessonWithCourse;
import ru.urfu.sv.studentvoice.model.domain.dto.response.LessonByCourseResponse;
import ru.urfu.sv.studentvoice.model.domain.dto.response.LessonDetailsResponse;
import ru.urfu.sv.studentvoice.model.domain.dto.response.LessonResponse;
import ru.urfu.sv.studentvoice.model.domain.entity.Lesson;
import ru.urfu.sv.studentvoice.model.domain.entity.QCourse;
import ru.urfu.sv.studentvoice.model.domain.entity.QLesson;
import ru.urfu.sv.studentvoice.model.domain.entity.User;
import ru.urfu.sv.studentvoice.model.query.LessonQuery;
import ru.urfu.sv.studentvoice.model.query.UserQuery;
import ru.urfu.sv.studentvoice.model.repository.LessonRepository;
import ru.urfu.sv.studentvoice.services.jwt.JwtUserDetailsService;
import ru.urfu.sv.studentvoice.services.mapper.LessonMapper;

import java.time.*;
import java.util.List;
import java.util.Objects;

@Slf4j
@Service
public class LessonService {

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
    private LessonMapper lessonMapper;
    @Autowired
    protected EntityManager entityManager;

    @Value("${application.host}")
    private String applicationHost;

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
//        final String reviewUrl = "%s/reviews/create?lessonId=%s".formatted(applicationHost);
        final String reviewUrl = "%s/swagger-ui/index.html".formatted(applicationHost);
        return qrCodeService.getEncodedCode(reviewUrl, 256, 256);
    }
}