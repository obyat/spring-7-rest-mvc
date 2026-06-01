package guru.springframework.spring7restmvc.controller;

import guru.springframework.spring7restmvc.model.Beer;
import guru.springframework.spring7restmvc.service.BeerService;
import guru.springframework.spring7restmvc.service.BeerServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MediaType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.ObjectMapper;


import java.math.BigDecimal;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@WebMvcTest(BeerController.class)
class BeerControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockitoBean
    BeerService beerService;

    BeerServiceImpl beerServiceImpl;


    @BeforeEach
    void setUp() {
        this.beerServiceImpl = new BeerServiceImpl();
    }

    @Test
    void testCreateBeer() throws Exception {
        Beer beer = this.beerServiceImpl.listBeers().getFirst();
        beer.setBeerName("New Beer 2.0");
        beer.setPrice(new BigDecimal("1.0"));

        // Echo back the saved beer so the response contains the created name
        when(beerService.saveNewBeer(any(Beer.class))).thenAnswer(invocation -> invocation.getArgument(0));

        this.mockMvc.perform(post("/api/v1/beer")
                        .accept(org.springframework.http.MediaType.APPLICATION_JSON)
                        .contentType(org.springframework.http.MediaType.APPLICATION_JSON)
                        .content(this.objectMapper.writeValueAsString(beer)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.beerName", equalTo("New Beer 2.0")))
                .andExpect(header().string("Location", "/api/v1/beer/" + beer.getId().toString()));
    }

    @Test
    void getBeerById() throws Exception {
        Beer testBeer = beerServiceImpl.listBeers().getFirst();

        when(beerService.getBeerById(testBeer.getId())).thenReturn(testBeer);

        mockMvc.perform(get("/api/v1/beer/" + testBeer.getId())
                        .accept(String.valueOf(MediaType.APPLICATION_JSON))) //
                .andExpect(status().isOk())
                .andExpect(content().contentType(String.valueOf(MediaType.APPLICATION_JSON)))
                .andExpect(jsonPath("$.id", is(testBeer.getId().toString())))
                .andExpect(jsonPath("$.beerName", equalTo("updated Beer")));
    }

    @Test
    void getBeerByIdLastBeer() throws Exception {
        Beer testBeer = beerServiceImpl.listBeers().getLast();

        when(beerService.getBeerById(testBeer.getId())).thenReturn(testBeer);

        mockMvc.perform(get("/api/v1/beer/" + testBeer.getId())
                        .accept(String.valueOf(MediaType.APPLICATION_JSON))) //
                .andExpect(status().isOk())
                .andExpect(content().contentType(String.valueOf(MediaType.APPLICATION_JSON)))
                .andExpect(jsonPath("$.id", is(testBeer.getId().toString())))
                .andExpect(jsonPath("$.beerName", is(testBeer.getBeerName())));
    }

    @Test
    void testListBeers() throws Exception {
        when(beerService.listBeers()).thenReturn(beerServiceImpl.listBeers());

        mockMvc.perform(get("/api/v1/beer"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(String.valueOf(MediaType.APPLICATION_JSON)))
                .andExpect(jsonPath("$.length()", is(5)));
    }
}