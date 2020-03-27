insert into mission(name, imagery_type, start_date, finish_date)
values 
('Apollo', 0, '2020-03-16', '2025-01-02'),
('Ranger', 1, '2000-06-23', '2030-10-15'),
('Pioneer', 2, '2015-12-14', '2020-03-05');
insert into product(mission_id, acquisition_date, footprint, price, url)
values
(1, '2020-03-20','35.2107659537729871,11.2285533486298225,35.2106780222360566,11.2282643420209139', 3000, 'http://www.product1.com'),
(2, '2020-04-21','40.2107659537729871,16.2285533486298225,40.2106780222360566,16.2282643420209139', 5000, 'http://www.product2.com'),
(3, '2020-05-22','45.2107659537729871,21.2285533486298225,45.2106780222360566,21.2282643420209139', 7000, 'http://www.product3.com');
insert into content_manager(first_name, last_name, username, password)
values 
('Piotr', 'Nowak', 'pnowak', 'nowakp'),
('Jan', 'Kowalski', 'jkowalski', 'kowalskij');
insert into customer(first_name, last_name, username, password)
values ('Albert', 'Marciniak','amarciniak', 'marciniaka'),
('Henryk','Krupa','hkrupa', 'krupah');
insert into orders (customer_id, order_date)
values (2, '2020-03-20'),
(2, '2020-02-11'),
(1, '2020-03-12'),
(2, '2020-04-13');
insert into product_order (order_id, product_id, quantity)
values (1, 1, 10),
(2,2,20),
(3,3,30),
(3,1,40),
(4,3,10),
(4,2,23);