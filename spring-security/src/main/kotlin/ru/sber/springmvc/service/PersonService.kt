package ru.sber.springmvc.service

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.core.convert.converter.Converter
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.stereotype.Service
import ru.sber.springmvc.persistence.entity.PersonEntity
import ru.sber.springmvc.persistence.repository.PersonRepository
import ru.sber.springmvc.persistence.repository.RoleRepository
import ru.sber.springmvc.vo.Person
import javax.persistence.EntityManager
import javax.persistence.PersistenceContext

@Service
class PersonService(
    @PersistenceContext
    var em: EntityManager
) : UserDetailsService {
    @Autowired
    lateinit var personRepository: PersonRepository

    @Autowired
    lateinit var roleRepository: RoleRepository

    @Autowired
    lateinit var personToEntity: Converter<Person, PersonEntity>

    @Autowired
    lateinit var entityToPerson: Converter<PersonEntity, Person>

    override fun loadUserByUsername(p0: String?): UserDetails {
        val p = personRepository.findByEmail(p0!!)
        return p
            ?: throw RuntimeException("No person with email $p0")
    }

    fun save(person: Person) {
        val existingPerson = personRepository.findByEmail(person.email)
        if (existingPerson != null) {
            throw RuntimeException("User ${existingPerson.email} already exists")
        }

        personRepository.saveAndFlush(personToEntity.convert(person)!!)
    }

    fun getAll(): List<Person> {
        return personRepository.findAll().mapNotNull { entityToPerson.convert(it) }
    }


}