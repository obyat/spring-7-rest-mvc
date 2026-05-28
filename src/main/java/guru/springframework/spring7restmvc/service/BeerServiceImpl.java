package guru.springframework.spring7restmvc.service;

import guru.springframework.spring7restmvc.model.Beer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

@Slf4j
@Service
public class BeerServiceImpl implements BeerService {

    private final Map<UUID, Beer> beerMap;

    public BeerServiceImpl() {
        this.beerMap = new HashMap<>();
        Beer beer1 = Beer.builder()
                .id(UUID.randomUUID())
                .version(1)
                .beerName("Midnight Hops")
                .upc("98237465")
                .price(new BigDecimal("14.99"))
                .quantityOnHand(88)
                .createdDate(LocalDateTime.now())
                .updatedDate(LocalDateTime.now())
                .build();

        Beer beer2 = Beer.builder()
                .id(UUID.randomUUID())
                .version(1)
                .beerName("Sunset Lager")
                .upc("44556677")
                .price(new BigDecimal("10.49"))
                .quantityOnHand(210)
                .createdDate(LocalDateTime.now())
                .updatedDate(LocalDateTime.now())
                .build();

        Beer beer3 = Beer.builder()
                .id(UUID.randomUUID())
                .version(1)
                .beerName("Velvet Stout")
                .upc("77889900")
                .price(new BigDecimal("16.75"))
                .quantityOnHand(64)
                .createdDate(LocalDateTime.now())
                .updatedDate(LocalDateTime.now())
                .build();

        Beer beer4 = Beer.builder()
                .id(UUID.randomUUID())
                .version(1)
                .beerName("Citrus Blaze IPA")
                .upc("11235813")
                .price(new BigDecimal("13.25"))
                .quantityOnHand(145)
                .createdDate(LocalDateTime.now())
                .updatedDate(LocalDateTime.now())
                .build();

        Beer beer5 = Beer.builder()
                .id(UUID.randomUUID())
                .version(1)
                .beerName("Arctic Pilsner")
                .upc("99887766")
                .price(new BigDecimal("11.99"))
                .quantityOnHand(97)
                .createdDate(LocalDateTime.now())
                .updatedDate(LocalDateTime.now())
                .build();

        beerMap.put(beer1.getId(), beer1);
        beerMap.put(beer2.getId(), beer2);
        beerMap.put(beer3.getId(), beer3);
        beerMap.put(beer4.getId(), beer4);
        beerMap.put(beer5.getId(), beer5);
//        this.beerService = beerService;
    }

    @Override
    public List<Beer> listBeers() {
        return new ArrayList<>(this.beerMap.values());
    }

    @Override
    public Beer getBeerById(UUID id) {
        log.debug("get BeerService by Id in BeerServiceImpl: {}", id.toString());

        return this.beerMap.get(id);
    }

    @Override
    public Beer saveNewBeer(Beer beer) {
        Beer savedBeer = Beer.builder()
                .id(UUID.randomUUID())
                .beerName(beer.getBeerName())
                .version(beer.getVersion())
                .price(beer.getPrice())
                .quantityOnHand(beer.getQuantityOnHand())
                .upc(beer.getUpc())
                .createdDate(LocalDateTime.now())
                .updatedDate(LocalDateTime.now())
                .build();

        this.beerMap.put(savedBeer.getId(), savedBeer);

        return savedBeer;
    }


    @Override
    public void updateBeerById(UUID id, Beer beer) {
        Beer existingBeer = this.beerMap.get(id);
        existingBeer.setBeerName(beer.getBeerName());
        existingBeer.setVersion(beer.getVersion());
        existingBeer.setPrice(beer.getPrice());
        existingBeer.setQuantityOnHand(beer.getQuantityOnHand());
        existingBeer.setUpc(beer.getUpc());
        existingBeer.setUpdatedDate(LocalDateTime.now());

        this.beerMap.put(id, existingBeer);
    }
}