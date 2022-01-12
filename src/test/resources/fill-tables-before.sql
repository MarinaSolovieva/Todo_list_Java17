delete from users;
delete from todos;

insert into users (id, name, surname) values (1, 'Marina', 'Solovieva');
insert into users (id, name, surname) values (2, 'Artsiom', 'Barkhatau');
insert into users (id, name, surname) values (3, 'Diana', 'Gamaunova');

insert into todos (id, description, user_id) values (1, 'Feed the cat', 1);
insert into todos (id, description, user_id) values (2, 'Go to the grocery store', 1);
insert into todos (id, description, user_id) values (3, 'Help to the mother', 2);
insert into todos (id, description, user_id) values (4, 'Bake the pie', 3);
