package guru.springframework.spring7restmvc.entities;

import java.time.LocalDateTime;
import java.util.UUID;

public class Customer {
  private UUID Id;
  private String customerName;
  private Integer version;
  private LocalDateTime createdDate;
  private LocalDateTime lastModifiedDate;
}
