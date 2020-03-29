package pl.strzelecki.spaceagency.service;

import pl.strzelecki.spaceagency.entity.DTO.OrderDTO;
import pl.strzelecki.spaceagency.entity.DTO.TopMissionDTO;
import pl.strzelecki.spaceagency.entity.DTO.TopProductDTO;
import pl.strzelecki.spaceagency.entity.Order;

import java.util.List;

public interface OrderService {

    List<Order> getAllOrders();

    void create(OrderDTO order);

    List<Order> getOrderHistory(long id);

    List<TopProductDTO> getMostOrderedProducts();

    List<TopMissionDTO> getMostOrderedMissions();
}
