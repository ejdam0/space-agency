package pl.strzelecki.spaceagency.service;

import pl.strzelecki.spaceagency.entity.Order;

import java.util.List;

public interface OrderService {

    List<Order> findAll();

    Order create(Order order);

    void update(Order order);
}
