package ru.urfu.sv.studentvoice.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.urfu.sv.studentvoice.model.domain.dto.institute.InstituteDto;
import ru.urfu.sv.studentvoice.model.domain.dto.institute.InstituteInfo;
import ru.urfu.sv.studentvoice.model.domain.dto.json.JLesson;
import ru.urfu.sv.studentvoice.model.domain.dto.response.InstituteResponse;
import ru.urfu.sv.studentvoice.model.domain.entity.Institute;
import ru.urfu.sv.studentvoice.model.query.InstituteQuery;
import ru.urfu.sv.studentvoice.model.repository.InstituteRepository;
import ru.urfu.sv.studentvoice.services.mapper.InstituteMapper;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class InstituteService {

    @Autowired
    private InstituteRepository instituteRepository;
    @Autowired
    private InstituteMapper instituteMapper;
    @Autowired
    private InstituteQuery instituteQuery;

    @Transactional
    @PreAuthorize("@InstitutesAC.isCreateNewInstitute(#instituteInfo.instituteFullName)")
    public void createInstitute(InstituteInfo instituteInfo) {

        final Institute institute = new Institute();
        institute.setFullName(instituteInfo.getInstituteFullName());
        institute.setShortName(instituteInfo.getInstituteShortName());
        institute.setAddress(instituteInfo.getInstituteAddress());
        instituteRepository.save(institute);
    }

    @Transactional
    public List<InstituteDto> findAllInstitutes() {
        final List<Institute> instituteList = instituteQuery.findAllInstituteList();
        return instituteMapper.createInstituteDtoListFromInstituteList(instituteList);
    }

    @PreAuthorize("@RolesAC.isAdminOrProfessor()")
    public List<InstituteResponse> findAllInstituteResponse() {
        final List<InstituteDto> instituteDtoList = findAllInstitutes();
        return instituteMapper.createInstituteResponseListFromInstituteDtoList(instituteDtoList);
    }

    @Transactional
    public List<String> findAllAddress() {
        return instituteQuery.findAllAddress();
    }

    @Transactional
    public void createInstitutesByJLessonList(List<JLesson> lessonList) {

        final List<Institute> instituteList = new ArrayList<>();
        for (final JLesson lesson : lessonList) {
            final String address = lesson.getAddress();
            if (!instituteQuery.isExistInstituteByAddress(address)) {

                final Institute institute = new Institute();
                institute.setFullName(lesson.getInstituteName());
                institute.setAddress(address);

                instituteList.add(institute);
            }
        }

        instituteRepository.saveAll(instituteList);
    }
}