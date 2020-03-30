package pl.strzelecki.spaceagency.service;

import pl.strzelecki.spaceagency.model.DTO.OrderDTO;
import pl.strzelecki.spaceagency.model.DTO.TopMissionDTO;
import pl.strzelecki.spaceagency.model.DTO.TopProductDTO;
import pl.strzelecki.spaceagency.model.Order;

import java.util.List;

public interface OrderService {

    List<Order> getAllOrders();

    void create(OrderDTO order);

    List<Order> getOrderHistory(long id);

    List<TopProductDTO> getMostOrderedProducts();

    List<TopMissionDTO> getMostOrderedMissions();
}
