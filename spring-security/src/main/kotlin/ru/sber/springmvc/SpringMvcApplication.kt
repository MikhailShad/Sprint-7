package ru.sber.springmvc

import org.hibernate.SessionFactory
import org.hibernate.cfg.Configuration
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.boot.web.servlet.ServletComponentScan
import org.springframework.context.annotation.Bean
import org.springframework.data.jpa.repository.config.EnableJpaRepositories
import ru.sber.springmvc.persistence.entity.AddressBookRecordEntity
import ru.sber.springmvc.persistence.entity.EmailEntity
import ru.sber.springmvc.persistence.entity.PersonEntity

@SpringBootApplication
@ServletComponentScan
@EnableJpaRepositories(basePackages = ["ru.sber.springmvc.persistence"])
class SpringMvcApplication {

    @Bean(name = ["sessionFactory", "entityManagerFactory"])
    fun sessionFactory(): SessionFactory = Configuration().configure()
        .addAnnotatedClass(AddressBookRecordEntity::class.java)
        .addAnnotatedClass(PersonEntity::class.java)
        .addAnnotatedClass(EmailEntity::class.java)
        .buildSessionFactory()

}

fun main(args: Array<String>) {
    runApplication<SpringMvcApplication>(*args)
}
