package ru.urfu.sv.studentvoice.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import ru.urfu.sv.studentvoice.model.domain.dto.institute.InstituteDto;
import ru.urfu.sv.studentvoice.model.domain.dto.modeus.LessonModeus;
import ru.urfu.sv.studentvoice.model.domain.entity.User;
import ru.urfu.sv.studentvoice.utils.exceptions.ModeusException;
import ru.urfu.sv.studentvoice.utils.formatters.TemporalFormatter;
import ru.urfu.sv.studentvoice.utils.model.LessonModeusMapper;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
public class ModeusService {

    @Value("${modeus.persons.url}")
    private String personSearchUrl;
    @Value("${modeus.events.url}")
    private String eventsSearchUrl;

    @Autowired
    private WebDriverService webDriverService;
    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private InstituteService instituteService;
    @Autowired
    private ObjectMapper objectMapper = new ObjectMapper();

    /**
     * Ищем пары на определенный промежуток, подгружая из Модеуса
     *
     * @param professor Преподаватель
     * @param dateFrom  Дата начала подгрузки пар
     * @param dateTo    Дата окончания подгрузки пар
     * @return Возвращаем список пар для преподаваталея
     */
    public List<LessonModeus> findJLessonListOfProfessor(User professor, LocalDate dateFrom, LocalDate dateTo) throws ModeusException {

        final Optional<String> modeusAuthToken = webDriverService.getModeusAuthToken();
        if (modeusAuthToken.isEmpty()) {
            log.error("Не получилось получить код авторизации Модеус");
            return Collections.emptyList();
        }

        final String professorFullName = String.format("%s %s %s", professor.getSurname(), professor.getName(), professor.getPatronymic());
        final Optional<String> professorModeusId = getProfessorModeusId(professorFullName, modeusAuthToken.get());
        if (professorModeusId.isEmpty()) {
            log.warn("Не получилось найти преподавателя {}", professorFullName);
            return Collections.emptyList();
        }

        final Optional<String> eventsJson = getEventsJson(professorModeusId.get(), dateFrom, dateTo, modeusAuthToken.get());
        if (eventsJson.isEmpty() || !LessonModeusMapper.hasEvents(eventsJson.get())) {
            log.warn("Не получилось найти пары для преподавателя {}", professorFullName);
            return Collections.emptyList();
        }

        final Map<String, String> institutesAddressNameMap = instituteService.findAllInstitutes()
                .stream()
                .collect(Collectors.toMap(InstituteDto::getInstituteAddress, InstituteDto::getInstituteShortName));

        return LessonModeusMapper.findFromEventsJson(eventsJson.get(), professor, institutesAddressNameMap);
    }

    /**
     * Получаем ответ из Модеуса
     *
     * @param url             URL запроса
     * @param body            Тело с данными
     * @param modeusAuthToken Токен авторизации
     */
    private Optional<String> getResponseFromModeus(String url, String body, String modeusAuthToken) {

        final HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(modeusAuthToken);
        final HttpEntity<String> entity = new HttpEntity<>(body, headers);

        final ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, entity, String.class);

        if (response.getStatusCode().isError()) {
            log.error("Ошибка при получения данных из Модеуса - {}, {}", response.getStatusCode().value(), response.getBody());
            return Optional.empty();
        }

        return response.getBody() == null ? Optional.empty() : Optional.of(response.getBody());
    }

    private Optional<String> getEventsJson(String professorModeusId, LocalDate dateFrom, LocalDate dateTo, String modeusAuthToken) {
        final String body = jsonForEventsSearch(professorModeusId, dateFrom, dateTo.plusDays(1));
        return getResponseFromModeus(eventsSearchUrl, body, modeusAuthToken);
    }

    private String jsonForEventsSearch(String professorModeusId, LocalDate dateFrom, LocalDate dateTo) {
        final Map<String, Object> bodyMap = Map.ofEntries(
                Map.entry("size", 500),
                Map.entry("timeMin", TemporalFormatter.formatToOffsetDateTime(dateFrom)),
                Map.entry("timeMax", TemporalFormatter.formatToOffsetDateTime(dateTo)),
                Map.entry("attendeePersonId", Collections.singletonList(professorModeusId))
        );

        try {
            return objectMapper.writeValueAsString(bodyMap);
        } catch (JsonProcessingException e) {
            log.error("Ошибка создания json", e);
            return "";
        }
    }

    private Optional<String> getProfessorModeusId(String professorFullName, String modeusAuthToken) {
        final String body = jsonForPersonSearch(professorFullName);
        final Optional<String> response = getResponseFromModeus(personSearchUrl, body, modeusAuthToken);

        if (response.isEmpty()) {
            return Optional.empty();
        }

        JsonNode root = null;
        try {
            root = objectMapper.readTree(response.get());
            return Optional.of(root.get("_embedded").get("persons").get(0).get("id").asText());
        } catch (JsonProcessingException | NullPointerException e) {
            log.error("Ошибка во время обработки ответа от модеуса", e);
            return Optional.empty();
        }
    }

    private String jsonForPersonSearch(String fullName) {
        final Map<String, Object> bodyMap = Map.ofEntries(
                Map.entry("fullName", fullName),
                Map.entry("sort", "+fullName"),
                Map.entry("size", 2147483647)
        );

        try {
            return objectMapper.writeValueAsString(bodyMap);
        } catch (JsonProcessingException e) {
            log.error("Ошибка создания json", e);
            return "";
        }
    }
}