package ru.sber.springmvc.repository

import org.springframework.stereotype.Service
import ru.sber.springmvc.vo.AddressBookRecord
import ru.sber.springmvc.vo.Query
import java.util.concurrent.ConcurrentHashMap

@Service
class AddressBookRepository {

    private var currentId = 0L
    private val storage = ConcurrentHashMap<Long, AddressBookRecord>()

    private fun checkContains(id: Long) {
        if (!storage.containsKey(id)) {
            throw RuntimeException("No such record by ID=$id")
        }
    }

    fun get(id: Long): AddressBookRecord {
        checkContains(id)
        return storage[id]!!
    }

    fun get(query: Query): List<AddressBookRecord> {
        return storage
            .filter { query.id?.equals(it.key.toString()) ?: true }
            .map { it.value }
            .filter { query.name?.equals(it.name) ?: true }
            .filter { query.address?.equals(it.address) ?: true }
            .toList()
    }

    fun getAll(): List<AddressBookRecord> {
        return storage
            .map { it.value }
            .toList()
    }

    fun create(addressBookRecord: AddressBookRecord) {
        addressBookRecord.id = currentId
        storage[currentId] = addressBookRecord

        currentId += 1
    }

    fun update(id: Long, addressBookRecord: AddressBookRecord) {
        checkContains(id)
        storage[id] = addressBookRecord
    }

    fun delete(id: Long) {
        checkContains(id)
        storage.remove(id)
    }

}
