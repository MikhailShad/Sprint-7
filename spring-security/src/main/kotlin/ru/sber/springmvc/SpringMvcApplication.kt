package ru.sber.springmvc

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.boot.web.servlet.ServletComponentScan
import org.springframework.data.jpa.repository.config.EnableJpaRepositories
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity

@SpringBootApplication
@ServletComponentScan
@EnableJpaRepositories(basePackages = ["ru.sber.springmvc.persistence"])
@EnableWebSecurity
class SpringMvcApplication {

}

fun main(args: Array<String>) {
    runApplication<SpringMvcApplication>(*args)
}
