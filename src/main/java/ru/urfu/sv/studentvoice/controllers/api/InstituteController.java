package ru.urfu.sv.studentvoice.controllers.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import ru.urfu.sv.studentvoice.controllers.links.Links;
import ru.urfu.sv.studentvoice.model.domain.dto.institute.InstituteInfo;
import ru.urfu.sv.studentvoice.model.domain.dto.response.InstituteResponse;
import ru.urfu.sv.studentvoice.services.InstituteService;

import java.util.List;

@Slf4j
@RestController
@SecurityRequirement(name = "Bearer Authentication")
@RequestMapping(Links.BASE_API + Links.INSTITUTES)
@PreAuthorize("@RolesAC.isAdmin")
public class InstituteController {

    @Autowired
    private InstituteService instituteService;

    @Operation(summary = "Создание института")
    @RequestMapping(path = "/create", method = RequestMethod.POST)
    public ResponseEntity<Void> createInstitute(@RequestBody InstituteInfo instituteInfo) {

        instituteService.createInstitute(instituteInfo);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

//    @Operation(summary = "Вывод списка институтов (всех в БД)")
//    @RequestMapping(path = "/list", method = RequestMethod.GET)
//    public ResponseEntity<List<InstituteResponse>> getInstituteList() {
//        final List<InstituteResponse> institutes = instituteService.findAllInstituteResponse();
//        return new ResponseEntity<>(institutes, HttpStatus.OK);
//    }
}