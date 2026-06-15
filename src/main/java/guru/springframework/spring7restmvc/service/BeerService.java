package guru.springframework.spring7restmvc.service;

import guru.springframework.spring7restmvc.model.BeerDTO;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface BeerService {

  List<BeerDTO> listBeers();

  Optional<BeerDTO> getBeerById(UUID id);

  BeerDTO saveNewBeer(BeerDTO beerDTO);

  void updateBeerById(UUID id, BeerDTO beerDTO);

  void deleteBeerById(UUID beerId);
}
