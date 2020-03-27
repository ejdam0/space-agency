package pl.strzelecki.spaceagency.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Table;

@Embeddable
@Table(name = "product_order")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductOrder {

    @Column(name = "order_id")
    private Order order;

    @Column(name = "product_id")
    private Product product;

    @Column(name = "quantity", nullable = false)
    private int quantity;
}
