package ru.urfu.sv.studentvoice.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.urfu.sv.studentvoice.model.domain.entity.Lesson;

@Repository
public interface LessonRepository extends JpaRepository<Lesson, Long> {
}