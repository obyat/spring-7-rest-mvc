package guru.springframework.spring7restmvc.controller;

import guru.springframework.spring7restmvc.constants.ApiPaths;
import guru.springframework.spring7restmvc.entities.Beer;
import guru.springframework.spring7restmvc.mappers.BeerMapper;
import guru.springframework.spring7restmvc.model.BeerDTO;
import guru.springframework.spring7restmvc.model.BeerStyle;
import guru.springframework.spring7restmvc.reporsitories.BeerRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import tools.jackson.databind.ObjectMapper;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest // complete sp context not just splice
@AutoConfigureMockMvc
class BeerControllerIT {

  @Autowired
  BeerController beerController;

  @Autowired
  BeerRepository beerRepository;

  @Autowired
  BeerMapper beerMapper;

  @Autowired
  WebApplicationContext wac;

  @Autowired
  MockMvc mockMvc;

  @Autowired
  ObjectMapper objectMapper;

  @BeforeEach
  void setup() {
    this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
  }

  @Test
  void testListBeersLimit1000() {
    List<BeerDTO> dtos = beerController.getAllBeers(null, null, false, 1, 2410).getContent();

    assertThat(dtos.size()).isEqualTo(1000);
  }

  @Test
  void testListBeersByStyleAndNameShowInventoryTruePage2() throws Exception {
    this.mockMvc
            .perform(
                    get(ApiPaths.Beer.ROOT, "IPA")
                            .queryParam("beerName", "IPA")
                            .queryParam("beerStyle", BeerStyle.IPA.name())
                            .queryParam("showInventoryOnHand", "true")
                            .queryParam("pageNumber", "2")
                            .queryParam("pageSize", "76")
                            .accept(org.springframework.http.MediaType.APPLICATION_JSON)
                            .contentType(org.springframework.http.MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.content", hasSize(76)));
  }

  @Test
  void testGetBeerById() {
    Beer beer = beerRepository.findAll().get(0);

    BeerDTO dto = beerController.getBeerById(beer.getId());

    assertThat(dto.getId()).isEqualTo(beer.getId());
  }

  @Test
  void testListBeersByName() throws Exception {
    this.mockMvc
            .perform(
                    get(ApiPaths.Beer.ROOT, "IPA")
                            .queryParam("beerName", "IPA")
                            .accept(org.springframework.http.MediaType.APPLICATION_JSON)
                            .contentType(org.springframework.http.MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.content", hasSize(25)));
  }

  @Test
  void testListBeersByBeerStyle() throws Exception {
    this.mockMvc
            .perform(
                    get(ApiPaths.Beer.ROOT)
                            .queryParam("beerStyle", BeerStyle.IPA.name())
                            .accept(org.springframework.http.MediaType.APPLICATION_JSON)
                            .contentType(org.springframework.http.MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.content", hasSize(25)));
  }

  @Test
  void testListBeersByBeerNameANDBeerStyle() throws Exception {
    this.mockMvc
            .perform(
                    get(ApiPaths.Beer.ROOT)
                            .queryParam("beerName", "IPA")
                            .queryParam("beerStyle", BeerStyle.IPA.name())
                            .accept(org.springframework.http.MediaType.APPLICATION_JSON)
                            .contentType(org.springframework.http.MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.content", hasSize(25)));
  }

  @Test
  void testBeerIdNotFound() {
    assertThrows(
            NotFoundException.class,
            () -> {
              beerController.getBeerById(UUID.randomUUID());
        });
  }

  @Transactional // rolls back db to original undeleted state
  @Test
  void testEmptyBeers() {
    beerRepository.deleteAll();
    List<BeerDTO> dtos = beerController.getAllBeers(null, null, false, 1, 25).getContent();

    assertThat(dtos.size()).isEqualTo(0);
  }

  @Transactional
  @Rollback
  @Test
  void saveNewBeerTest() {
    BeerDTO beerDTO = BeerDTO.builder().beerName("Pepsi").build();

    ResponseEntity<BeerDTO> responseEntity = beerController.handlePost(beerDTO);

    assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.valueOf(201));
    assertThat(responseEntity.getHeaders().getLocation()).isNotNull();

    String[] locationUUID =
            Objects.requireNonNull(responseEntity.getHeaders().getLocation()).getPath().split("/");
    UUID savedUUID = UUID.fromString(locationUUID[4]);

    Beer beer = beerRepository.findById(savedUUID).get();

    assertThat(beer.getBeerName()).isEqualTo(beerDTO.getBeerName());
    assertThat(beer.getId()).isEqualTo(savedUUID);
  }

  @Test
  void testUpdateExistingBeer() {
    Beer beer = beerRepository.findAll().get(0);
    BeerDTO beerDTO = beerMapper.beerToBeerDto(beer);
    beerDTO.setId(null);
    beerDTO.setVersion(null);
    final String beerName = "UPDATED";

    assertThat(beerDTO.getBeerName()).isNotEqualTo(beerName);

    beerDTO.setBeerName(beerName);

    ResponseEntity<BeerDTO> responseEntity = beerController.updateById(beer.getId(), beerDTO);
    assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.valueOf(204));

    Beer updatedBeer = beerRepository.findById(beer.getId()).get();
    assertThat(updatedBeer.getBeerName()).isEqualTo(beerName);
  }

  @Test
  void testUpdateExistingBeerExistingName() throws Exception {
    Beer beer = beerRepository.findAll().get(0);

    Map<String, Object> beerMap = new HashMap<>();
    beerMap.put(
            "beerName",
            "New Updated Name with a very long name over fifty characters which should fail "
                    + "because we cannot add this many characters to a name!");

    this.mockMvc
            .perform(
                    put(ApiPaths.Beer.BEER_WITH_ID, beer.getId())
                            .accept(org.springframework.http.MediaType.APPLICATION_JSON)
                            .contentType(org.springframework.http.MediaType.APPLICATION_JSON)
                            .content(this.objectMapper.writeValueAsString(beerMap)))
            .andExpect(status().isBadRequest());
  }

  @Test
  void testUpdateNoteFound() {
    assertThrows(
            (NotFoundException.class),
            () -> beerController.updateById(UUID.randomUUID(), BeerDTO.builder().build()));
  }

  @Transactional
  @Rollback
  @Test
  void deleteByIdNotFound() {
    Beer beer = beerRepository.findAll().get(0);

    ResponseEntity<BeerDTO> responseEntity = beerController.deleteById(beer.getId());
    Beer foundBeer = beerRepository.findById(beer.getId()).orElse(null);

    assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.valueOf(204));
    assertThat(foundBeer).isNull();
  }

  @Transactional
  @Rollback
  @Test
  void testDeleteByIDNotFound() {
    assertThrows(NotFoundException.class, () -> beerController.deleteById(UUID.randomUUID()));
  }
}
