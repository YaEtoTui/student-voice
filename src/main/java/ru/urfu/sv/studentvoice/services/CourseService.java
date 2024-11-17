package ru.urfu.sv.studentvoice.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.urfu.sv.studentvoice.model.domain.dto.course.CourseInfo;
import ru.urfu.sv.studentvoice.model.domain.entity.Course;
import ru.urfu.sv.studentvoice.model.query.CourseQuery;
import ru.urfu.sv.studentvoice.model.repository.CourseRepository;

import java.util.List;

@Slf4j
@Service
public class CourseService {

    @Autowired
    private CourseRepository courseRepository;
    @Autowired
    private CourseQuery courseQuery;

    @Transactional
    @PreAuthorize("@CoursesAC.isCreateNewCourse(#courseInfo)")
    public void createCourse(CourseInfo courseInfo) {

        final Course course = new Course();
        course.setName(courseInfo.getCourseName());
        course.setInstituteId(courseInfo.getInstituteId());

        final Course courseResponse = courseRepository.save(course);
        courseQuery.insertUserCourse(courseInfo.getProfessorId(), courseResponse.getId());
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
}