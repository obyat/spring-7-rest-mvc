package guru.springframework.spring7restmvc.service;

import guru.springframework.spring7restmvc.model.Beer;

import java.util.List;
import java.util.UUID;


public interface BeerService {

    List<Beer> listBeers();

    Beer getBeerById(UUID id);

    Beer saveNewBeer(Beer beer);

    void updateBeerById(UUID id, Beer beer);

    void deleteBeerById(UUID beerId);
}
