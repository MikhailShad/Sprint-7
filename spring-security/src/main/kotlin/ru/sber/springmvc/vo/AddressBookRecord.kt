package ru.sber.springmvc.vo

data class AddressBookRecord(
    var id: Long? = null,
    var name: String,
    var email: String = "dummy@test.com",
    var address: String
)
