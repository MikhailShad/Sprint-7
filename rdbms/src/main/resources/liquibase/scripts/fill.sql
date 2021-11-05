--liquibase formatted sql

--changeset shadrinmd:filling_with_test_data
insert into account
values (1234, 999, 1);
insert into account
values (4321, 500, 1);