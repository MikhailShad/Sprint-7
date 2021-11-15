package ru.sber.rdbms

class TransferOptimisticLock : AbstractTransfer() {

    override fun transfer(accountId1: Long, accountId2: Long, amount: ULong) {
        val autoCommit = databaseConnection.autoCommit

        databaseConnection.use { connection ->
            try {
                connection.autoCommit = false

                // Check accounts before operation
                val account1Version = getAccountVersion(connection, accountId1, amount)
                var account2Version = getAccountVersion(connection, accountId2)

                // Decrement amount on account 1
                val decrementStatement =
                    connection.prepareStatement(
                        """update account 
                        |set amount = amount - ?, version = version + 1 
                        |where id = ? and version = ?""".trimMargin()
                    )
                decrementStatement.setLong(1, amount.toLong())
                decrementStatement.setLong(2, accountId1)
                decrementStatement.setLong(3, account1Version)
                if (decrementStatement.executeUpdate() == 0) {
                    throw TransferException("Could not decrement money amount on account $accountId1")
                }

                // Increment amount on account 2
                val incrementStatement =
                    connection.prepareStatement(
                        """update account 
                        |set amount = amount + ?, version = version + 1 
                        |where id = ? and version = ?""".trimMargin()
                    )
                incrementStatement.setLong(1, amount.toLong())
                incrementStatement.setLong(2, accountId2)
                incrementStatement.setLong(3, account2Version)
                if (incrementStatement.executeUpdate() == 0) {
                    throw TransferException("Could not increment money amount on account $accountId2")
                }

                connection.commit()
            } catch (exception: RuntimeException) {
                println(exception.message)
                connection.rollback()
            } finally {
                connection.autoCommit = autoCommit
            }
        }
    }

}