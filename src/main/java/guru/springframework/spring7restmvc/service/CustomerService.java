package guru.springframework.spring7restmvc.service;

import guru.springframework.spring7restmvc.model.CustomerDTO;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CustomerService {

  List<CustomerDTO> getAllCustomers();

  Optional<CustomerDTO> getCustomerById(UUID id);

  CustomerDTO saveNewCustomer(CustomerDTO CustomerDTO);

  void updateCustomerById(UUID id, CustomerDTO CustomerDTO);

  void deleteCustomerById(UUID customerId);
}
