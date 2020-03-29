package pl.strzelecki.spaceagency.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.strzelecki.spaceagency.entity.ProductOrder;

public interface ProductOrderRepository extends JpaRepository<ProductOrder, Long> {
}