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

drop table if exists content_manager;

create table content_manager (
	id IDENTITY primary key,
	first_name varchar(50) not null,
	last_name varchar(100) not null,
	username varchar(10) not null unique,
	password varchar(10) not null
);

drop table if exists customer;

create table customer (
	id IDENTITY primary key,
	first_name varchar(50) not null,
	last_name varchar(100) not null,
	username varchar(10) not null unique,
	password varchar(10) not null
);

drop table if exists orders;

create table orders (
    id IDENTITY primary key,
	customer_id integer not null references customer(id),
    order_date date not null default current_date
);

drop table if exists product_order;

create table product_order (
    order_id integer not null references orders(id),
    product_id integer not null references product(id),
    quantity integer not null
);


