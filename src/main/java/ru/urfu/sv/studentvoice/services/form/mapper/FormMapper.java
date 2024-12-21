package ru.urfu.sv.studentvoice.services.form.mapper;

import org.springframework.stereotype.Component;
import ru.urfu.sv.studentvoice.model.domain.dto.form.FormInfo;
import ru.urfu.sv.studentvoice.model.domain.dto.form.FormInfoDto;
import ru.urfu.sv.studentvoice.model.domain.dto.form.InfoForLesson;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class FormMapper {

    public FormInfoDto createFormInfoDto(Collection<FormInfo> formInfoCollection) {

        if (formInfoCollection != null && !formInfoCollection.isEmpty()) {
            final Map<InfoForLesson, List<FormInfo>> groupedByInfoForLesson = formInfoCollection.stream()
                    .collect(Collectors.groupingBy(lesson -> new InfoForLesson(
                                    lesson.getLessonId(),
                                    lesson.getStartDateTime(),
                                    lesson.getEndDateTime(),
                                    lesson.getCourseName()
                            )
                    ));

            return groupProfessorData(groupedByInfoForLesson);
        }

        throw new IllegalArgumentException("No form info found");
    }

    private FormInfoDto groupProfessorData(Map<InfoForLesson, List<FormInfo>> groupedByInfoForLesson) {

        // ToDo доделать
        return new FormInfoDto();
    }
}