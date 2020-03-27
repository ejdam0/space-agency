package pl.strzelecki.spaceagency.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "product_order")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductOrder {

    @EmbeddedId
    @JsonIgnore
    private ProductOrderPK pk;

//    @Column(name = "order_id")
//    private Order order;
//
//    @Column(name = "product_id")
//    private Product product;

    @Column(name = "quantity", nullable = false)
    private int quantity;

    public ProductOrder(Order order, Product product, Integer quantity) {
        pk = new ProductOrderPK();
        pk.setOrder(order);
        pk.setProduct(product);
        this.quantity = quantity;
    }
}
