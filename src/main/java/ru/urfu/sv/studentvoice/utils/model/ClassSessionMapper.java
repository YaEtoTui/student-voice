package ru.urfu.sv.studentvoice.utils.model;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ru.urfu.sv.studentvoice.model.domain.dto.FullAddress;
import ru.urfu.sv.studentvoice.model.domain.entity.ClassSession;
import ru.urfu.sv.studentvoice.model.domain.dto.CourseDetails;
import ru.urfu.sv.studentvoice.model.domain.entity.Lesson;
import ru.urfu.sv.studentvoice.utils.exceptions.ModeusException;
import ru.urfu.sv.studentvoice.utils.formatters.TemporalFormatter;

import java.time.Instant;
import java.util.*;

@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ClassSessionMapper {

    private static final ObjectMapper mapper = new ObjectMapper();
    private static final String DISTANT_SESSION = "Дистант";

    public static List<ClassSession> fromEventsJson(String eventsJson, String professorFullName, Map<String, String> instituteAddressNameMap) throws ModeusException {
        try {
            JsonNode root = mapper.readTree(eventsJson).get("_embedded");
            return getSessions(root, professorFullName, instituteAddressNameMap);
        } catch (Exception e) {
            log.error("Ошибка во время чтения json пар", e);
            throw new ModeusException("Во время обработки ответа от Модеуса произошла ошибка");
        }
    }

    public static boolean hasEvents(String eventsJson) throws ModeusException {
        try {
            JsonNode root = mapper.readTree(eventsJson).get("_embedded");
            return root.get("events") != null && root.get("events").elements().hasNext();
        } catch (Exception e) {
            log.error("Ошибка во время чтения json пар", e);
            throw new ModeusException("Во время обработки ответа от Модеуса произошла ошибка");
        }
    }

    private static List<Lesson> getSessions(JsonNode root, String professorFullName, Map<String, String> instituteAddressNameMap) {
        final Map<UUID, String> coursesNameMap = findCourseMap(root);
        final Map<UUID, FullAddress> addressMap = findAddressMap(root);
        final Iterator<JsonNode> eventNodes = root.get("events").elements();
        final List<Lesson> result = new ArrayList<>();
        while (eventNodes.hasNext()) {
            final JsonNode eventNode = eventNodes.next();
            final Lesson currentLesson = new Lesson();

            ClassSession current = new ClassSession();
//            current.setSessionId(UUID.fromString(eventNode.get("id").asText()));

            currentLesson.setName(eventNode.get("name").asText());
            currentLesson.setStartDateTime(TemporalFormatter.fromLocalDateTimeString(eventNode.get("startsAtLocal").asText()));
            currentLesson.setEndDateTime(TemporalFormatter.fromLocalDateTimeString(eventNode.get("endsAtLocal").asText()));

            final String status = eventNode.get("holdingStatus").get("name").asText();
            currentLesson.setStatus("Неизвестно".equals(status) ? "Запланировано" : status);

            if (eventNode.get("_links").get("course-unit-realization") == null) {
                continue;
            }

            current.setCourseId(UUID.fromString(eventNode.get("_links").get("course-unit-realization").get("href").asText().substring(1)));

            final FullAddress address = addressMap.get(current.getSessionId());
            CourseDetails currentCourseDetails = CourseDetails
                    .builder()
                    .courseName(coursesNameMap.get(current.getCourseId()))
                    .instituteName(instituteAddressNameMap.get(address.getBuilding()))
                    .instituteAddress(address.getBuilding())
                    .professorsNames(professorFullName)
                    .build();
            current.setRoomName(address.getRoom());
            current.setCourseDetails(currentCourseDetails);
            current.setProfessorName(professorFullName);
            current.setCreateTimestamp(Instant.now());

            result.add(currentLesson);
        }

        return result;
    }

    private static Map<UUID, String> findCourseMap(JsonNode root) {
        final Map<UUID, String> result = new HashMap<>();
        final Iterator<JsonNode> coursesNode = root.get("course-unit-realizations").elements();
        while (coursesNode.hasNext()) {
            final JsonNode courseNode = coursesNode.next();
            result.put(UUID.fromString(courseNode.get("id").asText()), courseNode.get("nameShort").asText());
        }

        return result;
    }

    private static Map<UUID, FullAddress> findAddressMap(JsonNode root) {
        final Map<String, String> eventRoomsMap = new HashMap<>();
        final Iterator<JsonNode> eventRoomsNode = root.get("event-rooms").elements();
        while (eventRoomsNode.hasNext()) {
            final JsonNode eventRoomNode = eventRoomsNode.next();
            eventRoomsMap.put(eventRoomNode.get("_links").get("self").get("href").asText(), eventRoomNode.get("_links").get("room").get("href").asText());
        }

        final Map<String, FullAddress> roomsMap = new HashMap<>();
        final Iterator<JsonNode> roomsNode = root.get("rooms").elements();
        while (roomsNode.hasNext()) {
            final JsonNode roomNode = roomsNode.next();
            final String room = roomNode.get("name").asText();
            final String buildingAddress = roomNode.get("building").get("name").asText();
            roomsMap.put(roomNode.get("_links").get("self").get("href").asText(), new FullAddress(buildingAddress, room));
        }

        final Map<UUID, FullAddress> result = new HashMap<>();
        final Iterator<JsonNode> locationsNode = root.get("event-locations").elements();
        while (locationsNode.hasNext()) {
            final JsonNode locationNode = locationsNode.next();
            final FullAddress address;
            if (locationNode.get("_links").get("event-rooms") == null) {
                address = new FullAddress(DISTANT_SESSION, DISTANT_SESSION);
            } else {
                address = roomsMap.get(eventRoomsMap.get(locationNode.get("_links").get("event-rooms").get("href").asText()));
            }

            result.put(UUID.fromString(locationNode.get("eventId").asText()), address);
        }

        return result;
    }
}
