package ru.sber.springmvc.service

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import ru.sber.springmvc.persistence.repository.PersonRepository
import ru.sber.springmvc.persistence.repository.RoleRepository
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
    lateinit var passwordEncoder: PasswordEncoder

    override fun loadUserByUsername(p0: String?): UserDetails {
        return personRepository.findByEmail(p0!!)
    }


}