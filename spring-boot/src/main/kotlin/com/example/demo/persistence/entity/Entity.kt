package com.example.demo.persistence.entity

import javax.persistence.*
import javax.persistence.Entity

@Entity
class Entity(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) var id: Long? = null,
    @Column var name: String?
)