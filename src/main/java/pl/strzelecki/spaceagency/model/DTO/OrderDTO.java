package pl.strzelecki.spaceagency.model.DTO;

import lombok.Data;
import pl.strzelecki.spaceagency.model.Person;

import java.util.Map;

@Data
public class OrderDTO {
    private Person person;
    private Map<Long, Integer> productsIdsAndQuantities;
}
