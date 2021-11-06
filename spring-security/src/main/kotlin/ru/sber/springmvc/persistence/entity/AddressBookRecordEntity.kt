package ru.sber.springmvc.persistence.entity

import javax.persistence.*

@Entity
@Table(name = "records")
class AddressBookRecordEntity(
    @Id
    @SequenceGenerator(name = "record_id_gen", initialValue = 1000)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "record_id_gen")
    var id: Long = 0,

    @OneToMany(mappedBy = "addressBookRecord", cascade = [CascadeType.ALL])
    var people: Set<PersonEntity> = mutableSetOf(),

    var address: String
)