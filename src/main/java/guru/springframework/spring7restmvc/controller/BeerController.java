package guru.springframework.spring7restmvc.controller;

import guru.springframework.spring7restmvc.constants.ApiPaths;
import guru.springframework.spring7restmvc.model.BeerDTO;
import guru.springframework.spring7restmvc.model.BeerStyle;
import guru.springframework.spring7restmvc.service.BeerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;


@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping(ApiPaths.Beer.ROOT)
public class BeerController {
  private final BeerService beerService;


  @PostMapping
  public ResponseEntity<BeerDTO> handlePost(@Valid @RequestBody BeerDTO beerDTO) {
    BeerDTO savedBeerDTO = beerService.saveNewBeer(beerDTO);

    HttpHeaders headers = new HttpHeaders();
    headers.add(
            "Location",
            ApiPaths.Beer.BEER_WITH_ID.replace("{beerId}", savedBeerDTO.getId().toString()));

    return new ResponseEntity<>(savedBeerDTO, headers, HttpStatus.CREATED);
  }


  @GetMapping()
  public Page<BeerDTO> getAllBeers(
          @RequestParam(required = false) String beerName,
          @RequestParam(required = false) BeerStyle beerStyle,
          @RequestParam(required = false) boolean showInventory,
          @RequestParam(required = false) Integer pageNumber,
          @RequestParam(required = false) Integer pageSize) {
    return this.beerService.listBeers(beerName, beerStyle, showInventory, pageNumber, pageSize);
  }


  @GetMapping(value = ApiPaths.Beer.BY_ID)
  public BeerDTO getBeerById(@PathVariable("beerId") UUID id) {
    log.debug("Getting beer by id in BeerController: {}", id);
    return beerService.getBeerById(id).orElseThrow(NotFoundException::new);
  }


  @PutMapping(ApiPaths.Beer.BY_ID)
  public ResponseEntity<BeerDTO> updateById(
          @PathVariable UUID beerId, @Valid @RequestBody BeerDTO beerDTO) {
    if (beerService.getBeerById(beerId).isEmpty()) {
      throw new NotFoundException();
    }

    log.debug("Updating beer by id in BeerController: {}", beerId);
    beerService.updateBeerById(beerId, beerDTO);
    return new ResponseEntity<>(HttpStatus.NO_CONTENT);
  }


  @DeleteMapping(ApiPaths.Beer.BY_ID)
  public ResponseEntity<BeerDTO> deleteById(@PathVariable UUID beerId) {
    Boolean deleted = beerService.deleteBeerById(beerId);
    if (!Boolean.TRUE.equals(deleted)) {
      throw new NotFoundException();
    }
    log.debug("Deleting beer by id in BeerController: {}", beerId);
    return new ResponseEntity<>(HttpStatus.NO_CONTENT);
  }
}
