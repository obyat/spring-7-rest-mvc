package guru.springframework.spring7restmvc.service;

import guru.springframework.spring7restmvc.mappers.CustomerMapper;
import guru.springframework.spring7restmvc.model.BeerDTO;
import guru.springframework.spring7restmvc.model.CustomerDTO;
import guru.springframework.spring7restmvc.reporsitories.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import static java.util.Arrays.stream;

@Service
@RequiredArgsConstructor
@Primary
public class CustomerServiceJPA implements CustomerService {
    private final CustomerRepository customerRepository;
    private final CustomerMapper customerMapper;


    @Override
    public List<CustomerDTO> getAllCustomers() {
        return customerRepository.findAll()
                .stream()
                .map(customerMapper::customerToCustomerDto)
                .collect(Collectors.toList());
    }


    @Override
    public Optional<CustomerDTO> getCustomerById(UUID id) {
            return Optional.ofNullable(
                    customerMapper.customerToCustomerDto(
                            customerRepository
                                    .findById(id)
                                    .orElse(null)));
        }



    @Override
    public CustomerDTO saveNewCustomer(CustomerDTO CustomerDTO) {
        return null;
    }


    @Override
    public void updateCustomerById(UUID id, CustomerDTO CustomerDTO) {

    }

    @Override
    public void deleteCustomerById(UUID customerId) {

    }
}
