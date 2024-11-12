package ru.urfu.sv.studentvoice.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.urfu.sv.studentvoice.model.domain.entity.User;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    @Query(value = "select username from users", nativeQuery = true)
    List<String> findAllUsernames();

    User findByUsername(String username);
}