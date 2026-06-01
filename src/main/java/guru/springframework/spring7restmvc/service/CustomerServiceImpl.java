package guru.springframework.spring7restmvc.service;

import guru.springframework.spring7restmvc.model.Customer;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class CustomerServiceImpl implements CustomerService {

  private final Map<UUID, Customer> customerMap;

  public CustomerServiceImpl() {
    this.customerMap = new HashMap<>();

    Customer customer1 =
        Customer.builder().Id(UUID.randomUUID()).customerName("Customer 1").version(1).build();

    Customer customer2 =
        Customer.builder().Id(UUID.randomUUID()).customerName("Customer 2").version(2).build();

    Customer customer3 =
        Customer.builder().Id(UUID.randomUUID()).customerName("Customer 3").version(3).build();

    this.customerMap.put(customer1.getId(), customer1);
    this.customerMap.put(customer2.getId(), customer2);
    this.customerMap.put(customer3.getId(), customer3);
  }

  @Override
  public List<Customer> getAllCustomers() {
    return List.of(this.customerMap.values().toArray(new Customer[0]));
  }

  @Override
  public Customer getCustomerById(UUID id) {
    return this.customerMap.get(id);
  }

  @Override
  public Customer saveNewCustomer(Customer customer) {
    Customer savedCustomer =
        Customer.builder()
            .Id(UUID.randomUUID())
            .customerName(customer.getCustomerName())
            .version(1)
            .build();
    this.customerMap.put(savedCustomer.getId(), savedCustomer);
    return savedCustomer;
  }

  @Override
  public void updateCustomerById(UUID id, Customer customer) {
    Customer existingCustomer = this.customerMap.get(id);
    existingCustomer.setCustomerName(customer.getCustomerName());
    existingCustomer.setVersion(customer.getVersion());
    this.customerMap.put(id, existingCustomer);
  }

  @Override
  public void deleteCustomerById(UUID customerId) {
    this.customerMap.remove(customerId);
  }
}
