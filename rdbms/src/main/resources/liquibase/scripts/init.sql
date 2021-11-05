--liquibase formatted sql

--changeset rrmasgutov:init
create table account
(
    id      bigserial
        constraint account_pk primary key,
    amount  int CHECK ( amount >= 0 ),
    version int
);
--rollback
--drop table account;

--changeset shadrinmd:created_index_on_account_id
create index if not exists idx_account_id_version on account (id, version);
--rollback
--drop index idx_account_id_version;


