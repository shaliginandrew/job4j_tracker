drop table if exists items;

create table if not exists items(
id serial primary key,
name text
);

insert into items(name) values('item 1');
insert into items(name) values('item 2');
insert into items(name) values('item 3');
insert into items(name) values('item 4');
insert into items(name) values('item 5');

