package ru.sber.springmvc.persistence.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import ru.sber.springmvc.persistence.entity.PersonEntity

@Repository
interface PersonRepository : JpaRepository<PersonEntity, Long> {
    @Query("select person from PersonEntity as person join EmailEntity email on person.email = email where email.value = :email")
    fun findByEmail(@Param("email") email: String): PersonEntity?
}