package ru.sber.rdbms

class TransferPessimisticLock : AbstractTransfer() {

    override fun transfer(accountId1: Long, accountId2: Long, amount: ULong) {
        val autoCommit = databaseConnection.autoCommit

        databaseConnection.use { connection ->
            try {
                connection.autoCommit = false

                // Check accounts before operation
                getAccountVersion(connection, accountId1, amount)

                // Select accounts for update
                val selectForUpdateStatement = connection.prepareStatement(
                    """select * from account 
                    |where id in (?,?) 
                    |for update """.trimMargin()
                )
                selectForUpdateStatement.setLong(1, accountId1)
                selectForUpdateStatement.setLong(2, accountId2)

                // Decrement amount on account 1
                val decrementStatement =
                    connection.prepareStatement(
                        """update account 
                        |set amount = amount - ?
                        |where id = ?""".trimMargin()
                    )
                decrementStatement.setLong(1, amount.toLong())
                decrementStatement.setLong(2, accountId1)
                if (decrementStatement.executeUpdate() == 0) {
                    throw TransferException("Could not decrement money amount on account $accountId1")
                }

                // Increment amount on account 2
                val incrementStatement =
                    connection.prepareStatement(
                        """update account 
                        |set amount = amount + ?
                        |where id = ?""".trimMargin()
                    )
                incrementStatement.setLong(1, amount.toLong())
                incrementStatement.setLong(2, accountId2)
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