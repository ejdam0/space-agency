package pl.strzelecki.spaceagency.entity.DTO;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Data;
import pl.strzelecki.spaceagency.entity.serializer.LocalDateDeserializer;
import pl.strzelecki.spaceagency.entity.serializer.LocalDateSerializer;

import java.time.LocalDate;

@Data
public class ProductByTypeOrDateDTO {
    private long id;

    private String missionName;

    @JsonDeserialize(using = LocalDateDeserializer.class)
    @JsonSerialize(using = LocalDateSerializer.class)
    private LocalDate acquisitionDate;

    private String footprint;

    private double price;

    private String url;

    public ProductByTypeOrDateDTO(long id, LocalDate acquisitionDate, String footprint, double price, String url, String missionName) {
        this.id = id;
        this.acquisitionDate = acquisitionDate;
        this.footprint = footprint;
        this.price = price;
        this.url = url;
        this.missionName = missionName;
    }
}
