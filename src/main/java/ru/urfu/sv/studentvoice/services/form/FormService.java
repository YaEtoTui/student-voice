package ru.urfu.sv.studentvoice.services.form;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.urfu.sv.studentvoice.model.domain.dto.form.FormInfo;
import ru.urfu.sv.studentvoice.model.domain.dto.form.FormInfoDto;
import ru.urfu.sv.studentvoice.model.query.LessonQuery;
import ru.urfu.sv.studentvoice.services.form.mapper.FormMapper;

import java.util.List;

@Service
public class FormService {

    @Autowired
    private LessonQuery lessonQuery;
    @Autowired
    private FormMapper formMapper;

    @Transactional
    public FormInfoDto getDataForm(Long lessonId) {

        final List<FormInfo> formInfoList = lessonQuery.getFormInfoList(lessonId);
        return formMapper.createFormInfoDto(formInfoList);
    }
}