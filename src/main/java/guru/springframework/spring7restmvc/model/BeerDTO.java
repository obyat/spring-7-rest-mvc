package guru.springframework.spring7restmvc.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;


@Builder
@Data
@AllArgsConstructor
public class BeerDTO {
  private UUID id;
  private Integer version;
  @NotBlank
  @NotNull
  private String beerName;
  @NotNull
  private BeerStyle beerStyle;
  @NotBlank
  @NotNull
  private String upc;
  private Integer quantityOnHand;
  @NotNull
  private BigDecimal price;
  private LocalDateTime createdDate;
  private LocalDateTime updatedDate;
}
