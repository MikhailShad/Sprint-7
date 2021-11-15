package ru.sber.springmvc.persistence.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import ru.sber.springmvc.persistence.entity.PersonEntity

@Repository
interface PersonRepository : JpaRepository<PersonEntity, Long> {
}