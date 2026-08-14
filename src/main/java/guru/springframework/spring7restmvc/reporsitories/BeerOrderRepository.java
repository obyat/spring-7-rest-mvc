package guru.springframework.spring7restmvc.reporsitories;

import guru.springframework.spring7restmvc.entities.BeerOrder;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;


public interface BeerOrderRepository extends JpaRepository<BeerOrder, UUID> {
}
