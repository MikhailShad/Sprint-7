package ru.sber.springmvc.config

import liquibase.integration.spring.SpringLiquibase
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.jpa.repository.config.EnableJpaRepositories
import javax.sql.DataSource

@Configuration
@EnableJpaRepositories(basePackages = ["ru.sber.springmvc.persistence"])
class PersistenceConfig {

    @Autowired
    lateinit var dataSource: DataSource

    @Bean
    fun springLiquibase(): SpringLiquibase {
        val liquibase = SpringLiquibase()
        liquibase.changeLog = "classpath:liquibase/db.changelog-master.yaml"
        liquibase.dataSource = dataSource
        return liquibase
    }

}