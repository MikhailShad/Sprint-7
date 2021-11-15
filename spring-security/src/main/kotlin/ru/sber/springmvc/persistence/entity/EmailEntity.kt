package ru.sber.springmvc.persistence.entity

import org.hibernate.annotations.NaturalId
import javax.persistence.*

@Entity
@Table(name = "emails")
class EmailEntity(
    @Id
    @SequenceGenerator(name = "email_id_gen", initialValue = 1000)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "email_id_gen")
    var id: Long = 0,

    @NaturalId
    var value: String
)