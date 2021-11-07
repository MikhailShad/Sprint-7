package ru.sber.springmvc.persistence.repository

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional
import ru.sber.springmvc.persistence.entity.AddressBookRecordEntity
import ru.sber.springmvc.vo.Query
import java.util.*
import javax.persistence.EntityManager
import javax.persistence.PersistenceContext

@Repository
class AddressBookRepository(
    @PersistenceContext
    val em: EntityManager
) : CrudRepository<AddressBookRecordEntity, Long> {

    @Autowired
    lateinit var personRepository: PersonRepository

    @Autowired
    lateinit var emailRepository: EmailRepository

    fun get(query: Query): List<AddressBookRecordEntity> {
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

        val emQuery = em.createQuery(queryString)
        queryParams.forEach {
            emQuery.setParameter(it.key, it.value)
        }

        return emQuery.resultList.map { it as AddressBookRecordEntity }.toList()
    }

    @Transactional
    override fun <S : AddressBookRecordEntity?> save(entity: S): S {
        entity?.people?.forEach {
            it.email = emailRepository.findByValue(it.email?.value!!) ?: it.email
        }

        val persistedEntity = em.merge(entity)
        em.persist(persistedEntity)

        return persistedEntity
    }

    override fun <S : AddressBookRecordEntity?> saveAll(entities: MutableIterable<S>): MutableIterable<S> {
        return entities
            .map { save(it) }
            .toMutableList()
    }

    override fun findById(id: Long): Optional<AddressBookRecordEntity> {
        return Optional.ofNullable(
            em.createQuery("from AddressBookRecordEntity r where r.id = :id")
                .setParameter("id", id)
                .resultList.first() as AddressBookRecordEntity
        )

    }

    override fun existsById(id: Long): Boolean {
        return findById(id).isPresent
    }

    override fun findAll(): MutableIterable<AddressBookRecordEntity> {
        return em.createQuery("from AddressBookRecordEntity").resultList
            .map { it as AddressBookRecordEntity }
            .toMutableList()
    }

    override fun findAllById(ids: MutableIterable<Long>): MutableIterable<AddressBookRecordEntity> {
        return em.createQuery("from AddressBookRecordEntity r where r.id in (:ids)")
            .setParameter("ids", ids)
            .resultList
            .map { it as AddressBookRecordEntity }
            .toMutableList()
    }

    override fun count(): Long {
        return em.createQuery("select count(*) from AddressBookRecordEntity").singleResult as Long
    }

    @Transactional
    override fun delete(entity: AddressBookRecordEntity) {
        em.remove(entity)
    }

    @Transactional
    override fun deleteById(id: Long) {
        delete(findById(id).get())
    }

    override fun deleteAllById(ids: MutableIterable<Long>) {
        TODO("Not yet implemented")
    }

    override fun deleteAll(entities: MutableIterable<AddressBookRecordEntity>) {
        TODO("Not yet implemented")
    }

    override fun deleteAll() {
        TODO("Not yet implemented")
    }

}
