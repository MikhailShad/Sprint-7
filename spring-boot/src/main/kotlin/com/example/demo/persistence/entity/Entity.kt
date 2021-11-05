package com.example.demo.persistence.entity

import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id

@Entity
class Entity(
    @Id
    @GeneratedValue
    var id: Long?,

    @Column
    var name: String?
) {

}