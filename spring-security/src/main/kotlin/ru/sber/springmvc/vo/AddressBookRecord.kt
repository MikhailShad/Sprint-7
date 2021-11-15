package ru.sber.springmvc.vo

data class AddressBookRecord(
    var id: Long? = null,
    var people: List<Person> = mutableListOf(),
    var address: String = ""
)
