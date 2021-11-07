package ru.sber.springmvc

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.boot.web.servlet.ServletComponentScan
import org.springframework.data.jpa.repository.config.EnableJpaRepositories

@SpringBootApplication
@ServletComponentScan
@EnableJpaRepositories(basePackages = ["ru.sber.springmvc.persistence"])
class SpringMvcApplication {

}

fun main(args: Array<String>) {
    runApplication<SpringMvcApplication>(*args)
}
