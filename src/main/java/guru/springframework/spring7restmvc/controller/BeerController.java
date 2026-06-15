package guru.springframework.spring7restmvc.controller;

import guru.springframework.spring7restmvc.constants.ApiPaths;
import guru.springframework.spring7restmvc.model.BeerDTO;
import guru.springframework.spring7restmvc.service.BeerService;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping(ApiPaths.Beer.ROOT)
public class BeerController {
  private final BeerService beerService;

  @PostMapping
  public ResponseEntity<BeerDTO> handlePost(@RequestBody BeerDTO beerDTO) {
    BeerDTO savedBeerDTO = beerService.saveNewBeer(beerDTO);

    HttpHeaders headers = new HttpHeaders();
    headers.add(
        "Location",
        ApiPaths.Beer.BEER_WITH_ID.replace("{beerId}", savedBeerDTO.getId().toString()));

    return new ResponseEntity<>(savedBeerDTO, headers, HttpStatus.CREATED);
  }

  @GetMapping()
  public List<BeerDTO> getAllBeers() {
    return this.beerService.listBeers();
  }

  @GetMapping(value = ApiPaths.Beer.BY_ID)
  public BeerDTO getBeerById(@PathVariable("beerId") UUID id) {
    log.debug("Getting beer by id in BeerController: {}", id);
    log.info("Getting beer by id in BeerController: {}", id);
    return beerService.getBeerById(id).orElseThrow(NotFoundException::new);
  }

  @PutMapping(ApiPaths.Beer.BY_ID)
  public ResponseEntity<BeerDTO> updateById(
      @PathVariable UUID beerId, @RequestBody BeerDTO beerDTO) {
    log.debug("Updating beer by id in BeerController: {}", beerId);
    beerService.updateBeerById(beerId, beerDTO);
    return new ResponseEntity<BeerDTO>(HttpStatus.NO_CONTENT);
  }

  @DeleteMapping(ApiPaths.Beer.BY_ID)
  public ResponseEntity<BeerDTO> deleteById(@PathVariable UUID beerId) {
    log.debug("Deleting beer by id in BeerController: {}", beerId);
    beerService.deleteBeerById(beerId);
    return new ResponseEntity<>(HttpStatus.NO_CONTENT);
  }
}
