package pl.strzelecki.spaceagency.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.strzelecki.spaceagency.entity.Order;
import pl.strzelecki.spaceagency.service.OrderService;

import java.util.List;

@RestController
@RequestMapping("/orders")
public class OrderRestController {

    private static final Logger logger = LogManager.getLogger(OrderRestController.class);

    private OrderService orderService;

    @Autowired
    public OrderRestController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping("/all")
    public ResponseEntity<List<Order>> findAll() {
        logger.info("Find all orders");
        logger.trace("Calling orderService do find all orders");
        return new ResponseEntity<>(orderService.findAll(), HttpStatus.OK);
    }

    @PostMapping("/new/{customerId}")
    public ResponseEntity<?> add(@PathVariable("customerId") long customerId) {

        return new ResponseEntity<>("Added new order:\n ", HttpStatus.CREATED);
    }
}