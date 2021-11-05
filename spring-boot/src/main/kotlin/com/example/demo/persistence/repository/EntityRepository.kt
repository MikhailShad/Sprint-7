package com.example.demo.persistence.repository

import com.example.demo.persistence.entity.Entity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface EntityRepository : JpaRepository<Entity, Long>