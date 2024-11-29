package ru.urfu.sv.studentvoice.services.mapper;

import org.springframework.stereotype.Component;
import ru.urfu.sv.studentvoice.model.domain.dto.response.ProfessorResponse;
import ru.urfu.sv.studentvoice.model.domain.dto.user.Professor;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class ProfessorMapper {

    public List<ProfessorResponse> createListProfessorResponse(Collection<Professor> professorCollection) {
        return professorCollection.stream()
                .map(this::createProfessorResponse)
                .collect(Collectors.toList());
    }

    public ProfessorResponse createProfessorResponse(Professor professor) {
        final ProfessorResponse professorResponse = new ProfessorResponse();
        professorResponse.setProfessorId(professor.getProfessorId());
        final String fio = String.format("%s %s %s",
                professor.getProfessorSurname(), professor.getProfessorName(), professor.getProfessorPatronymic()
        );
        professorResponse.setProfessorFio(fio);

        return professorResponse;
    }
}