package pl.strzelecki.spaceagency.model.DTO;

import lombok.Data;

@Data
public class TopProductDTO {
    private long productId;
    private Long quantity;

    public TopProductDTO(long productId, Long quantity) {
        this.productId = productId;
        this.quantity = quantity;
    }
}
