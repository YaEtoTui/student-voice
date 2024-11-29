package ru.urfu.sv.studentvoice.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import ru.urfu.sv.studentvoice.model.domain.dto.ScheduleByDay;
import ru.urfu.sv.studentvoice.model.domain.dto.lesson.LessonDetailsDto;
import ru.urfu.sv.studentvoice.model.domain.dto.response.LessonDetailsResponse;
import ru.urfu.sv.studentvoice.model.domain.entity.User;
import ru.urfu.sv.studentvoice.model.query.LessonQuery;
import ru.urfu.sv.studentvoice.model.query.UserQuery;
import ru.urfu.sv.studentvoice.services.jwt.JwtUserDetailsService;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

import static java.util.Objects.nonNull;

/**
 * Сервис по поиску расписания
 */
@Slf4j
@Service
public class ScheduleService {

    @Autowired
    private JwtUserDetailsService jwtUserDetailsService;
    @Autowired
    private UserQuery userQuery;
    @Autowired
    private LessonQuery lessonQuery;

    @PreAuthorize("@RolesAC.isProfessor()")
    public List<ScheduleByDay> findScheduleShort() {
        final String username = jwtUserDetailsService.findUsername();
        final User professor = userQuery.findProfessorByUsername(username);

        if (nonNull(professor)) {
            final String professorName = professor.getUsername();

            final List<LessonDetailsDto> lessonList = lessonQuery.findSchedule(professorName);

            final LocalDate currentDate = LocalDate.now();
            final List<ScheduleByDay> scheduleByDayList = new ArrayList<>();

            if (lessonList != null && !lessonList.isEmpty()) {
                final Map<LocalDate, List<LessonDetailsDto>> groupedByDate = lessonList.stream()
                        .collect(Collectors.groupingBy(lesson -> lesson.getDateStart().toLocalDate()));

                for (int i = 0; i < 3; i++) {
                    final LocalDate date = currentDate.plusDays(i);
                    final List<LessonDetailsDto> lessonsForDate = groupedByDate.getOrDefault(date, Collections.emptyList());

                    final List<LessonDetailsResponse> lessonResponses = lessonsForDate.stream()
                            .map(lesson -> {
                                final LessonDetailsResponse response = new LessonDetailsResponse();
                                response.setLessonId(lesson.getLessonId());
                                response.setCourseName(lesson.getCourseName());
                                response.setAddress(lesson.getAddress());
                                response.setDateStart(lesson.getDateStart());
                                response.setDateEnd(lesson.getDateEnd());

                                return response;
                            })
                            .collect(Collectors.toList());

                    final ScheduleByDay scheduleByDay = new ScheduleByDay();
                    scheduleByDay.setDate(date);
                    scheduleByDay.setListLessons(lessonResponses);

                    scheduleByDayList.add(scheduleByDay);
                }
            } else {
                for (int i = 0; i < 3; i++) {
                    final LocalDate date = currentDate.plusDays(i);

                    final ScheduleByDay scheduleByDay = new ScheduleByDay();
                    scheduleByDay.setDate(date);

                    scheduleByDayList.add(scheduleByDay);
                }
            }

            return scheduleByDayList;
        } else {
            throw new IllegalArgumentException("Not found professor");
        }
    }

    @PreAuthorize("@RolesAC.isProfessor()")
    public List<ScheduleByDay> findSchedule() {

        final String professorName = jwtUserDetailsService.findUsername();
        final LocalDate currentDate = LocalDate.now();

        final List<LessonDetailsDto> lessonList = lessonQuery.findSchedule(professorName);

        final LocalDate startOfWeek = currentDate.with(DayOfWeek.MONDAY);
        final LocalDate endOfWeek = startOfWeek.plusDays(5);
        final List<ScheduleByDay> scheduleByDayList = new ArrayList<>();

        if (nonNull(lessonList) && !lessonList.isEmpty()) {
            final Map<LocalDate, List<LessonDetailsDto>> groupedByDate = lessonList.stream()
                    .collect(Collectors.groupingBy(lesson -> lesson.getDateStart().toLocalDate()));

            for (LocalDate date = startOfWeek; !date.isAfter(endOfWeek); date = date.plusDays(1)) {

                final List<LessonDetailsDto> lessonForDateList = groupedByDate.getOrDefault(date, Collections.emptyList());

                final List<LessonDetailsResponse> lessonResponses = lessonForDateList.stream()
                        .map(lesson -> {
                            final LessonDetailsResponse response = new LessonDetailsResponse();
                            response.setLessonId(lesson.getLessonId());
                            response.setCourseName(lesson.getCourseName());
                            response.setAddress(lesson.getAddress());
                            response.setDateStart(lesson.getDateStart());
                            response.setDateEnd(lesson.getDateEnd());

                            return response;
                        })
                        .collect(Collectors.toList());

                final ScheduleByDay scheduleByDay = new ScheduleByDay();
                scheduleByDay.setDate(date);
                scheduleByDay.setListLessons(lessonResponses);

                scheduleByDayList.add(scheduleByDay);
            }
        } else {
            for (LocalDate date = startOfWeek; !date.isAfter(endOfWeek); date = date.plusDays(1)) {
                final ScheduleByDay scheduleByDay = new ScheduleByDay();
                scheduleByDay.setDate(date);

                scheduleByDayList.add(scheduleByDay);
            }
        }

        return scheduleByDayList;
    }
}