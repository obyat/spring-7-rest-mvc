package guru.springframework.spring7restmvc.controller;

import guru.springframework.spring7restmvc.entities.Customer;
import guru.springframework.spring7restmvc.entities.Customer;
import guru.springframework.spring7restmvc.mappers.CustomerMapper;
import guru.springframework.spring7restmvc.model.CustomerDTO;
import guru.springframework.spring7restmvc.model.CustomerDTO;
import guru.springframework.spring7restmvc.reporsitories.CustomerRepository;
import jakarta.transaction.Transactional;
import org.assertj.core.api.AssertionsForClassTypes;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.Rollback;

import java.util.List;
import java.util.Objects;
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

    @Autowired
    CustomerMapper customerMapper;


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
        assertThat(customerController.getAllCustomers().size(), equalTo(3));
    }


    @Transactional
    @Rollback
    @Test
    void testListAllEmptyList() {
        customerRepository.deleteAll();

        assertThat(customerController.getAllCustomers().size(), equalTo(0));

    }


    @Transactional // rolls back db to original undeleted state
    @Test
    void testEmptyCustomers() {
        customerRepository.deleteAll();
        List<CustomerDTO> dtos = customerController.getAllCustomers();

        AssertionsForClassTypes.assertThat(dtos.size()).isEqualTo(0);
    }


    @Transactional
    @Rollback
    @Test
    void saveNewCustomerTest() {
        CustomerDTO customerDTO = CustomerDTO.builder().customerName("Kevin").build();

        ResponseEntity<CustomerDTO> responseEntity = customerController.handlePost(customerDTO);

        AssertionsForClassTypes.assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.valueOf(201));
        AssertionsForClassTypes.assertThat(responseEntity.getHeaders().getLocation()).isNotNull();

        String[] locationUUID = Objects.requireNonNull(responseEntity.getHeaders().getLocation()).getPath().split("/");
        UUID savedUUID = UUID.fromString(locationUUID[4]);

        Customer customer = customerRepository.findById(savedUUID).get();

        AssertionsForClassTypes.assertThat(customer.getCustomerName()).isEqualTo(customerDTO.getCustomerName());
        AssertionsForClassTypes.assertThat(customer.getId()).isEqualTo(savedUUID);
    }


    @Test
    void testUpdateExistingCustomer() {
        Customer customer = customerRepository.findAll().get(0);
        CustomerDTO customerDTO = customerMapper.customerToCustomerDto(customer);
        customerDTO.setId(null);
        customerDTO.setVersion(null);
        final String customerName = "UPDATED";

        AssertionsForClassTypes.assertThat(customerDTO.getCustomerName()).isNotEqualTo(customerName);


        customerDTO.setCustomerName(customerName);

        ResponseEntity<CustomerDTO> responseEntity = customerController.updateCustomerById(customer.getId(), customerDTO);
        AssertionsForClassTypes.assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.valueOf(204));

        Customer updatedCustomer = customerRepository.findById(customer.getId()).get();
        AssertionsForClassTypes.assertThat(updatedCustomer.getCustomerName()).isEqualTo(customerName);
    }


    @Test
    void testUpdateNoteFound() {
        assertThrows((NotFoundException.class), () -> customerController.updateCustomerById(UUID.randomUUID(), CustomerDTO.builder().build()));
    }


    @Transactional
    @Rollback
    @Test
    void deleteByIdNotFound() {
        Customer customer = customerRepository.findAll().get(0);

        ResponseEntity<CustomerDTO> responseEntity = customerController.deleteCustomerById(customer.getId());
        Customer foundCustomer = customerRepository.findById(customer.getId()).orElse(null);

        AssertionsForClassTypes.assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.valueOf(204));
        AssertionsForClassTypes.assertThat(foundCustomer).isNull();
    }



}