package ru.sber.springmvc.persistence.entity

import org.hibernate.annotations.NaturalId
import org.springframework.security.core.GrantedAuthority
import ru.sber.springmvc.enum.Role
import javax.persistence.*

@Entity
@Table(name = "roles")
final class RoleEntity(
    @Id
    @SequenceGenerator(name = "role_id_gen", initialValue = 1000)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "role_id_gen")
    var id: Long = 0,

    @NaturalId
    @Enumerated(value = EnumType.STRING)
    var role: Role,

    @ManyToMany(mappedBy = "roles")
    var people: Set<PersonEntity>? = null
) : GrantedAuthority {
    override fun getAuthority(): String {
        return role.name
    }
}