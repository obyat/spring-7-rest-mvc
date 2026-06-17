package guru.springframework.spring7restmvc.entities;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.UuidGenerator;

import java.time.LocalDateTime;
import java.util.UUID;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Customer {
  @Id
  @GeneratedValue(generator = "UUID")
  @Column(length = 36, columnDefinition = "varchar(36)", updatable = false, nullable = false)
  @UuidGenerator
  private UUID Id;
  private String customerName;
  @Version
  private Integer version;
  private LocalDateTime createdDate;
  private LocalDateTime lastModifiedDate;
}
