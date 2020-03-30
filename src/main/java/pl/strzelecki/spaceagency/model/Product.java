package pl.strzelecki.spaceagency.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import pl.strzelecki.spaceagency.model.serializer.LocalDateDeserializer;
import pl.strzelecki.spaceagency.model.serializer.LocalDateSerializer;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "product")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;

    @ManyToOne(cascade = {CascadeType.MERGE, CascadeType.DETACH, CascadeType.REFRESH})
    @JoinColumn(name = "mission_id", nullable = false)
    private Mission mission;

    @Column(name = "acquisition_date", nullable = false)
    @JsonDeserialize(using = LocalDateDeserializer.class)
    @JsonSerialize(using = LocalDateSerializer.class)
    private LocalDate acquisitionDate;

    @Column(name = "footprint", nullable = false)
    private String footprint;

    @Column(name = "price", nullable = false)
    private double price;

    @Column(name = "url", nullable = false)
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String url;
}
