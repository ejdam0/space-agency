package pl.strzelecki.spaceagency.model.DTO;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Builder;
import lombok.Data;
import pl.strzelecki.spaceagency.model.serializer.LocalDateDeserializer;
import pl.strzelecki.spaceagency.model.serializer.LocalDateSerializer;

import java.time.LocalDate;

@Data
public class ProductByMissionDTO {
    private long id;

    @JsonDeserialize(using = LocalDateDeserializer.class)
    @JsonSerialize(using = LocalDateSerializer.class)
    private LocalDate acquisitionDate;

    private String footprint;

    private double price;

    @JsonIgnore
    private String url;

    @Builder
    public ProductByMissionDTO(long id, LocalDate acquisitionDate, String footprint, double price, String url) {
        this.id = id;
        this.acquisitionDate = acquisitionDate;
        this.footprint = footprint;
        this.price = price;
        this.url = url;
    }
}
