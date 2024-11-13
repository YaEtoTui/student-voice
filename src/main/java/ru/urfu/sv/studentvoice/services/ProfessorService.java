package ru.urfu.sv.studentvoice.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.urfu.sv.studentvoice.model.domain.dto.response.PairResponse;
import ru.urfu.sv.studentvoice.model.domain.entity.ClassSession;
import ru.urfu.sv.studentvoice.model.domain.entity.Professor;
import ru.urfu.sv.studentvoice.model.repository.ProfessorRepository;
import ru.urfu.sv.studentvoice.services.jwt.JwtUserDetailsService;
import ru.urfu.sv.studentvoice.services.mapper.PairMapper;
import ru.urfu.sv.studentvoice.utils.result.ActionResult;
import ru.urfu.sv.studentvoice.utils.result.ActionResultFactory;

import java.util.List;
import java.util.Optional;

/**
 * Сервис предназначен для обработки данных для преподаваталя,
 * Это могут быть предметы, пары, сам преподаватель и т.д.
 *
 * @author Egor Sazhin
 * @since 07.11.2024
 */
@Service
@Slf4j
public class ProfessorService {

//    @Autowired
//    private ProfessorRepository repository;
//    @Autowired
//    private CourseService courseService;
//    @Autowired
//    private ClassSessionService sessionService;
//    @Autowired
//    private JwtUserDetailsService jwtUserDetailsService;
//    @Autowired
//    private ClassSessionService classSessionService;
//    @Autowired
//    private PairMapper pairMapper;

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
//
//    /**
//     * Ищем список пар для преподавателя
//     */
//    public List<PairResponse> findListPair() {
//        final String username = jwtUserDetailsService.findUsername();
//        final Optional<Professor> professor = findProfessorByUsername(username);
//
//        if (professor.isPresent()) {
//            final String professorName = professor.get().getUsername();
//            final List<ClassSession> classSessionList = classSessionService.findAllSavedClassSessionsByProfessorName(professorName);
//            return pairMapper.createPairResponseList(classSessionList);
//        } else {
//            throw new IllegalArgumentException("Not found professor");
//        }
//    }
}