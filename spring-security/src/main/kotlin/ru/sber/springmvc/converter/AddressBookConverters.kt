package ru.sber.springmvc.converter

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.core.convert.converter.Converter
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Component
import ru.sber.springmvc.persistence.entity.AddressBookRecordEntity
import ru.sber.springmvc.persistence.entity.EmailEntity
import ru.sber.springmvc.persistence.entity.PersonEntity
import ru.sber.springmvc.vo.AddressBookRecord
import ru.sber.springmvc.vo.Person

@Component
class RecordToEntity : Converter<AddressBookRecord, AddressBookRecordEntity> {
    @Autowired
    private lateinit var personToEntity: PersonToEntity

    override fun convert(source: AddressBookRecord): AddressBookRecordEntity {
        val recordEntity = AddressBookRecordEntity(
            id = source.id ?: 0,
            address = source.address,
            people = source.people.map { personToEntity.convert(it) }.toSet()
        )

        recordEntity.people.forEach { it.addressBookRecord = recordEntity }

        return recordEntity
    }
}

@Component
class EntityToRecord : Converter<AddressBookRecordEntity, AddressBookRecord> {

    @Autowired
    private lateinit var entityToPerson: EntityToPerson

    override fun convert(source: AddressBookRecordEntity): AddressBookRecord {
        return AddressBookRecord(
            id = source.id,
            address = source.address,
            people = source.people.map { entityToPerson.convert(it) }
        )
    }

}

@Component
class PersonToEntity : Converter<Person, PersonEntity> {

    @Autowired
    lateinit var passwordEncoder: PasswordEncoder

    override fun convert(source: Person): PersonEntity {
        return PersonEntity(
            id = source.id ?: 0,
            name = source.name,
            email = EmailEntity(value = source.email),
            storedPassword = passwordEncoder.encode(source.password)
        )
    }
}

@Component
class EntityToPerson : Converter<PersonEntity, Person> {
    override fun convert(source: PersonEntity): Person {
        return Person(
            id = source.id,
            name = source.name,
            email = source.email?.value!!
        )
    }
}
