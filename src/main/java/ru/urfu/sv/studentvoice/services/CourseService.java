package ru.urfu.sv.studentvoice.services;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.PathBuilderFactory;
import com.querydsl.jpa.JPQLQuery;
import jakarta.persistence.EntityManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.Querydsl;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.urfu.sv.studentvoice.model.domain.dto.course.CourseDto;
import ru.urfu.sv.studentvoice.model.domain.dto.course.CourseInfo;
import ru.urfu.sv.studentvoice.model.domain.dto.course.CourseRedaction;
import ru.urfu.sv.studentvoice.model.domain.dto.lesson.LessonWithCourse;
import ru.urfu.sv.studentvoice.model.domain.dto.response.CourseResponse;
import ru.urfu.sv.studentvoice.model.domain.entity.*;
import ru.urfu.sv.studentvoice.model.query.CourseQuery;
import ru.urfu.sv.studentvoice.model.query.UserQuery;
import ru.urfu.sv.studentvoice.model.repository.CourseRepository;
import ru.urfu.sv.studentvoice.services.jwt.JwtUserDetailsService;
import ru.urfu.sv.studentvoice.services.mapper.CourseMapper;

import java.util.List;
import java.util.Objects;

@Slf4j
@Service
public class CourseService {

    @Autowired
    private JwtUserDetailsService jwtUserDetailsService;
    @Autowired
    private CourseRepository courseRepository;
    @Autowired
    private UserQuery userQuery;
    @Autowired
    private CourseQuery courseQuery;
    @Autowired
    protected EntityManager entityManager;
    @Autowired
    private CourseMapper courseMapper;

    @Transactional
    @PreAuthorize("@CoursesAC.isCreateNewCourse(#courseInfo)")
    public void createCourse(CourseInfo courseInfo) {

        final Course course = new Course();
        course.setName(courseInfo.getCourseName());
        course.setAddress(courseInfo.getAddress());
        course.setInstituteId(courseInfo.getInstituteId());

        /* Тут проверить постоянную ссылку */

        final Course courseResponse = courseRepository.save(course);
        courseQuery.insertUserCourse(courseInfo.getProfessorId(), courseResponse.getId());
    }

    @Transactional
    @PreAuthorize("@RolesAC.isProfessor()")
    public Page<CourseResponse> findCourseList(String searchText, Pageable pageable) {
        final String username = jwtUserDetailsService.findUsername();
        final User professor = userQuery.findProfessorByUsername(username);

        if (Objects.nonNull(professor)) {
            final String professorName = professor.getUsername();

            final QCourse course = new QCourse("course");
            final QInstitute institute = new QInstitute("institute");

            final JPQLQuery<?> query = courseQuery.findAllCourseBySearchTextAndProfName(searchText, professorName);
            final long count = query.select(course.name).fetchCount();

            final JPQLQuery<?> queryPageable = new Querydsl(entityManager, new PathBuilderFactory().create(LessonWithCourse.class)).applyPagination(pageable, query);

            final List<CourseDto> courseList = queryPageable.select(
                            Projections.bean(CourseDto.class,
                                    course.id.as("courseId"),
                                    course.name.as("name"),
                                    institute.address.as("address")
                            )
                    )
                    .fetch();

            final List<CourseResponse> courseResponseList = courseMapper.createCourseResponseList(courseList);
            return new PageImpl<>(courseResponseList, pageable, count);
        } else {
            throw new IllegalArgumentException("Not found professor");
        }
    }

    @PreAuthorize("@RolesAC.isProfessor()")
    public CourseResponse findCourseDetailsById(Long courseId) {
        return courseQuery.findCourseDetailsById(courseId);
    }

//    @Transactional
//    public void createCoursesByClassSessions(List<ClassSession> sessions) {
//        for (ClassSession session : sessions) {
//            if (!repository.existsById(session.getCourseId())) {
//                ActionResult result = createCourse(session.getCourseId(), session.getCourseDetails());
//                if (!result.isSuccess()) {
//                    log.error("Предмет {} не создался - {}", session.getCourseDetails().getCourseName(), result.getFormattedMessage());
//                } else {
//                    log.info("Создался новый предмет - {}", repository.findById(session.getCourseId()));
//                }
//            }
//        }
//    }
//
//    @Transactional
//    public ActionResult updateProfessor(String professorName, String newProfessorName){
//        List<Course> courses = findCoursesByProfessorName(professorName);
//        courses.forEach(course -> {
//            String names = course.getCourseDetails().getProfessorsNames();
//            if(names.contains(professorName)){
//                course.getCourseDetails().setProfessorsNames(names.replace(professorName, newProfessorName));
//                repository.save(course);
//            }
//        });
//
//        return new ActionResult(true, "Предметы успешно обновлены");
//    }

//    public Optional<Course> findCourseByName(String courseName) {
//        return repository.findByCourseDetails_CourseName(courseName);
//    }
//
//    public Optional<Course> findCourseById(UUID courseId) {
//        return repository.findById(courseId);
//    }
//
//    public Optional<Course> findCourseBySession(ClassSession classSession) {
//        return repository.findById(classSession.getCourseId());
//    }
//
//    public List<Course> findCoursesByProfessorName(String professorName) {
//        List<Course> courses = repository.findAllByCourseDetails_ProfessorsNamesContainsIgnoreCase(professorName);
//        String coursesList = String.join("\n", courses.stream().map(Course::toString).toList());
//        log.info("Для профессора {} найдены следующие предметы: {}", professorName, coursesList);
//        return courses;
//    }
//
//    public List<Course> findCoursesByInstituteName(String instituteShortName) {
//        return repository.findAllByCourseDetails_InstituteName(instituteShortName);
//    }

    public List<Course> findAll() {
        return courseRepository.findAll();
    }

    @PreAuthorize("@RolesAC.isAdminOrProfessor()")
    public void updateCourse(Long courseId, CourseRedaction courseInfo) {
        courseQuery.updateCourse(courseId, courseInfo);
    }
}