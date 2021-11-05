package ru.sber.springmvc.vo

data class AddressBookRecord(var id: Long?, var name: String, var address: String) {
    constructor(name: String, address: String) : this(null, name, address) {}
}
