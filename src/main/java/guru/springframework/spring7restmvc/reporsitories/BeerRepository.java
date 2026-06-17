package guru.springframework.spring7restmvc.reporsitories;

import guru.springframework.spring7restmvc.entities.Beer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface BeerRepository extends JpaRepository<Beer, UUID> {
}
