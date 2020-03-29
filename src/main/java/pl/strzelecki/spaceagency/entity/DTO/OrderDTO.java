package pl.strzelecki.spaceagency.entity.DTO;

import lombok.Data;
import pl.strzelecki.spaceagency.entity.Customer;

import java.util.Map;

@Data
public class OrderDTO {
    private Customer customer;
    private Map<Long, Integer> productsIdsAndQuantities;
}
