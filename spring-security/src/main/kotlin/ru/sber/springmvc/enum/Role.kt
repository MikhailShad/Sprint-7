package ru.sber.springmvc.enum

enum class Role(
    var authority: String
) {
    USER("ROLE_USER"),
    ADMIN("ROLE_ADMIN"),
    TECH("ROLE_TECH")
}