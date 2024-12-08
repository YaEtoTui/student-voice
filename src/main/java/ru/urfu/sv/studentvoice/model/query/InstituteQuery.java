package ru.urfu.sv.studentvoice.model.query;

import com.querydsl.core.types.dsl.BooleanExpression;
import org.springframework.stereotype.Repository;
import ru.urfu.sv.studentvoice.model.domain.entity.Institute;
import ru.urfu.sv.studentvoice.model.domain.entity.QInstitute;

import java.util.Collection;
import java.util.List;

@Repository
public class InstituteQuery extends AbstractQuery {

    private final static QInstitute institute = new QInstitute("institute");

    /**
     * Ищем все институты
     */
    public List<Institute> findAllInstituteList() {

        return query()
                .selectFrom(institute)
                .fetch();
    }

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

    public List<String> findAllAddress() {
        return query()
                .from(institute)
                .select(institute.address)
                .distinct()
                .fetch();
    }
}