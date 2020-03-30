package pl.strzelecki.spaceagency.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicUpdate;
import org.javers.core.metamodel.annotation.DiffIgnore;
import pl.strzelecki.spaceagency.model.serializer.LocalDateDeserializer;
import pl.strzelecki.spaceagency.model.serializer.LocalDateSerializer;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "mission")
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@DynamicUpdate
public class Mission {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    @DiffIgnore
    private long id;

    @Column(name = "name", unique = true, nullable = false)
    private String name;

    @Column(name = "imagery_type")
    @DiffIgnore
    private ImageryTypeEnum imageryType;

    @Column(name = "start_date", nullable = false)
    @JsonDeserialize(using = LocalDateDeserializer.class)
    @JsonSerialize(using = LocalDateSerializer.class)
    @DiffIgnore
    private LocalDate startDate;

    @Column(name = "finish_date", nullable = false)
    @JsonDeserialize(using = LocalDateDeserializer.class)
    @JsonSerialize(using = LocalDateSerializer.class)
    @DiffIgnore
    private LocalDate finishDate;
}
