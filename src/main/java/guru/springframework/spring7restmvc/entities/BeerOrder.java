package guru.springframework.spring7restmvc.entities;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Version;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.annotations.UuidGenerator;

import java.sql.Timestamp;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class BeerOrder {

  @Id
  @GeneratedValue(generator = "UUID")
  @UuidGenerator
  @Column(length = 36, columnDefinition = "varchar(36)", updatable = false, nullable = false)
  private UUID id;
  @OneToOne(cascade = CascadeType.PERSIST)
  private BeerOrderShipment beerOrderShipment;

  @Version
  private Long version;
  @Builder.Default
  private Integer orderQuantity = 0;

  @CreationTimestamp
  @Column(updatable = false)
  private Timestamp createdDate;

  @UpdateTimestamp
  private Timestamp lastModifiedDate;
  @Builder.Default
  private Integer quantityAllocated = 0;
  private String customerRef;
  @OneToMany(mappedBy = "beerOrder")
  @Builder.Default
  private Set<BeerOrderLine> beerOrderLines = new HashSet<>();

  @ManyToOne
  private Customer customer;

  public BeerOrder(Set<BeerOrderLine> beerOrderLines, Timestamp createdDate, Customer customer, String customerRef,
                   UUID id, Timestamp lastModifiedDate, Integer orderQuantity, Integer quantityAllocated,
                   Long version, BeerOrderShipment beerOrderShipment) {
    this.beerOrderLines = beerOrderLines;
    this.createdDate = createdDate;
    this.setCustomer(customer);
    this.setBeerOrderShipment(beerOrderShipment);
    this.customerRef = customerRef;
    this.id = id;
    this.lastModifiedDate = lastModifiedDate;
    this.orderQuantity = orderQuantity;
    this.quantityAllocated = quantityAllocated;
    this.version = version;
  }


  public void setCustomer(Customer customer) {
    this.customer = customer;
    customer.getBeerOrders().add(this);
  }


  public void setBeerOrderShipment(BeerOrderShipment beerOrderShipment) {
    this.beerOrderShipment = beerOrderShipment;
    beerOrderShipment.setBeerOrder(this);
  }


  public boolean isNew() {
    return this.id == null;
  }
}