package pl.strzelecki.spaceagency.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.URL;
import pl.strzelecki.spaceagency.entity.serializer.LocalDateDeserializer;
import pl.strzelecki.spaceagency.entity.serializer.LocalDateSerializer;

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
    @URL
    @JsonIgnore
    private String url;
}
