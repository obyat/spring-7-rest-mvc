package guru.springframework.spring7restmvc.controller;

import guru.springframework.spring7restmvc.entities.Customer;
import guru.springframework.spring7restmvc.model.CustomerDTO;
import guru.springframework.spring7restmvc.reporsitories.CustomerRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;

import java.util.UUID;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
class CustomerControllerIT {

    @Autowired
    CustomerRepository customerRepository;

    @Autowired
    CustomerController customerController;

    @Test
    void testGetById() {
        Customer customer = customerRepository.findAll().get(0);
        CustomerDTO customerDTO = customerController.getCustomerById(customer.getId());

        assertThat(customerDTO.getId(), equalTo(customer.getId()));
    }


    @Test
    void testGetByIdNotFound() {

        assertThrows(NotFoundException.class, () -> {
            customerController.getCustomerById(UUID.randomUUID());
        });
    }


    @Test
    void testGetAllCustomers() {
        assertThat(customerController.getAllCustomers().size(), equalTo(5));
    }


    @Transactional
    @Rollback
    @Test
    void testListAllEmptyList() {
        customerRepository.deleteAll();

        assertThat(customerController.getAllCustomers().size(), equalTo(0));

    }
}