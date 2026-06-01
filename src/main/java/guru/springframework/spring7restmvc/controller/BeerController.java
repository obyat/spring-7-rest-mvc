package guru.springframework.spring7restmvc.controller;

import guru.springframework.spring7restmvc.constants.ApiPaths;
import guru.springframework.spring7restmvc.model.Beer;
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
  public ResponseEntity<Beer> handlePost(@RequestBody Beer beer) {
    Beer savedBeer = beerService.saveNewBeer(beer);

    HttpHeaders headers = new HttpHeaders();
    headers.add(
        "Location", ApiPaths.Beer.BEER_WITH_ID.replace("{beerId}", savedBeer.getId().toString()));

    return new ResponseEntity<>(savedBeer, headers, HttpStatus.CREATED);
  }

  @GetMapping()
  public List<Beer> getAllBeers() {
    return this.beerService.listBeers();
  }

  @GetMapping(value = ApiPaths.Beer.BY_ID)
  public Beer getBeerById(@PathVariable("beerId") UUID id) {
    log.debug("Getting beer by id in BeerController: {}", id);
    log.info("Getting beer by id in BeerController: {}", id);
    return beerService.getBeerById(id);
  }

  @PutMapping(ApiPaths.Beer.BY_ID)
  public ResponseEntity<Beer> updateById(@PathVariable UUID beerId, @RequestBody Beer beer) {
    log.debug("Updating beer by id in BeerController: {}", beerId);
    beerService.updateBeerById(beerId, beer);
    return new ResponseEntity<Beer>(HttpStatus.NO_CONTENT);
  }

  @DeleteMapping(ApiPaths.Beer.BY_ID)
  public ResponseEntity<Beer> deleteById(@PathVariable UUID beerId) {
    log.debug("Deleting beer by id in BeerController: {}", beerId);
    beerService.deleteBeerById(beerId);
    return new ResponseEntity<>(HttpStatus.NO_CONTENT);
  }
}
