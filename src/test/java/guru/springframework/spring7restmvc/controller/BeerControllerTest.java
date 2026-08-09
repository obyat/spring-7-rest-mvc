package guru.springframework.spring7restmvc.controller;

import guru.springframework.spring7restmvc.constants.ApiPaths;
import guru.springframework.spring7restmvc.model.BeerDTO;
import guru.springframework.spring7restmvc.model.BeerStyle;
import guru.springframework.spring7restmvc.service.BeerService;
import guru.springframework.spring7restmvc.service.BeerServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MediaType;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.ObjectMapper;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(BeerController.class)
@ExtendWith(MockitoExtension.class)
class BeerControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockitoBean
    BeerService beerService;

    BeerServiceImpl beerServiceImpl;

    @Captor
    ArgumentCaptor<UUID> beerUUIDCaptor;


    @BeforeEach
    void setUp() {
        this.beerServiceImpl = new BeerServiceImpl();
    }


    @Test
    void testCreateBeer() throws Exception {
        BeerDTO beerDTO = BeerDTO.builder()
                .beerName("New Beer 2.0")
                .beerStyle(BeerStyle.ALE)
                .upc("12345")
                .price(new BigDecimal("1.0"))
                .build();

        BeerDTO savedBeer = BeerDTO.builder()
                .id(UUID.randomUUID())
                .beerName("New Beer 2.0")
                .beerStyle(BeerStyle.ALE)
                .upc("12345")
                .price(new BigDecimal("1.0"))
                .build();

        when(beerService.saveNewBeer(any(BeerDTO.class)))
                .thenReturn(savedBeer);

        this.mockMvc
                .perform(
                        post(ApiPaths.Beer.ROOT)
                                .accept(org.springframework.http.MediaType.APPLICATION_JSON)
                                .contentType(org.springframework.http.MediaType.APPLICATION_JSON)
                                .content(this.objectMapper.writeValueAsString(beerDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.beerName", equalTo("New Beer 2.0")))
                .andExpect(header().exists("Location"));
    }


    @Test
    void testUpdateBeer() throws Exception {
        UUID beerId = UUID.randomUUID();
        BeerDTO beerDTO = BeerDTO.builder()
                .id(beerId)
                .beerName("Updated Beer 2.0")
                .beerStyle(BeerStyle.ALE)
                .upc("12345")
                .price(new BigDecimal("2.0"))
                .build();

        when(beerService.getBeerById(beerId))
                .thenReturn(Optional.of(beerDTO));

        this.mockMvc
                .perform(
                        put(ApiPaths.Beer.BEER_WITH_ID, beerId)
                                .accept(org.springframework.http.MediaType.APPLICATION_JSON)
                                .contentType(org.springframework.http.MediaType.APPLICATION_JSON)
                                .content(this.objectMapper.writeValueAsString(beerDTO)))
                .andExpect(status().isNoContent());
    }


    @Test
    void getBeerById() throws Exception {
        BeerDTO testBeerDTO = beerServiceImpl.listBeers(null, null, false, 1, 25).getContent().getFirst();

        when(beerService.getBeerById(testBeerDTO.getId())).thenReturn(Optional.of(testBeerDTO));

        mockMvc
                .perform(
                        get(ApiPaths.Beer.BEER_WITH_ID, testBeerDTO.getId())
                                .accept(String.valueOf(MediaType.APPLICATION_JSON))) //
                .andExpect(status().isOk())
                .andExpect(content().contentType(String.valueOf(MediaType.APPLICATION_JSON)))
                .andExpect(jsonPath("$.id", is(testBeerDTO.getId().toString())))
                .andExpect(jsonPath("$.beerName", equalTo("updated Beer")));
    }


    @Test
    void testDeleteBeer() throws Exception {
        BeerDTO testBeerDTO = beerServiceImpl.listBeers(null, null, false, 1, 25).getContent().getFirst();

        given(beerService.deleteBeerById(testBeerDTO.getId())).willReturn(true);

        assertThat(beerServiceImpl.listBeers(null, null, false, 1, 25).getContent().size(), is(5));

        // Make the mocked service's deleteById affect the real in-memory impl
        org.mockito.Mockito.doAnswer(
                        _ -> {
                            beerServiceImpl.deleteBeerById(testBeerDTO.getId());
                            return true;
                        })
                .when(beerService)
                .deleteBeerById(testBeerDTO.getId());

        mockMvc
                .perform(
                        delete(ApiPaths.Beer.BEER_WITH_ID, testBeerDTO.getId())
                                .accept(String.valueOf(MediaType.APPLICATION_JSON))
                                .content(String.valueOf(MediaType.APPLICATION_JSON)))
                .andExpect(status().isNoContent());

        verify(beerService).deleteBeerById(this.beerUUIDCaptor.capture());
        assertThat(this.beerUUIDCaptor.getValue(), is(testBeerDTO.getId()));
        assertThat(beerServiceImpl.listBeers(null, null, false, 1, 25).getContent().size(), is(4));
    }


    @Test
    void getBeerByIdLastBeer() throws Exception {
        BeerDTO testBeerDTO = beerServiceImpl.listBeers(null, null, false, 1, 25).getContent().getLast();

        when(beerService.getBeerById(testBeerDTO.getId())).thenReturn(Optional.of(testBeerDTO));

        mockMvc
                .perform(
                        get(ApiPaths.Beer.BEER_WITH_ID, testBeerDTO.getId())
                                .accept(String.valueOf(MediaType.APPLICATION_JSON))) //
                .andExpect(status().isOk())
                .andExpect(content().contentType(String.valueOf(MediaType.APPLICATION_JSON)))
                .andExpect(jsonPath("$.id", is(testBeerDTO.getId().toString())))
                .andExpect(jsonPath("$.beerName", is(testBeerDTO.getBeerName())));
    }


    @Test
    void testListBeers() throws Exception {
        when(beerService.listBeers(null, null, false, null, null)).thenReturn(beerServiceImpl.listBeers(null, null,
                false, 1, null));

        mockMvc
                .perform(get(ApiPaths.Beer.ROOT))
                .andExpect(status().isOk())
                .andExpect(content().contentType(String.valueOf(MediaType.APPLICATION_JSON)))
                .andExpect(jsonPath("$.length()", is(11)));
    }


    @Test
    void getBeerNotFound() throws Exception {

        when(beerService.getBeerById(any(UUID.class))).thenReturn(Optional.empty());
        this.mockMvc
                .perform(get(ApiPaths.Beer.BEER_WITH_ID, UUID.randomUUID()))
                .andExpect(status().isNotFound());
    }


    @Test
    void testCreateBeerNullBeerName() throws Exception {
        BeerDTO beerDTO = BeerDTO.builder().build();

        given(beerService.saveNewBeer(any(BeerDTO.class))).willReturn(beerServiceImpl.listBeers(null, null, false, 1,
                25).getContent().get(1));

        var result = this.mockMvc
                .perform(
                        post(ApiPaths.Beer.ROOT)
                                .accept(org.springframework.http.MediaType.APPLICATION_JSON)
                                .contentType(org.springframework.http.MediaType.APPLICATION_JSON)
                                .content(this.objectMapper.writeValueAsString(beerDTO)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.length()", is(6)))
                .andReturn();

        System.out.println(result.getResponse().getContentAsString());
    }


    @Test
    void testUpdateBeerBlankName() throws Exception {
        BeerDTO beerDTO = this.beerServiceImpl.listBeers(null, null, false, 1, 25).getContent().getFirst();
        beerDTO.setBeerName("");
        beerDTO.setBeerName("Updated Beer 2.0");
        beerDTO.setPrice(new BigDecimal("2.0"));

        this.mockMvc
                .perform(
                        put(ApiPaths.Beer.BEER_WITH_ID, beerDTO.getId())
                                .accept(org.springframework.http.MediaType.APPLICATION_JSON)
                                .contentType(org.springframework.http.MediaType.APPLICATION_JSON)
                                .content(this.objectMapper.writeValueAsString(beerDTO)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.length()", is(1)));
    }
}