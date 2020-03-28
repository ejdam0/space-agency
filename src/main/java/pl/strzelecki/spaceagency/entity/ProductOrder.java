package pl.strzelecki.spaceagency.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "product_order")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductOrder {

    @EmbeddedId
    @JsonIgnore
    private ProductOrderPK pk;

    @Column(name = "quantity", nullable = false)
    private int quantity;

    public ProductOrder(Order order, Product product, int quantity) {
        pk = new ProductOrderPK();
        pk.setOrder(order);
        pk.setProduct(product);
        this.quantity = quantity;
    }

    @Transient
    public Product getProduct() {
        return this.pk.getProduct();
    }

    @Transient
    public double getTotalPrice() {
        return this.getProduct().getPrice() * this.getQuantity();
    }
}
