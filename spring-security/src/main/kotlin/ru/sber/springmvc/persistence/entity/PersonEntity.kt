package ru.sber.springmvc.persistence.entity

import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import javax.persistence.*

@Entity
@Table(name = "people")
final class PersonEntity(
    @Id
    @SequenceGenerator(name = "person_id_gen", initialValue = 1000)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "person_id_gen")
    var id: Long = 0,

    var name: String,

    var storedPassword: String? = null,

    @OneToOne(cascade = [CascadeType.ALL], fetch = FetchType.EAGER)
    var email: EmailEntity? = null,

    @ManyToOne(cascade = [CascadeType.ALL], fetch = FetchType.EAGER)
    @JoinColumn(name = "record_id", referencedColumnName = "id")
    var addressBookRecord: AddressBookRecordEntity? = null,

    @ManyToMany(fetch = FetchType.EAGER)
    var roles: Set<RoleEntity>? = null
) : UserDetails {
    override fun getAuthorities(): MutableCollection<out GrantedAuthority> {
        return roles!!.toMutableSet()
    }

    override fun getPassword(): String {
        return storedPassword!!
    }

    override fun getUsername(): String {
        return email!!.value
    }

    override fun isAccountNonExpired(): Boolean {
        return true
    }

    override fun isAccountNonLocked(): Boolean {
        return true
    }

    override fun isCredentialsNonExpired(): Boolean {
        return true
    }

    override fun isEnabled(): Boolean {
        return true
    }
}