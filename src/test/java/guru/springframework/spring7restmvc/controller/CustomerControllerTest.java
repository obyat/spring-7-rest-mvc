package guru.springframework.spring7restmvc.controller;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import guru.springframework.spring7restmvc.constants.ApiPaths;
import guru.springframework.spring7restmvc.model.CustomerDTO;
import guru.springframework.spring7restmvc.service.CustomerService;
import guru.springframework.spring7restmvc.service.CustomerServiceImpl;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.ObjectMapper;

@WebMvcTest(controllers = CustomerController.class)
class CustomerControllerTest {

  @Autowired MockMvc mockMvc;

  @Autowired ObjectMapper objectMapper;

  @MockitoBean CustomerService customerService;

  CustomerServiceImpl customerServiceImpl;

  @BeforeEach
  void setUp() {
    this.customerServiceImpl = new CustomerServiceImpl();
  }

  @Test
  void testCreateCustomer() throws Exception {
    CustomerDTO CustomerDTO = this.customerServiceImpl.getAllCustomers().getFirst();
    CustomerDTO.setCustomerName("Ray Jay");
    CustomerDTO.setVersion(2);

    when(this.customerService.saveNewCustomer(CustomerDTO))
        .thenAnswer(invocation -> invocation.getArgument(0));

    this.mockMvc
        .perform(
            post(ApiPaths.Customer.ROOT)
                .accept(org.springframework.http.MediaType.APPLICATION_JSON)
                .contentType(org.springframework.http.MediaType.APPLICATION_JSON)
                .content(this.objectMapper.writeValueAsString(CustomerDTO)))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.customerName").value("Ray Jay"))
        .andExpect(jsonPath("$.version").value(2))
        .andExpect(
            header()
                .string("Location", ApiPaths.Customer.ROOT + "/" + CustomerDTO.getId().toString()));
  }

  @Test
  void testUpdateCustomer() throws Exception {
    CustomerDTO CustomerDTO = this.customerServiceImpl.getAllCustomers().getFirst();

    this.mockMvc
        .perform(
            put(ApiPaths.Customer.CUSTOMER_WITH_ID, CustomerDTO.getId().toString())
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(this.objectMapper.writeValueAsString(CustomerDTO)))
        .andExpect(status().isNoContent());

    verify(customerService).updateCustomerById(any(UUID.class), any(CustomerDTO.class));
  }

  @Test
  void testGetCustomerById() throws Exception {
    CustomerDTO CustomerDTO = customerServiceImpl.getAllCustomers().getFirst();

    when(customerService.getCustomerById(CustomerDTO.getId())).thenReturn(Optional.of(CustomerDTO));

    mockMvc
        .perform(get(ApiPaths.Customer.CUSTOMER_WITH_ID, CustomerDTO.getId()))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.Id", is(CustomerDTO.getId().toString())))
        .andExpect(jsonPath("$.customerName", is(CustomerDTO.getCustomerName())));
  }

  @Test
  void testDeleteCustomerById() throws Exception {
    CustomerDTO CustomerDTO = customerServiceImpl.getAllCustomers().getFirst();
    assertThat(customerService.getAllCustomers().size(), is(0));

    Mockito.doAnswer(
            invocation -> {
              java.util.UUID customerId = invocation.getArgument(0);
              customerServiceImpl.deleteCustomerById(customerId);
              return null;
            })
        .when(customerService)
        .deleteCustomerById(any(UUID.class));

    this.mockMvc
        .perform(
            delete(ApiPaths.Customer.CUSTOMER_WITH_ID, CustomerDTO.getId())
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isNoContent());

    ArgumentCaptor<UUID> uuidCaptor = ArgumentCaptor.forClass(UUID.class);

    verify(customerService).deleteCustomerById(uuidCaptor.capture());
    assertThat(uuidCaptor.getValue(), is(CustomerDTO.getId()));
    assertThat(customerServiceImpl.getAllCustomers().size(), is(2));
  }

  @Test
  void testListCustomers() throws Exception {
    when(customerService.getAllCustomers()).thenReturn(customerServiceImpl.getAllCustomers());

    mockMvc
        .perform(get("/api/v1/customer"))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.length()", is(3)));
  }
}
