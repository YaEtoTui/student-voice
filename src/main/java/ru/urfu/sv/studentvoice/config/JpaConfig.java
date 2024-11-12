package ru.urfu.sv.studentvoice.config;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;


@Configuration
@EntityScan(basePackages = {"ru.urfu.sv.studentvoice.model.domain.entity"})
@EnableJpaRepositories(basePackages = {"ru.urfu.sv.studentvoice.model.repository"})
@EnableScheduling
@EnableTransactionManagement
public class JpaConfig {
    @Bean
    public PlatformTransactionManager transactionManager(DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }
}
