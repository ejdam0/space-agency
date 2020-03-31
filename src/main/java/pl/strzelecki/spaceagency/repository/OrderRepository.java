package pl.strzelecki.spaceagency.repository;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import pl.strzelecki.spaceagency.model.DTO.TopMissionDTO;
import pl.strzelecki.spaceagency.model.DTO.TopProductDTO;
import pl.strzelecki.spaceagency.model.Order;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {
    @EntityGraph(value = "Order.op", type = EntityGraph.EntityGraphType.LOAD)
    @Query("SELECT o FROM Order o JOIN FETCH o.orderedProducts WHERE o.person.id = :personId")
    List<Order> getOrderHistory(@Param("personId") long id);

    @Query("SELECT new pl.strzelecki.spaceagency.model.DTO.TopProductDTO" +
            "(po.pk.product.id, SUM(po.quantity)) " +
            "FROM ProductOrder po group by po.pk.product order by SUM(po.quantity) desc")
    List<TopProductDTO> getMostOrderedProducts();

    @Query("SELECT new pl.strzelecki.spaceagency.model.DTO.TopMissionDTO" +
            "(po.pk.product.mission.id, COUNT(po.pk.product)) " +
            "FROM ProductOrder po group by po.pk.product.mission order by COUNT(po.pk.product) desc")
    List<TopMissionDTO> getMostOrderedMissions();
}