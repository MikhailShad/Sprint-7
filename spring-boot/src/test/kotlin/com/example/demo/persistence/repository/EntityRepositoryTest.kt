package com.example.demo.persistence.repository

import com.example.demo.persistence.entity.Entity
import io.zonky.test.db.AutoConfigureEmbeddedDatabase
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest

@DataJpaTest
@AutoConfigureEmbeddedDatabase(provider = AutoConfigureEmbeddedDatabase.DatabaseProvider.ZONKY)
internal class EntityRepositoryTest @Autowired constructor(
    val entityRepository: EntityRepository
) {

    @Test
    fun `test save and find actions`() {
        val savedEntity = entityRepository.save(Entity(name = "Name"))

        val foundEntity = entityRepository.findById(savedEntity.id!!)

        assertTrue(foundEntity.isPresent)
        assertEquals(savedEntity, foundEntity.get())
    }
}