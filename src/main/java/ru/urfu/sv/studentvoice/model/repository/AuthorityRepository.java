package ru.urfu.sv.studentvoice.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.urfu.sv.studentvoice.model.domain.entity.Authority;

@Repository
public interface AuthorityRepository extends JpaRepository<Authority, String> {

    Authority findAuthorityByUsername(String username);
}