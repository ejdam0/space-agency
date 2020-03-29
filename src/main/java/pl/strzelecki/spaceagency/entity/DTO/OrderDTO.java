package pl.strzelecki.spaceagency.entity.DTO;

import lombok.Data;
import pl.strzelecki.spaceagency.entity.Person;

import java.util.Map;

@Data
public class OrderDTO {
    private Person person;
    private Map<Long, Integer> productsIdsAndQuantities;
}
