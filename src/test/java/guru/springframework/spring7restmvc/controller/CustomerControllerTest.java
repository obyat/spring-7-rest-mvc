package guru.springframework.spring7restmvc.controller;


import guru.springframework.spring7restmvc.model.Customer;
import guru.springframework.spring7restmvc.service.CustomerService;
import guru.springframework.spring7restmvc.service.CustomerServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.ObjectMapper;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.Matchers.is;


@WebMvcTest(controllers = CustomerController.class)
class CustomerControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockitoBean
    CustomerService customerService;

    CustomerServiceImpl customerServiceImpl;

    @BeforeEach
    void setUp() {
        this.customerServiceImpl = new CustomerServiceImpl();
    }

    @Test
    void testCreateCustomer() throws Exception {
    Customer customer = this.customerServiceImpl.getAllCustomers().getFirst();
    customer.setCustomerName("Ray Jay");
    customer.setVersion(2);

    when(this.customerService.saveNewCustomer(customer)).thenAnswer(invocation -> invocation.getArgument(0));

    this.mockMvc.perform(post("/api/v1/customer")
                    .accept(org.springframework.http.MediaType.APPLICATION_JSON)
                    .contentType(org.springframework.http.MediaType.APPLICATION_JSON)
                    .content(this.objectMapper.writeValueAsString(customer)))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.customerName").value("Ray Jay"))
            .andExpect(jsonPath("$.version").value(2))
            .andExpect(header().string("Location", "/api/v1/customer/" + customer.getId().toString()));

    }


    @Test
    void testGetCustomerById() throws Exception {
        Customer customer = customerServiceImpl.getAllCustomers().getFirst();

        when(customerService.getCustomerById(customer.getId())).thenReturn(customer);

        mockMvc.perform(get("/api/v1/customer/" + customer.getId()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.Id", is(customer.getId().toString())))
                .andExpect(jsonPath("$.customerName", is(customer.getCustomerName())));
    }

    @Test
    void testListCustomers() throws Exception {
        when(customerService.getAllCustomers()).thenReturn(customerServiceImpl.getAllCustomers());

        mockMvc.perform(get("/api/v1/customer"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()", is(3)));
    }

}