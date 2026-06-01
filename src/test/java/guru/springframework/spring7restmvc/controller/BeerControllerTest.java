package guru.springframework.spring7restmvc.controller;

import guru.springframework.spring7restmvc.model.Beer;
import guru.springframework.spring7restmvc.service.BeerService;
import guru.springframework.spring7restmvc.service.BeerServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MediaType;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.ObjectMapper;


import java.math.BigDecimal;
import java.util.UUID;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
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
    void testUpdateBeer() throws Exception {
        Beer beer = this.beerServiceImpl.listBeers().getFirst();
        beer.setBeerName("Updated Beer 2.0");
        beer.setPrice(new BigDecimal("2.0"));

        this.mockMvc.perform(put("/api/v1/beer/" + beer.getId().toString())
                        .accept(org.springframework.http.MediaType.APPLICATION_JSON)
                        .contentType(org.springframework.http.MediaType.APPLICATION_JSON)
                        .content(this.objectMapper.writeValueAsString(beer)))
                .andExpect(status().isNoContent());
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
    void testDeleteBeer() throws Exception {
        Beer testBeer = beerServiceImpl.listBeers().getFirst();

        assertThat(beerServiceImpl.listBeers().size(), is(5));

        // Make the mocked service's deleteById affect the real in-memory impl
        org.mockito.Mockito.doAnswer(invocation -> {
            java.util.UUID id = invocation.getArgument(0);
            beerServiceImpl.deleteBeerById(testBeer.getId());
            return null;
        }).when(beerService).deleteBeerById(testBeer.getId());

        mockMvc.perform(delete("/api/v1/beer/" + testBeer.getId())
                        .accept(String.valueOf(MediaType.APPLICATION_JSON))
                        .content(String.valueOf(MediaType.APPLICATION_JSON)))
                .andExpect(status().isNoContent());


        ArgumentCaptor<UUID> uuidCaptor = ArgumentCaptor.forClass(UUID.class);
        verify(beerService).deleteBeerById(uuidCaptor.capture());

        assertThat(uuidCaptor.getValue(), is(testBeer.getId()));
        assertThat(beerServiceImpl.listBeers().size(), is(4));
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