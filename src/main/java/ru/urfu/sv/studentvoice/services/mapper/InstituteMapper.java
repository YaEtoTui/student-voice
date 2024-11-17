package ru.urfu.sv.studentvoice.services.mapper;

import org.springframework.stereotype.Component;
import ru.urfu.sv.studentvoice.model.domain.dto.institute.InstituteDto;
import ru.urfu.sv.studentvoice.model.domain.dto.response.InstituteResponse;
import ru.urfu.sv.studentvoice.model.domain.entity.Institute;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class InstituteMapper {

    /**
     * Переделываем List<Institute> в List<InstituteDto>
     *
     * @param instituteCollection Список институтов
     */
    public List<InstituteDto> createInstituteDtoListFromInstituteList(Collection<Institute> instituteCollection) {
        return instituteCollection.stream()
                .map(this::createInstituteDto)
                .collect(Collectors.toList());
    }

    /**
     * Переделываем Institute в InstituteDto
     *
     * @param institute Институт
     */
    public InstituteDto createInstituteDto(Institute institute) {
        final InstituteDto instituteDto = new InstituteDto();
        instituteDto.setInstituteFullName(institute.getFullName());
        instituteDto.setInstituteShortName(institute.getShortName());
        instituteDto.setInstituteAddress(institute.getAddress());

        return instituteDto;
    }

    /**
     * Переделываем List<InstituteDto> в List<InstituteResponse>
     *
     * @param instituteDtoCollection Список институтов
     */
    public List<InstituteResponse> createInstituteResponseListFromInstituteDtoList(Collection<InstituteDto> instituteDtoCollection) {
        return instituteDtoCollection.stream()
                .map(this::createInstituteResponse)
                .collect(Collectors.toList());
    }

    /**
     * Переделываем InstituteDto в InstituteResponse
     *
     * @param instituteDto Институт
     */
    public InstituteResponse createInstituteResponse(InstituteDto instituteDto) {
        final InstituteResponse instituteResponse = new InstituteResponse();
        instituteResponse.setInstituteFullName(instituteDto.getInstituteFullName());
        instituteResponse.setInstituteAddress(instituteDto.getInstituteAddress());

        return instituteResponse;
    }
}