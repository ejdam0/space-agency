package pl.strzelecki.spaceagency.service.impl;

import com.google.common.collect.Lists;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import pl.strzelecki.spaceagency.model.DTO.OrderDTO;
import pl.strzelecki.spaceagency.model.DTO.TopMissionDTO;
import pl.strzelecki.spaceagency.model.DTO.TopProductDTO;
import pl.strzelecki.spaceagency.model.*;
import pl.strzelecki.spaceagency.repository.OrderRepository;
import pl.strzelecki.spaceagency.repository.PersonRepository;
import pl.strzelecki.spaceagency.service.OrderService;
import pl.strzelecki.spaceagency.service.ProductService;

import java.time.LocalDate;
import java.util.*;

@Service
@Transactional
public class OrderServiceImpl implements OrderService {

    private static final Logger logger = LogManager.getLogger(OrderServiceImpl.class);

    private OrderRepository orderRepository;
    private ProductService productService;
    private PersonRepository personRepository;

    @Autowired
    public OrderServiceImpl(OrderRepository orderRepository,
                            PersonRepository personRepository,
                            ProductService productService) {
        this.orderRepository = orderRepository;
        this.personRepository = personRepository;
        this.productService = productService;
    }

    @Override
    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    @Override
    public void create(OrderDTO orderDTO) {
        // save order to get it's id from db
        logger.info("Create new order");
        logger.trace("Creating new order that will be saved in database");
        Order placedOrder = new Order();
        // get person from db
        logger.trace("Getting person provided in order, from database");
        Optional<Person> optCustomerInDb = personRepository.findById(orderDTO.getPerson().getId());
        logger.trace("Checking if person exists in database");
        if (optCustomerInDb.isEmpty()) {
            logger.info("Person does not exist in the database");
            logger.error("Exception while searching for person - person does not exist");
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "This person does not exist in the database.");
        } else if (optCustomerInDb.get().getAuthority().equals("ROLE_CM")) {
            logger.trace("Checking if person with given id is either a customer or a content manager");
            logger.error("Exception - person with that id is a content manager");
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "That person is a content manager and not a customer.");
        }
        logger.trace("Person exists in database");
        Person person = optCustomerInDb.get();
        // set person and date
        logger.trace("Setting new order's person and date");
        placedOrder.setPerson(person);
        placedOrder.setOrderDate(LocalDate.now());

        // save order to database
        logger.trace("Saving new order to database");
        Order newlySavedOrder = orderRepository.save(placedOrder);

        // get list of products ids
        logger.info("Creating map of corresponding products and their quantities");
        Map<Long, Integer> productsIdsAndQuantities = orderDTO.getProductsIdsAndQuantities();

        logger.trace("Filling list of products and list of quantities");
        List<Long> listOfProductsIds = Lists.newArrayList(productsIdsAndQuantities.keySet());
        List<Integer> listOfQuantities = Lists.newArrayList(productsIdsAndQuantities.values());

        // create list of new product order pk to associate order id and product id
        logger.info("Creating list of productOrderPks and setting order");
        List<ProductOrderPK> productOrderPKs = setOrderToAllPks(orderDTO, newlySavedOrder);

        logger.info("Creating list of products according to list of id's");
        List<Product> productsList;
        try {
            productsList = getProductsList(listOfProductsIds);
        } catch (NoSuchElementException e) {
            logger.error("Exception - product type is invalid");
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Product not found in database.");
        }

        // fill list of productOrderPKs with list of products
        logger.trace("Filling list of productOrderPKs with products");
        for (int i = 0; i < productsList.size(); i++) {
            productOrderPKs.get(i).setProduct(productsList.get(i));
        }

        // create multiple productOrders, set quantities, save all
        logger.info("Filling list of productOrders with productOrderPks and corresponding quantities");
        List<ProductOrder> productOrders = fillListWithProductOrderPKsAndQuantities(listOfQuantities, productOrderPKs);

        logger.trace("Setting productOrders to the order");
        newlySavedOrder.setOrderedProducts(productOrders);

        logger.info("Saving list of productOrders to database");
        orderRepository.save(newlySavedOrder);
    }

    @Override
    public List<Order> getOrderHistory(long id) {
        logger.info("Get order history of a customer");
        logger.trace("Checking if person with given id is either a customer or a content manager");
        Optional<Person> personFromDb = personRepository.findById(id);
        if (personFromDb.isPresent() && personFromDb.get().getAuthority().equals("ROLE_CM")) {
            logger.error("Exception - person with that id is a content manager");
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "That person is a content manager.");
        }
        logger.info("Returning result");
        return orderRepository.getOrderHistory(id);
    }

    @Override
    public List<TopProductDTO> getMostOrderedProducts() {
        logger.info("Get most ordered products");
        return orderRepository.getMostOrderedProducts();
    }

    @Override
    public List<TopMissionDTO> getMostOrderedMissions() {
        logger.info("Get most ordered missions");
        return orderRepository.getMostOrderedMissions();
    }

    private List<ProductOrder> fillListWithProductOrderPKsAndQuantities(
            List<Integer> listOfQuantities, List<ProductOrderPK> productOrderPKs) {
        logger.trace("Creating list of productOrders");
        List<ProductOrder> productOrders = new ArrayList<>();
        logger.trace("Filling list with productOrders and quantities");
        for (int i = 0; i < productOrderPKs.size(); i++) {
            productOrders.add(
                    new ProductOrder(productOrderPKs.get(i), listOfQuantities.get(i)));
        }
        logger.info("Returning list of productOrders");
        return productOrders;
    }

    private List<Product> getProductsList(List<Long> listOfProductsIds) {
        // create empty list of Products to populate it according to list of ids
        logger.trace("Creating list of products");
        List<Product> productsList = new ArrayList<>();
        logger.trace("Filling list with products");
        for (Long productsId : listOfProductsIds) {
            productsList.add(productService.getProduct(productsId));
        }
        logger.info("Returning list of products");
        return productsList;
    }

    private List<ProductOrderPK> setOrderToAllPks(OrderDTO orderDTO, Order newlySavedOrder) {
        // create list of new product order pk to associate order id and product id
        logger.trace("Creating list of productOrderPKs");
        List<ProductOrderPK> productOrderPKs = new ArrayList<>();
        // set order id to all the productOrderPKs
        logger.trace("Filling list with order id");
        for (int i = 0; i < orderDTO.getProductsIdsAndQuantities().size(); i++) {
            productOrderPKs.add(new ProductOrderPK(newlySavedOrder));
        }
        logger.info("Returning list of productOrderPKs");
        return productOrderPKs;
    }
}
