--liquibase formatted sql

--changeset shadrinmd:create_people_table
create sequence if not exists person_id_gen start 1000 increment 50;
create table people
(
    id              bigint not null default nextval('person_id_gen'),
    name            text,
    stored_password text,
    record_id       bigint,
    email_id        bigint,
    primary key (id)
);
--rollback
--drop table people

--changeset shadrinmd:create_roles_table
create sequence if not exists role_id_gen start 1000 increment 50;
create table roles
(
    id   bigint not null default nextval('role_id_gen'),
    role text,
    primary key (id)
);
--rollback
--drop table roles

--changeset shadrinmd:create_emails_table
create sequence if not exists email_id_gen start 1000 increment 50;
create table emails
(
    id    bigint not null default nextval('email_id_gen'),
    value text,
    primary key (id)
);
--rollback
--drop table emails

--changeset shadrinmd:create_records_table
create sequence if not exists record_id_gen start 1000 increment 50;
create table records
(
    id      bigint not null default nextval('record_id_gen'),
    address text,
    primary key (id)
);
--rollback
--drop table records

--changeset shadrinmd:create_people_roles_table
create table people_roles
(
    people_id bigint,
    roles_id  bigint,
    primary key (people_id, roles_id)
);
--rollback
--drop table people_roles

--changeset shadrinmd:mapping_tables
alter table people
    add constraint fk_record foreign key (record_id) references records (id);
alter table people
    add constraint fk_email foreign key (email_id) references emails (id);


