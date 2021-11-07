package ru.sber.springmvc.persistence.repository

import org.hibernate.SessionFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Repository
import ru.sber.springmvc.persistence.entity.AddressBookRecordEntity
import ru.sber.springmvc.persistence.entity.EmailEntity
import ru.sber.springmvc.persistence.entity.PersonEntity
import ru.sber.springmvc.vo.AddressBookRecord
import ru.sber.springmvc.vo.Person
import ru.sber.springmvc.vo.Query

@Repository
class AddressBookRepository {

    @Autowired
    lateinit var sessionFactory: SessionFactory

    fun get(id: Long): AddressBookRecord {
        val addressBookRecord: AddressBookRecord

        sessionFactory.openSession().use { session ->
            session.beginTransaction()
            val result: AddressBookRecordEntity = session.get(AddressBookRecordEntity::class.java, id)
                ?: throw java.lang.RuntimeException("No record with id $id")
            addressBookRecord = AddressBookRecord(
                result.id,
                result.people
                    .map { Person(name = it.name, email = it.email?.value!!) }
                    .sortedBy { it.id }
                    .toList(),
                result.address
            )

            session.transaction.commit()
        }

        return addressBookRecord
    }

    fun get(query: Query): List<AddressBookRecord> {
        val queryParts = mutableListOf("select record from AddressBookRecordEntity as record")
        val queryParams = mutableMapOf<String, String>()

        var needsWhereClause = true
        if (query.name != null) {
            queryParts.add("full join record.people person where person.name = :name")
            queryParams["name"] = query.name
            needsWhereClause = false
        }

        if (query.id != null) {
            queryParts.add("${if (needsWhereClause) "where" else "and"} record.id = :id")
            queryParams["id"] = query.id
            needsWhereClause = false
        }

        if (query.address != null) {
            queryParts.add("${if (needsWhereClause) "where" else "and"} record.address = :address")
            queryParams["address"] = query.address
        }

        val queryString =
            queryParts.joinToString(" ").trim()
        return getByQuery(queryString, queryParams)
    }

    fun getAll(): List<AddressBookRecord> {
        return getByQuery("from AddressBookRecordEntity")
    }

    private fun getByQuery(
        queryString: String,
        queryParams: Map<String, String> = emptyMap()
    ): List<AddressBookRecord> {
        val result = mutableListOf<AddressBookRecord>()

        sessionFactory.openSession().use { session ->
            session.beginTransaction()

            val query = session.createQuery(queryString)
            queryParams.forEach {
                query.setParameter(it.key, it.value)
            }

            val records = query.list() as List<AddressBookRecordEntity>
            records.forEach { record ->
                result.add(
                    AddressBookRecord(
                        id = record.id,
                        people = record.people
                            .map { Person(name = it.name, email = it.email?.value!!) }
                            .sortedBy { it.id }
                            .toList(),
                        address = record.address
                    )
                )
            }

            session.transaction.commit()
        }

        return result
    }

    fun create(addressBookRecord: AddressBookRecord): Long {
        val personEntities = addressBookRecord.people.map {
            PersonEntity(name = it.name, email = EmailEntity(value = it.email))
        }.toSet()
        val addressBookRecordEntity =
            AddressBookRecordEntity(
                id = addressBookRecord.id ?: 0,
                address = addressBookRecord?.address,
                people = personEntities
            )
        personEntities.forEach { it.addressBookRecord = addressBookRecordEntity }

        val id: Long
        sessionFactory.openSession().use { session ->
            session.beginTransaction()
            id = session.save(addressBookRecordEntity) as Long
            session.transaction.commit()
        }

        return id
    }

    fun update(id: Long, addressBookRecord: AddressBookRecord) {
        sessionFactory.openSession().use { session ->
            session.beginTransaction()

            val record = session.get(AddressBookRecordEntity::class.java, addressBookRecord.id)
            record.address = addressBookRecord.address
            record.people = addressBookRecord.people
                .map {
                    PersonEntity(
                        id = it.id ?: 0,
                        name = it.name,
                        email = EmailEntity(value = it.email),
                        addressBookRecord = record
                    )
                }
                .toSet()

            session.save(record)
            session.transaction.commit()
        }
    }

    fun delete(id: Long) {
        sessionFactory.openSession().use { session ->
            session.beginTransaction()
            val record = session.get(AddressBookRecordEntity::class.java, id)
                ?: throw RuntimeException("No entity found by id $id")
            session.remove(record)
            session.transaction.commit()
        }
    }

}
