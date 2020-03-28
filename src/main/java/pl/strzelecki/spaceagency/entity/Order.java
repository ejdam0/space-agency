package pl.strzelecki.spaceagency.entity;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import pl.strzelecki.spaceagency.entity.serializer.LocalDateDeserializer;
import pl.strzelecki.spaceagency.entity.serializer.LocalDateSerializer;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "orders")
@Data
@NoArgsConstructor
@AllArgsConstructor
@NamedEntityGraph(name = "Order.op",
    attributeNodes = @NamedAttributeNode("orderedProducts"))
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;

    @ManyToOne(cascade = {CascadeType.MERGE, CascadeType.DETACH, CascadeType.REFRESH}, fetch = FetchType.EAGER)
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;

    @Column(name = "order_date", nullable = false)
    @JsonDeserialize(using = LocalDateDeserializer.class)
    @JsonSerialize(using = LocalDateSerializer.class)
    private LocalDate orderDate;

    @OneToMany(mappedBy = "pk.order",
            fetch = FetchType.LAZY,
            cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.REFRESH, CascadeType.PERSIST})
    List<ProductOrder> orderedProducts;

    @Transient
    public double getTotalOrderPrice() {
        List<ProductOrder> orderedProducts = this.getOrderedProducts();
        return orderedProducts.stream()
                .mapToDouble(ProductOrder::getTotalPrice)
                .sum();
    }

    @Transient
    public int getNumberOfProducts() {
        return this.orderedProducts.size();
    }
}
