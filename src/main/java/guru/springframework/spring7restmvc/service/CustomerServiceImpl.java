package guru.springframework.spring7restmvc.service;

import guru.springframework.spring7restmvc.model.CustomerDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;


@Slf4j
@Service
public class CustomerServiceImpl implements CustomerService {

    private final Map<UUID, CustomerDTO> customerMap;


    public CustomerServiceImpl() {
        this.customerMap = new HashMap<>();

        CustomerDTO CustomerDTO1 =
                CustomerDTO.builder().id(UUID.randomUUID()).customerName("Customer 1").version(1).build();

        CustomerDTO CustomerDTO2 =
                CustomerDTO.builder().id(UUID.randomUUID()).customerName("Customer 2").version(2).build();

        CustomerDTO CustomerDTO3 =
                CustomerDTO.builder().id(UUID.randomUUID()).customerName("Customer 3").version(3).build();

        this.customerMap.put(CustomerDTO1.getId(), CustomerDTO1);
        this.customerMap.put(CustomerDTO2.getId(), CustomerDTO2);
        this.customerMap.put(CustomerDTO3.getId(), CustomerDTO3);
    }


    @Override
    public List<CustomerDTO> getAllCustomers() {
        return List.of(this.customerMap.values().toArray(new CustomerDTO[0]));
    }


    @Override
    public Optional<CustomerDTO> getCustomerById(UUID id) {
        return Optional.of(this.customerMap.get(id));
    }


    @Override
    public CustomerDTO saveNewCustomer(CustomerDTO CustomerDTO) {
        CustomerDTO savedCustomerDTO =
                guru.springframework.spring7restmvc.model.CustomerDTO.builder()
                        .id(UUID.randomUUID())
                        .customerName(CustomerDTO.getCustomerName())
                        .version(1)
                        .email(CustomerDTO.getEmail())
                        .build();
        this.customerMap.put(savedCustomerDTO.getId(), savedCustomerDTO);
        return savedCustomerDTO;
    }


    @Override
    public Optional<CustomerDTO> updateCustomerById(UUID id, CustomerDTO CustomerDTO) {
        CustomerDTO existingCustomerDTO = this.customerMap.get(id);
        existingCustomerDTO.setCustomerName(CustomerDTO.getCustomerName());
        existingCustomerDTO.setVersion(CustomerDTO.getVersion());
        existingCustomerDTO.setEmail(CustomerDTO.getEmail());
        this.customerMap.put(id, existingCustomerDTO);
        return Optional.of(existingCustomerDTO);
    }


    @Override
    public Boolean deleteCustomerById(UUID customerId) {
        this.customerMap.remove(customerId);
        return true;
    }
}
