package guru.springframework.spring7restmvc.reporsitories;

import guru.springframework.spring7restmvc.entities.Beer;
import guru.springframework.spring7restmvc.entities.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface CustomerRepository extends JpaRepository<Customer, UUID> {

}
