package guru.springframework.spring7restmvc.reporsitories;

import guru.springframework.spring7restmvc.entities.Customer;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class CustomerRepositoryTest {

    @Autowired
    private CustomerRepository customerRepository;

    @Test
    void findByBeerName() {
        Customer customer = Customer.builder().customerName("Ray Ray").build();

        customerRepository.save(customer);

        assertThat(customer, notNullValue());
        assertThat(customer.getId(), notNullValue());
        assertThat(customer.getCustomerName(), equalTo("Ray Ray"));
    }
}