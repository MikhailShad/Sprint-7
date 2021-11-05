package ru.sber.rdbms

import java.sql.Connection
import java.sql.DriverManager

interface Transfer {
    fun transfer(accountId1: Long, accountId2: Long, amount: ULong)
}

abstract class AbstractTransfer : Transfer {
    protected val databaseConnection: Connection = DriverManager.getConnection(
        "jdbc:postgresql://localhost:5432/postgres",
        "postgres",
        "postgres"
    )
}

class TransferException(message: String) : RuntimeException(message)