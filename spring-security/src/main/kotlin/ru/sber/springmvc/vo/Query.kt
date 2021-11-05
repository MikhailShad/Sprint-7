package ru.sber.springmvc.vo

class Query(val id: String?, val name: String?, val address: String?) {

    companion object Params {
        const val ID: String = "id"
        const val NAME: String = "name"
        const val ADDRESS: String = "address"
    }

}
