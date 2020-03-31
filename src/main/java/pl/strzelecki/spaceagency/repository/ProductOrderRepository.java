package pl.strzelecki.spaceagency.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import pl.strzelecki.spaceagency.model.ProductOrder;

public interface ProductOrderRepository extends JpaRepository<ProductOrder, Long> {
    @Query(value = "INSERT into product_order_copy SELECT * FROM " +
            "product_order WHERE product_id " +
            "NOT IN (select product_id from product_order_copy) " +
            "AND product_id = :id", nativeQuery = true)
    @Modifying
    void backupProductOrder(@Param("id") long productId);

    @Query("DELETE FROM ProductOrder where pk.product.id = :id")
    @Modifying
    void deleteProductOrder(@Param("id") long productId);
}