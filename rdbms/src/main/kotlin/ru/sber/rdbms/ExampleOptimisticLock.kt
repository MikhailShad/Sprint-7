package ru.sber.rdbms

/**
create table account1
(
id bigserial constraint account_pk primary key,
amount int,
version int
);
 */
fun main() {
    TransferOptimisticLock().transfer(1234, 4321, 100u)
}


