package pl.strzelecki.spaceagency.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.strzelecki.spaceagency.entity.Order;
import pl.strzelecki.spaceagency.service.OrderService;

@RestController
@RequestMapping("/orders")
public class OrderRestController {

    private OrderService orderService;
}
