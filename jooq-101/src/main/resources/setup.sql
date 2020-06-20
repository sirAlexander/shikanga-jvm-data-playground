create table users
(
    id    int auto_increment primary key,
    email varchar
);

create table courses
(
    id          int auto_increment primary key,
    title       varchar,
    description varchar,
    price       int
);

create table purchases
(
    user_id      int,
    course_id    int,
    purchased_at timestamp,
    foreign key (user_id) references users (id),
    foreign key (course_id) references courses (id)
);

insert into users (email)
values ('test1@email.com');
insert into users (email)
values ('test2@email.com');

insert into courses (title, description, price)
VALUES ('title1', 'title1 description', 21.00);
insert into courses (title, description, price)
VALUES ('title2', 'title2 description', 75.00);

insert into purchases (user_id, course_id, purchased_at)
VALUES (1, 2, now());
insert into purchases (user_id, course_id, purchased_at)
VALUES (2, 1, now());
commit;

select * from courses;
select * from users;
select * from purchases;

