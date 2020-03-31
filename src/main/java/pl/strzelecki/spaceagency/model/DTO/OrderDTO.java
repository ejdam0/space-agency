package pl.strzelecki.spaceagency.model.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import pl.strzelecki.spaceagency.model.Person;

import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderDTO {
    private Person person;
    private Map<Long, Integer> productsIdsAndQuantities;
}
