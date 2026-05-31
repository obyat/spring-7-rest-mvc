package guru.springframework.spring7restmvc.controller;

import guru.springframework.spring7restmvc.model.Beer;
import guru.springframework.spring7restmvc.service.BeerService;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/beer")
public class BeerController {
    private final BeerService beerService;

    @PostMapping
    //    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<Beer> handlePost(@RequestBody Beer beer) {
        Beer savedBeer = beerService.saveNewBeer(beer);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Location", "/api/v1/beer/" + savedBeer.getId().toString());

        return new ResponseEntity<>(savedBeer, headers, HttpStatus.CREATED);
    }

    @RequestMapping(method = RequestMethod.GET)
    public List<Beer> getAllBeers() {
        return this.beerService.listBeers();
    }


    @RequestMapping(value = "{beerId}", method = RequestMethod.GET)
    public Beer getBeerById(@PathVariable("beerId") UUID id){
        log.debug("Getting beer by id in BeerController: {}", id);
        log.info("Getting beer by id in BeerController: {}", String.valueOf(id) + String.valueOf(id));
        return beerService.getBeerById(id);
    }

    @PutMapping("{beerId}")
    public ResponseEntity<Beer> updateById(@PathVariable UUID beerId, @RequestBody Beer beer){
        log.debug("Updating beer by id in BeerController: {}", beerId);
        beerService.updateBeerById(beerId, beer);
        return new ResponseEntity<Beer>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping("{beerId}")
    public ResponseEntity<Beer> deleteById(@PathVariable UUID beerId){
        log.debug("Deleting beer by id in BeerController: {}", beerId);
        beerService.deleteBeerById(beerId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}