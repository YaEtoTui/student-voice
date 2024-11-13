package ru.urfu.sv.studentvoice.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.urfu.sv.studentvoice.model.domain.entity.Professor;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ProfessorRepository {
    Optional<Professor> findByFullNameIgnoreCase(String professorName);
    Optional<Professor> findByUsername(String username);

    List<Professor> findAllByOrderByFullNameAsc();
}
