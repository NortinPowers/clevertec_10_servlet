truncate table products restart identity;

insert into products (name, description, price)
values ('Плюмбус', 'это универсальное устройство', 215),
       ('Портальная пушка', 'устройство создающее порталы', 4200);

update products
set uuid = '76dbb74c-2f08-4bc0-8029-aed02147e737'
where name = 'Плюмбус';

