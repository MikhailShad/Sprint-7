package ru.sber.springmvc.vo

data class Person(
    var id: Long? = null,
    var name: String = "",
    var email: String = "",
    var password: String = ""
)