package ru.urfu.sv.studentvoice.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.urfu.sv.studentvoice.controllers.links.Links;
import ru.urfu.sv.studentvoice.services.InstituteService;

@Slf4j
@RestController
@RequestMapping(Links.INSTITUTES)
@PreAuthorize("@RolesAC.isAdmin()")
public class InstituteController {

    @Autowired
    private InstituteService instituteService;

//    @PostMapping("/create")
//    public String createInstitute(HttpServletRequest request, Model model) {
//        String instituteFullName = request.getParameter(INSTITUTE_FULL_NAME);
//        String instituteShortName = request.getParameter(INSTITUTE_SHORT_NAME);
//        String instituteAddress = request.getParameter(INSTITUTE_ADDRESS);
//
//        ActionResult result = instituteService.createInstitute(instituteFullName, instituteShortName, instituteAddress);
//        model.addAttribute(RESULT, result);
//        return CREATE_INSTITUTE;
//    }
}
