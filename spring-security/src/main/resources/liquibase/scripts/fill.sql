--liquibase formatted sql

--changeset shadrinmd:add_roles
insert into roles (id, role)
values (1, 'ROLE_ADMIN');
insert into roles (id, role)
values (2, 'ROLE_TECH');
insert into roles (id, role)
values (3, 'ROLE_USER');

--changeset shadrinmd:add_admin
insert into emails(id, value)
VALUES (1, 'admin@admin.com');
insert into people (id, name, email_id, stored_password)
values (1, 'Админ Админович Тестов', 1, '$2a$10$ZU5OfsXBajd75/eiVtInqe1xstihShQWVpozDIxECo9sFe4SlZake');
insert into people_roles (people_id, roles_id)
VALUES (1, 1);

--changeset shadrinmd:add_user
insert into emails(id, value)
VALUES (2, 'user@foo.bar');
insert into people (id, name, email_id, stored_password)
values (2, 'Юзер Юзерович Обычнов', 2, '$2a$10$ZU5OfsXBajd75/eiVtInqe1xstihShQWVpozDIxECo9sFe4SlZake');
insert into people_roles (people_id, roles_id)
VALUES (2, 3);

--changeset shadrinmd:add_record
insert into records(id, address)
VALUES (1, 'Улица Пушкина, дом Колотушкина');
update people
set record_id = 1
where id in (1, 2)