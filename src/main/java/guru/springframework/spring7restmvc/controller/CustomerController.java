package guru.springframework.spring7restmvc.controller;


import guru.springframework.spring7restmvc.model.Beer;
import guru.springframework.spring7restmvc.model.Customer;
import guru.springframework.spring7restmvc.service.CustomerService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;


@Slf4j
@AllArgsConstructor
@RestController
@RequestMapping("/api/v1/customer")
public class CustomerController {

    private final CustomerService customerService;

    @RequestMapping(method = RequestMethod.GET)
    public List<Customer> getAllCustomers() {
        return customerService.getAllCustomers();
    }

    @PostMapping
    public ResponseEntity<Customer> handlePost(@RequestBody Customer customer) {
        Customer savedCustomer = customerService.saveNewCustomer(customer);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Location", "/api/v1/customer/" + savedCustomer.getId().toString());

        return new ResponseEntity<>(savedCustomer, headers, HttpStatus.CREATED);
    }


    @RequestMapping(value = "{customerId}", method = RequestMethod.GET)
    public Customer getCustomerById(@PathVariable("customerId") UUID id) {
        log.debug("Getting customer by id in controller: {}", id);

        return customerService.getCustomerById(id);
    }

    @PutMapping("{customerId}")
    public ResponseEntity<Customer> updateCustomerById(@PathVariable UUID customerId, @RequestBody Customer customer) {
        log.debug("Updating customer by id in controller: {}", customerId);
        customerService.updateCustomerById(customerId, customer);
        return new ResponseEntity<Customer>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping("{customerId}")
    public ResponseEntity<Customer> deleteCustomerById(@PathVariable UUID customerId) {
        log.debug("Deleting customer by id in controller: {}", customerId);
        this.customerService.deleteCustomerById(customerId);
        return new ResponseEntity<Customer>(HttpStatus.NO_CONTENT);
    }
}