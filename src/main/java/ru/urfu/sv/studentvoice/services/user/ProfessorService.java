package ru.urfu.sv.studentvoice.services.user;

import lombok.extern.slf4j.Slf4j;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.urfu.sv.studentvoice.model.domain.entity.User;
import ru.urfu.sv.studentvoice.model.query.UserQuery;
import ru.urfu.sv.studentvoice.services.LessonService;
import ru.urfu.sv.studentvoice.services.CourseService;
import ru.urfu.sv.studentvoice.services.jwt.JwtUserDetailsService;

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
    private LessonService sessionService;
    @Autowired
    private UserQuery userQuery;
    @Autowired
    private JwtUserDetailsService jwtUserDetailsService;

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

    public User findProfessor() {
        final String username = jwtUserDetailsService.findUsername();
        return userQuery.findProfessorByUsername(username);
    }
}