package guru.springframework.spring7restmvc.controller;

import guru.springframework.spring7restmvc.constants.ApiPaths;
import guru.springframework.spring7restmvc.model.CustomerDTO;
import guru.springframework.spring7restmvc.service.CustomerService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;


@Slf4j
@AllArgsConstructor
@RestController
@RequestMapping(ApiPaths.Customer.ROOT)
public class CustomerController {

  private final CustomerService customerService;


  @GetMapping()
  public List<CustomerDTO> getAllCustomers() {
    return customerService.getAllCustomers();
  }


  @PostMapping
  public ResponseEntity<CustomerDTO> handlePost(@RequestBody CustomerDTO CustomerDTO) {
    CustomerDTO savedCustomerDTO = customerService.saveNewCustomer(CustomerDTO);

    HttpHeaders headers = new HttpHeaders();
    headers.add(
            "Location",
            ApiPaths.Customer.CUSTOMER_WITH_ID.replace(
                    "{customerId}", savedCustomerDTO.getId().toString()));

    return new ResponseEntity<>(savedCustomerDTO, headers, HttpStatus.CREATED);
  }


  @GetMapping(ApiPaths.Customer.BY_ID)
  public CustomerDTO getCustomerById(@PathVariable("customerId") UUID id) {
    log.debug("Getting customer by id in controller: {}", id);

    return customerService.getCustomerById(id).orElseThrow(NotFoundException::new);
  }


  @PutMapping(ApiPaths.Customer.BY_ID)
  public ResponseEntity<CustomerDTO> updateCustomerById(
          @PathVariable UUID customerId, @RequestBody CustomerDTO CustomerDTO) {

    if (customerService.getCustomerById(customerId).isEmpty()) {
      throw new NotFoundException();
    }

    log.debug("Updating customer by id in controller: {}", customerId);
    customerService.updateCustomerById(customerId, CustomerDTO);
    return new ResponseEntity<>(HttpStatus.NO_CONTENT);
  }


  @DeleteMapping(ApiPaths.Customer.BY_ID)
  public ResponseEntity<CustomerDTO> deleteCustomerById(@PathVariable UUID customerId) {
    Boolean deleted = customerService.deleteCustomerById(customerId);
    if (!Boolean.TRUE.equals(deleted)) {
      throw new NotFoundException();
    }
    log.debug("Deleting customer by id in controller: {}", customerId);
    return new ResponseEntity<>(HttpStatus.NO_CONTENT);
  }
}
