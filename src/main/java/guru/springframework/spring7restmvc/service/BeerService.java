package guru.springframework.spring7restmvc.service;

import guru.springframework.spring7restmvc.model.BeerDTO;
import guru.springframework.spring7restmvc.model.BeerStyle;

import java.util.List;
import java.util.Optional;
import java.util.UUID;


public interface BeerService {

    List<BeerDTO> listBeers(String beerName, BeerStyle beerStyle, boolean showInventory);

    Optional<BeerDTO> getBeerById(UUID id);

    BeerDTO saveNewBeer(BeerDTO beerDTO);

    Optional<BeerDTO> updateBeerById(UUID id, BeerDTO beerDTO);

    Boolean deleteBeerById(UUID beerId);
}
