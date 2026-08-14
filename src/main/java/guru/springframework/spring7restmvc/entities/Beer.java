package guru.springframework.spring7restmvc.entities;

import guru.springframework.spring7restmvc.model.BeerStyle;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Version;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.annotations.UuidGenerator;
import org.hibernate.type.SqlTypes;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
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
  @JdbcTypeCode(SqlTypes.CHAR)
  @Column(length = 36, columnDefinition = "varchar(36)", updatable = false, nullable = false)
  @UuidGenerator
  private UUID id;

  @Version
  private Integer version;

  @NotNull
  @NotEmpty
  @Size(max = 50)
  // Size constraint validation is good because it checks before @Column writes to database with a
  // column size
  @Column(length = 50)
  private String beerName;

  @Enumerated(EnumType.STRING)
  @JdbcTypeCode(SqlTypes.SMALLINT)
  private BeerStyle beerStyle;

  @NotNull
  @NotEmpty
  @Size(max = 255)
  private String upc;

  private Integer quantityOnHand;
  @NotNull
  private BigDecimal price;
  @CreationTimestamp
  private LocalDateTime createdDate;
  @UpdateTimestamp
  private LocalDateTime updatedDate;

  @ManyToMany
  @Builder.Default
  @JoinTable(
          name = "beer_category",
          joinColumns = @JoinColumn(name = "beer_id"),
          inverseJoinColumns = @JoinColumn(name = "category_id")
  )
  private Set<Category> categories = new HashSet<>();
  @OneToMany(mappedBy = "beer")
  private Set<BeerOrderLine> beerOrderLines;


  public void addCategory(Category category) {
    this.categories.add(category);
    category.getBeers().add(this);
  }


  public void removeCategory(Category category) {
    this.categories.remove(category);
    category.getBeers().remove(this);
  }
}
