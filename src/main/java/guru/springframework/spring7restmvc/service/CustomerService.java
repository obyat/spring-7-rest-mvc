package guru.springframework.spring7restmvc.service;

import guru.springframework.spring7restmvc.model.Customer;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CustomerService {

  List<Customer> getAllCustomers();

  Optional<Customer> getCustomerById(UUID id);

  Customer saveNewCustomer(Customer customer);

  void updateCustomerById(UUID id, Customer customer);

  void deleteCustomerById(UUID customerId);
}
