package guru.springframework.spring7restmvc.service;

import guru.springframework.spring7restmvc.mappers.CustomerMapper;
import guru.springframework.spring7restmvc.model.CustomerDTO;
import guru.springframework.spring7restmvc.reporsitories.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;


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
        return customerMapper.customerToCustomerDto(customerRepository.save(customerMapper.customerDtoToCustomer(CustomerDTO)));
    }


    @Override
    public Optional<CustomerDTO> updateCustomerById(UUID id, CustomerDTO CustomerDTO) {
        AtomicReference<Optional<CustomerDTO>> atomicReference = new AtomicReference<>(Optional.empty());

        customerRepository.findById(id).ifPresentOrElse(customer -> {
            customer.setCustomerName(CustomerDTO.getCustomerName());
            customer.setCreatedDate(CustomerDTO.getCreatedDate());
            customer.setLastModifiedDate(CustomerDTO.getLastModifiedDate());
            atomicReference.set(Optional.of(customerMapper.customerToCustomerDto(customerRepository.save(customer))));
        }, () -> atomicReference.set(Optional.empty()));
        return atomicReference.get();
    }


    @Override
    public Boolean deleteCustomerById(UUID customerId) {
        if (!customerRepository.existsById(customerId)) {
            return false;
        }
        customerRepository.deleteById(customerId);
        return true;
    }
}
