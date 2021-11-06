package ru.sber.springmvc.persistence.entity

import javax.persistence.*

@Entity
@Table(name = "people")
final class PersonEntity(
    @Id
    @SequenceGenerator(name = "person_id_gen", initialValue = 1000)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "person_id_gen")
    var id: Long = 0,

    var name: String,

    @OneToOne(cascade = [CascadeType.ALL], fetch = FetchType.LAZY)
    var email: EmailEntity? = null,

    @ManyToOne(optional = false, cascade = [CascadeType.ALL], fetch = FetchType.LAZY)
    @JoinColumn(name = "record_id", referencedColumnName = "id")
    var addressBookRecord: AddressBookRecordEntity? = null
)