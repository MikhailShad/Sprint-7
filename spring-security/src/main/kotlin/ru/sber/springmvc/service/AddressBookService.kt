package ru.sber.springmvc.service

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.core.convert.converter.Converter
import org.springframework.stereotype.Service
import ru.sber.springmvc.persistence.entity.AddressBookRecordEntity
import ru.sber.springmvc.persistence.repository.AddressBookRepository
import ru.sber.springmvc.vo.AddressBookRecord
import ru.sber.springmvc.vo.Query

@Service
class AddressBookService @Autowired constructor(
    val addressBookRepository: AddressBookRepository,
    val entityConverter: Converter<AddressBookRecordEntity, AddressBookRecord>,
    val voConverter: Converter<AddressBookRecord, AddressBookRecordEntity>
) {

    fun get(id: Long): AddressBookRecord {
        val recordEntity = addressBookRepository.findById(id).get()
        return entityConverter.convert(recordEntity)!!
    }

    fun get(query: Map<String, String>?): List<AddressBookRecord> {
        if (query.isNullOrEmpty()) {
            return addressBookRepository.findAll()
                .map { entityConverter.convert(it)!! }
                .toList()
        }

        return addressBookRepository.get(Query(query[Query.ID], query[Query.NAME], query[Query.ADDRESS]))
            .map { entityConverter.convert(it)!! }
    }

    fun create(addressBookRecord: AddressBookRecord): Long {
        val recordEntity = voConverter.convert(addressBookRecord)!!
        return addressBookRepository.save(recordEntity).id
    }

    fun update(id: Long, addressBookRecord: AddressBookRecord) {
        val existingEntity = addressBookRepository.findById(id)
        if (existingEntity.isEmpty) {
            create(addressBookRecord)
            return
        }

        val newRecordEntity = voConverter.convert(addressBookRecord)!!
        addressBookRepository.save(newRecordEntity)
    }

    fun delete(id: Long) {
        addressBookRepository.deleteById(id)
    }

}
