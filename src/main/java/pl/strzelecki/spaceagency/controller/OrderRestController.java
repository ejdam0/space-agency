package pl.strzelecki.spaceagency.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.strzelecki.spaceagency.entity.Order;
import pl.strzelecki.spaceagency.service.OrderService;

import java.util.List;

@RestController
@RequestMapping("/orders")
public class OrderRestController {

    private OrderService orderService;

    @Autowired
    public OrderRestController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping("/all")
    public List<Order> findAll() {
        return orderService.findAll();
    }
}
