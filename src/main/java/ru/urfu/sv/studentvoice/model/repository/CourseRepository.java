package ru.urfu.sv.studentvoice.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.urfu.sv.studentvoice.model.domain.entity.Course;
import ru.urfu.sv.studentvoice.model.domain.dto.CourseDetails;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface CourseRepository extends JpaRepository<Course, UUID> {

    boolean existsByCourseDetails_CourseName(String courseName);
    boolean existsByCourseDetails(CourseDetails courseDetails);

    Optional<Course> findByCourseDetails_CourseName(String courseName);
    List<Course> findAllByCourseDetails_InstituteName(String instituteName);

    List<Course> findAllByCourseDetails_ProfessorsNamesContainsIgnoreCase(String professorName);
}
