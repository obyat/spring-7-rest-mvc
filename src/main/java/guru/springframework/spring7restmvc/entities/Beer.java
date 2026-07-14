package guru.springframework.spring7restmvc.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.annotations.UuidGenerator;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Beer {

  @Id
  @GeneratedValue(generator = "UUID")
  @Column(length = 36, columnDefinition = "varchar(36)", updatable = false, nullable = false)
  @UuidGenerator
  private UUID id;
  @Version
  private Integer version;
  @NotNull
  @NotEmpty
  @Size(max = 50) // Size constraint validation is good because it checks before @Column writes to database with a column size
  @Column(length = 50)
  private String beerName;
  @NotNull
  //  private BeerStyle beerStyle;
  @NotNull
  @NotEmpty
  @Size(max = 255)
  private String upc;
  private Integer quantityOnHand;
  @NotNull
  private BigDecimal price;
  private LocalDateTime createdDate;
  private LocalDateTime updatedDate;
}
