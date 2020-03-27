package pl.strzelecki.spaceagency.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.strzelecki.spaceagency.entity.Order;
import pl.strzelecki.spaceagency.repository.OrderRepository;
import pl.strzelecki.spaceagency.service.OrderService;

import java.util.List;

@Service
public class OrderServiceImpl implements OrderService {

    private OrderRepository orderRepository;

    @Autowired
    public OrderServiceImpl(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Override
    public List<Order> findAll() {
        return orderRepository.findAll();
    }
}
