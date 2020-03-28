package pl.strzelecki.spaceagency.repository;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import pl.strzelecki.spaceagency.entity.Order;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {

    @EntityGraph(value = "Order.op", type = EntityGraph.EntityGraphType.LOAD)
    List<Order> findAll();
}