package ru.sber.rdbms

/**
create table account1
(
id bigserial constraint account_pk primary key,
amount int
);
 */
fun main() {
    TransferPessimisticLock().transfer(1234, 4321, 100u)
}


