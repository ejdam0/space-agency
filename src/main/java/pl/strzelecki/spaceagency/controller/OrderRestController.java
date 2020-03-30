package pl.strzelecki.spaceagency.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import pl.strzelecki.spaceagency.model.DTO.OrderDTO;
import pl.strzelecki.spaceagency.model.DTO.TopMissionDTO;
import pl.strzelecki.spaceagency.model.DTO.TopProductDTO;
import pl.strzelecki.spaceagency.model.Order;
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
    public ResponseEntity<List<Order>> getAll() {
        return new ResponseEntity<>(orderService.getAllOrders(), HttpStatus.OK);
    }

    @PostMapping("/new")
    public ResponseEntity<?> add(@RequestBody OrderDTO orderDTO) {
        logger.info("Create new order");
        try {
            logger.trace("Calling orderService save the order");
            orderService.create(orderDTO);

        } catch (ResponseStatusException e) {
            logger.error("Exception while trying to save the order: " + e.getMessage());
            return new ResponseEntity<>(e.getMessage(), e.getStatus());

        }
        logger.info("New order added successfully");
        return new ResponseEntity<>("Added new order:\n " + orderDTO, HttpStatus.CREATED);
    }

    @GetMapping("/history")
    public ResponseEntity<?> getOrdersHistoryOfCustomer(@RequestParam(value = "person-id") long id) {
        logger.info("Get history of orders");
        List<Order> result;
        try {
            result = orderService.getOrderHistory(id);
        } catch (ResponseStatusException e) {
            logger.error("Exception while trying to get the order history: " + e.getMessage());
            return new ResponseEntity<>(e.getMessage(), e.getStatus());
        }
        logger.trace("Checking size of results list. Returning responseEntity");
        return result.size() == 0 ? new ResponseEntity<>(HttpStatus.NOT_FOUND) :
                new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping("/top-products")
    public ResponseEntity<?> getMostOrderedProducts() {
        logger.info("Get most ordered products");
        List<TopProductDTO> result;
        try {
            result = orderService.getMostOrderedProducts();
        } catch (ResponseStatusException e) {
            logger.error("Exception while trying to get list of most ordered products: " + e.getMessage());
            return new ResponseEntity<>(e.getMessage(), e.getStatus());
        }
        logger.trace("Checking size of results list. Returning responseEntity");
        return result.size() == 0 ? new ResponseEntity<>(HttpStatus.NOT_FOUND) :
                new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping("/top-missions")
    public ResponseEntity<?> getMostOrderedMissions() {
        logger.info("Get most ordered missions");
        List<TopMissionDTO> result;
        try {
            result = orderService.getMostOrderedMissions();
        } catch (ResponseStatusException e) {
            logger.error("Exception while trying to get list of most ordered missions: " + e.getMessage());
            return new ResponseEntity<>(e.getMessage(), e.getStatus());
        }
        logger.trace("Checking size of results list. Returning responseEntity");
        return result.size() == 0 ? new ResponseEntity<>(HttpStatus.NOT_FOUND) :
                new ResponseEntity<>(result, HttpStatus.OK);
    }
}