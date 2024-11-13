package ru.urfu.sv.studentvoice.utils.model;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.ui.Model;
import ru.urfu.sv.studentvoice.model.domain.entity.Professor;
import ru.urfu.sv.studentvoice.services.ProfessorService;

import java.util.Optional;

import static ru.urfu.sv.studentvoice.utils.consts.Parameters.PROFESSOR_NAME;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ModelUtils {
//    public static void addProfessorName(ProfessorService professorService, Model model, UserDetails userDetails){
//        if(userDetails == null) return;
//
//        Optional<Professor> professorOpt = professorService.findProfessorByUsername(userDetails.getUsername());
//        if(professorOpt.isPresent()){
//            model.addAttribute(PROFESSOR_NAME, professorOpt.get().getFullName());
//        }
//    }

    public static Object orNull(Object value){
        return value != null ? value : "null";
    }
}
