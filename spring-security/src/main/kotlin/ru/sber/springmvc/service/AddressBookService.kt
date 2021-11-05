package ru.sber.springmvc.service

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import ru.sber.springmvc.repository.AddressBookRepository
import ru.sber.springmvc.vo.AddressBookRecord
import ru.sber.springmvc.vo.Query

@Service
class AddressBookService @Autowired constructor(val addressBookRepository: AddressBookRepository) {

    fun get(id: Long): AddressBookRecord {
        return addressBookRepository.get(id)
    }

    fun get(query: Map<String, String>?): List<AddressBookRecord> {
        if (query.isNullOrEmpty()) {
            return addressBookRepository.getAll()
        }

        return addressBookRepository.get(Query(query[Query.ID], query[Query.NAME], query[Query.ADDRESS]))
    }

    fun create(addressBookRecord: AddressBookRecord) {
        addressBookRepository.create(addressBookRecord)
    }

    fun update(id: Long, addressBookRecord: AddressBookRecord) {
        addressBookRepository.update(id, addressBookRecord)
    }

    fun delete(id: Long) {
        addressBookRepository.delete(id)
    }

}
