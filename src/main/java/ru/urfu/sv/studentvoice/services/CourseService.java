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
import ru.urfu.sv.studentvoice.model.domain.dto.modeus.LessonModeus;
import ru.urfu.sv.studentvoice.model.domain.dto.lesson.LessonWithCourse;
import ru.urfu.sv.studentvoice.model.domain.dto.response.CourseResponse;
import ru.urfu.sv.studentvoice.model.domain.entity.*;
import ru.urfu.sv.studentvoice.model.query.CourseQuery;
import ru.urfu.sv.studentvoice.model.query.InstituteQuery;
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
    private InstituteQuery instituteQuery;
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

    @Transactional
    @PreAuthorize("@RolesAC.isProfessor()")
    public CourseResponse findCourseDetailsById(Long courseId) {
        return courseQuery.findCourseDetailsById(courseId);
    }

    @Transactional
    public void createCoursesByJLessonList(Long professorId, List<LessonModeus> lessonList) {

        final List<Institute> instituteList = instituteQuery.findAllIds();

        for (final LessonModeus lessonModeus : lessonList) {
            if (!courseQuery.isExistCourseByProfId(professorId, lessonModeus.getCourseName())) {

                final Long instituteId = instituteList.stream()
                        .filter(institute -> institute.getFullName().equals(lessonModeus.getInstituteName())
                                && institute.getAddress().equals(lessonModeus.getAddress()))
                        .map(AbstractEntity::getId)
                        .findFirst()
                        .orElse(null);

                final Course course = new Course();
                course.setName(lessonModeus.getCourseName());
                course.setAddress(lessonModeus.getAddress());
                course.setInstituteId(instituteId);

                final Course courseResponse = courseRepository.save(course);
                courseQuery.insertUserCourse(professorId, courseResponse.getId());
            }
        }
    }
}