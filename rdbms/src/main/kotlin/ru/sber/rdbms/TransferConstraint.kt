package ru.sber.rdbms

import java.sql.SQLException

class TransferConstraint : AbstractTransfer() {

    override fun transfer(accountId1: Long, accountId2: Long, amount: ULong) {
        databaseConnection.use { connection ->
            try {
                val decrementStatement =
                    connection.prepareStatement("update account set amount = amount - ? where id = ?")
                decrementStatement.setLong(1, amount.toLong())
                decrementStatement.setLong(2, accountId1)
                decrementStatement.executeUpdate()

                val incrementStatement =
                    connection.prepareStatement("update account set amount = amount + ? where id = ?")
                incrementStatement.setLong(1, amount.toLong())
                incrementStatement.setLong(2, accountId2)
                incrementStatement.executeUpdate()
            } catch (exception: SQLException) {
                println(exception.message)
            }
        }
    }

}
