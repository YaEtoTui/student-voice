package ru.urfu.sv.studentvoice.services.mapper;

import org.springframework.stereotype.Component;
import ru.urfu.sv.studentvoice.model.domain.dto.response.PairResponse;
import ru.urfu.sv.studentvoice.model.domain.entity.ClassSession;

import java.util.List;
import java.util.stream.Collectors;

import static java.util.Objects.nonNull;

/**
 * Mapper по созданию PairResponse
 *
 * @author Egor Sazhin
 * @since 07.11.2024
 */
@Component
public class PairMapper {

    /**
     * Переделываем List<ClassSession> в List<PairResponse>
     *
     * @param classSessionList Список сессий
     */
    public List<PairResponse> createPairResponseList(List<ClassSession> classSessionList) {
        return classSessionList.stream()
                .map(this::createPairResponse)
                .collect(Collectors.toList());
    }

    /**
     * Переделываем ClassSession в PairResponse
     *
     * @param classSession Сессия
     */
    public PairResponse createPairResponse(ClassSession classSession) {
        final PairResponse pairResponse = new PairResponse();
        pairResponse.setStatus(classSession.getStatus());
        pairResponse.setCourseName(nonNull(classSession.getCourseDetails()) ? classSession.getCourseDetails().getCourseName() : null);
        pairResponse.setDateStart(classSession.getStartDateTime());
        pairResponse.setDateEnd(classSession.getEndDateTime());

        return pairResponse;
    }
}