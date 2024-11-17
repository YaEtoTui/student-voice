package ru.urfu.sv.studentvoice.controllers.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.urfu.sv.studentvoice.controllers.InstituteController;
import ru.urfu.sv.studentvoice.controllers.links.Links;
import ru.urfu.sv.studentvoice.model.domain.entity.Institute;
import ru.urfu.sv.studentvoice.services.InstituteService;

import java.util.List;
import java.util.Map;

import static ru.urfu.sv.studentvoice.utils.consts.Parameters.INSTITUTES_LIST;

@RestController
@RequestMapping(Links.BASE_API + Links.INSTITUTES)
@PreAuthorize("@RolesAC.isAdmin")
public class InstituteApiController {

    @Autowired
    private InstituteController instituteController;
    @Autowired
    private InstituteService instituteService;

//    @PostMapping("create")
//    @Parameters(value = {
//            @Parameter(name = "instituteFullName", in = ParameterIn.QUERY, required = true),
//            @Parameter(name = "instituteShortName", in = ParameterIn.QUERY, required = true),
//            @Parameter(name = "instituteAddress", in = ParameterIn.QUERY, required = true)
//    })
//    public ResponseEntity<Map<String, Object>> create(HttpServletRequest request) {
//        ExtendedModelMap model = new ExtendedModelMap();
//        instituteController.createInstitute(request, model);
//
//        ActionResultResponse result = fromActionResult(model.getAttribute(RESULT));
//        return ResponseEntity.ok().body(Map.of(RESULT, result));
//    }

    @GetMapping("list")
    public ResponseEntity<Map<String, Object>> list() {
        List<Institute> institutes = instituteService.findAllInstitutes();
        return ResponseEntity.ok().body(Map.of(INSTITUTES_LIST, institutes));
    }
}