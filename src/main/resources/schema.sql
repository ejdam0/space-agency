drop table if exists mission;

create table mission (
	id IDENTITY primary key,
	name varchar(200) not null unique,
	imagery_type integer not null,
    start_date date not null default current_date,
	finish_date date not null default current_date
);
drop table if exists product;

create table product (
	id IDENTITY primary key,
	mission_id integer not null references mission(id),
	acquisition_date date not null default current_date,
	footprint varchar(80) not null,
	price real not null,
	url varchar(200) not null
);

drop table if exists person;

create table person (
	id IDENTITY primary key,
	first_name varchar(50) not null,
	last_name varchar(100) not null,
	authority varchar(10) not null,
	username varchar(10) not null unique,
	password varchar(60) not null,
	enabled BOOLEAN NOT NULL DEFAULT true
);

drop table if exists orders;

create table orders (
    id IDENTITY primary key,
	person_id integer not null references person(id),
    order_date date not null default current_date
);

drop table if exists product_order;

create table product_order (
    order_id integer not null references orders(id),
    product_id integer not null references product(id),
    quantity integer not null
);

drop table if exists product_copy;

create table product_copy (
	id IDENTITY primary key,
	mission_id integer not null references mission(id),
	acquisition_date date not null default current_date,
	footprint varchar(80) not null,
	price real not null,
	url varchar(200) not null
);

drop table if exists product_order_copy;

create table product_order_copy (
    order_id integer not null references orders(id),
    product_id integer not null references product_copy(id),
    quantity integer not null
);
