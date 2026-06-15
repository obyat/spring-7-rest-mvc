package guru.springframework.spring7restmvc.model;

import java.time.LocalDateTime;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
@AllArgsConstructor
public class CustomerDTO {
  private UUID Id;
  private String customerName;
  private Integer version;
  private LocalDateTime createdDate;
  private LocalDateTime lastModifiedDate;
}
