package ru.urfu.sv.studentvoice.model.query;

import com.querydsl.core.types.dsl.BooleanExpression;
import org.springframework.stereotype.Repository;
import ru.urfu.sv.studentvoice.model.domain.entity.Institute;
import ru.urfu.sv.studentvoice.model.domain.entity.QInstitute;

import java.util.Collection;

@Repository
public class InstituteQuery extends AbstractQuery {

    private final static QInstitute institute = new QInstitute("institute");

    /**
     * Проверяем, существует ли институт с fullNameInstitute
     */
    public boolean isExistInstitute(String fullNameInstitute) {

        final BooleanExpression exp = institute.fullName.eq(fullNameInstitute);

        final Collection<Institute> institutes = query()
                .selectFrom(institute)
                .where(exp)
                .fetch();

        return !institutes.isEmpty();
    }
}