package pl.strzelecki.spaceagency.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.strzelecki.spaceagency.entity.Order;

public interface OrderRepository extends JpaRepository<Order, Long> {
}
