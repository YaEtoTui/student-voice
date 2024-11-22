package ru.urfu.sv.studentvoice.services.user;

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
import org.springframework.stereotype.Service;
import ru.urfu.sv.studentvoice.model.domain.dto.response.PairResponse;
import ru.urfu.sv.studentvoice.model.domain.entity.QCourse;
import ru.urfu.sv.studentvoice.model.domain.entity.QLesson;
import ru.urfu.sv.studentvoice.model.domain.entity.User;
import ru.urfu.sv.studentvoice.model.domain.dto.lesson.LessonWithCourse;
import ru.urfu.sv.studentvoice.model.query.CourseQuery;
import ru.urfu.sv.studentvoice.model.query.UserQuery;
import ru.urfu.sv.studentvoice.services.ClassSessionService;
import ru.urfu.sv.studentvoice.services.CourseService;
import ru.urfu.sv.studentvoice.services.jwt.JwtUserDetailsService;
import ru.urfu.sv.studentvoice.services.mapper.PairMapper;

import java.util.List;
import java.util.Objects;

/**
 * Сервис предназначен для обработки данных для преподаваталя,
 * Это могут быть предметы, пары, сам преподаватель и т.д.
 *
 * @author Egor Sazhin
 * @since 07.11.2024
 */
@Slf4j
@Service
public class ProfessorService {

    @Autowired
    private CourseService courseService;
    @Autowired
    private ClassSessionService sessionService;
    @Autowired
    private JwtUserDetailsService jwtUserDetailsService;
    @Autowired
    private PairMapper pairMapper;
    @Autowired
    private UserQuery userQuery;
    @Autowired
    private CourseQuery courseQuery;
    @Autowired
    protected EntityManager entityManager;

//    public List<Professor> getAllProfessors() {
//        return repository.findAllByOrderByFullNameAsc();
//    }
//
//    @Transactional
//    public ActionResult createProfessor(String username, String professorName) {
//        Optional<Professor> professorOptByUsername = repository.findByUsername(username);
//        if (professorOptByUsername.isPresent()) {
//            Professor professor = professorOptByUsername.get();
//            log.warn("Профессор с username={} уже существует - {}", username, professor);
//            return ActionResultFactory.userExist(username);
//        }
//
//        Optional<Professor> professorOptByFullName = repository.findByFullNameIgnoreCase(professorName);
//        if (professorOptByFullName.isPresent()) {
//            Professor professor = professorOptByFullName.get();
//            log.warn("Профессор с именем {} уже существует - {}", professorName, professor);
//            return ActionResultFactory.professorExist();
//        }
//
//        Professor professor = repository.save(new Professor(username, professorName));
//        return repository.existsById(professor.getProfessorId()) ? ActionResultFactory.professorCreated() : ActionResultFactory.professorCreatingError();
//    }
//
////    @Transactional
////    public ActionResult updateProfessor(String username, String newProfessorName) {
////        Optional<Professor> professor = findProfessorByUsername(username);
////        if (professor.isEmpty()) {
////            return ActionResultFactory.professorNotExist(username);
////        }
////        String oldProfessorName = professor.get().getFullName();
////        professor.get().setFullName(newProfessorName);
////        repository.save(professor.get());
////
////        courseService.updateProfessor(oldProfessorName, newProfessorName);
////        sessionService.updateProfessorName(oldProfessorName, newProfessorName);
////
////        return ActionResultFactory.professorCreated();
////    }
//
//    public Optional<Professor> findProfessorByUsername(String username) {
//        return repository.findByUsername(username);
//    }

    /**
     * Ищем список пар для преподавателя
     */
    public Page<PairResponse> findListPair(Pageable pageable) {
        final String username = jwtUserDetailsService.findUsername();
        final User professor = userQuery.findProfessorByUsername(username);

        if (Objects.nonNull(professor)) {
            final String professorName = professor.getUsername();

            final QCourse course = new QCourse("course");
            final QLesson lesson = new QLesson("lesson");

            final JPQLQuery<?> query = courseQuery.findAllLessonsByProfessorUsername(professorName);
            final long count = query.select(course.name).fetchCount();

            final JPQLQuery<?> queryPageable = new Querydsl(entityManager, new PathBuilderFactory().create(LessonWithCourse.class)).applyPagination(pageable, query);

            final List<LessonWithCourse> lessonWithCourseList = queryPageable.select(
                            Projections.bean(LessonWithCourse.class,
                                    /* Статус добавить */
                                    course.name.as("courseName"),
                                    lesson.startDateTime.as("dateStart"),
                                    lesson.endDateTime.as("dateEnd"))
                    )
                    .fetch();

            final List<PairResponse> pairResponseList = pairMapper.createPairResponseListFromLessonWithCourseList(lessonWithCourseList);
            return new PageImpl<>(pairResponseList, pageable, count);
        } else {
            throw new IllegalArgumentException("Not found professor");
        }
    }
}