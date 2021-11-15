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

    /**
     * Проверяет наличие аккаунта и возвращает его версию
     */
    protected fun getAccountVersion(connection: Connection, accountId: Long, amount: ULong? = null): Long {
        val getCurrentAccountInfoStatement =
            connection.prepareStatement("select amount, version from account where id = ?")
        getCurrentAccountInfoStatement.setLong(1, accountId)
        getCurrentAccountInfoStatement.executeQuery().use { resultSet ->
            if (resultSet.isLast) {
                throw TransferException("No account with id $accountId found")
            }

            resultSet.next()
            if (amount != null && resultSet.getLong("amount") - amount.toLong() < 0) {
                throw TransferException("Not enough money on account $accountId")
            }

            return resultSet.getLong("version")
        }
    }
}

class TransferException(message: String) : RuntimeException(message)