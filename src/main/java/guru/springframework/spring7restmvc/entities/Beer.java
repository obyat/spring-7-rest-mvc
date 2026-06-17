package guru.springframework.spring7restmvc.entities;

import guru.springframework.spring7restmvc.model.BeerStyle;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;
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
  private String beerName;
//  private BeerStyle beerStyle;
  private String upc;
  private Integer quantityOnHand;
  private BigDecimal price;
  private LocalDateTime createdDate;
  private LocalDateTime updatedDate;
}
