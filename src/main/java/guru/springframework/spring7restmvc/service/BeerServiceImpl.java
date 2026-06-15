package guru.springframework.spring7restmvc.service;

import guru.springframework.spring7restmvc.model.BeerDTO;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class BeerServiceImpl implements BeerService {

  private final Map<UUID, BeerDTO> beerMap;

  public BeerServiceImpl() {
    this.beerMap = new HashMap<>();
    BeerDTO beerDTO1 =
        BeerDTO.builder()
            .id(UUID.randomUUID())
            .version(1)
            .beerName("Midnight Hops")
            .upc("98237465")
            .price(new BigDecimal("14.99"))
            .quantityOnHand(88)
            .createdDate(LocalDateTime.now())
            .updatedDate(LocalDateTime.now())
            .build();

    BeerDTO beerDTO2 =
        BeerDTO.builder()
            .id(UUID.randomUUID())
            .version(1)
            .beerName("Sunset Lager")
            .upc("44556677")
            .price(new BigDecimal("10.49"))
            .quantityOnHand(210)
            .createdDate(LocalDateTime.now())
            .updatedDate(LocalDateTime.now())
            .build();

    BeerDTO beerDTO3 =
        BeerDTO.builder()
            .id(UUID.randomUUID())
            .version(1)
            .beerName("Velvet Stout")
            .upc("77889900")
            .price(new BigDecimal("16.75"))
            .quantityOnHand(64)
            .createdDate(LocalDateTime.now())
            .updatedDate(LocalDateTime.now())
            .build();

    BeerDTO beerDTO4 =
        BeerDTO.builder()
            .id(UUID.randomUUID())
            .version(1)
            .beerName("Citrus Blaze IPA")
            .upc("11235813")
            .price(new BigDecimal("13.25"))
            .quantityOnHand(145)
            .createdDate(LocalDateTime.now())
            .updatedDate(LocalDateTime.now())
            .build();

    BeerDTO beerDTO5 =
        BeerDTO.builder()
            .id(UUID.randomUUID())
            .version(1)
            .beerName("Arctic Pilsner")
            .upc("99887766")
            .price(new BigDecimal("11.99"))
            .quantityOnHand(97)
            .createdDate(LocalDateTime.now())
            .updatedDate(LocalDateTime.now())
            .build();

    beerMap.put(beerDTO1.getId(), beerDTO1);
    beerMap.put(beerDTO2.getId(), beerDTO2);
    beerMap.put(beerDTO3.getId(), beerDTO3);
    beerMap.put(beerDTO4.getId(), beerDTO4);
    beerMap.put(beerDTO5.getId(), beerDTO5);
  }

  @Override
  public List<BeerDTO> listBeers() {
    this.beerMap.values().stream()
        .findFirst()
        .ifPresent(
            beerDTO -> {
              beerDTO.setBeerName("updated Beer");
            });
    return new ArrayList<>(this.beerMap.values());
  }

  @Override
  public Optional<BeerDTO> getBeerById(UUID id) {
    log.debug("get BeerService by Id in BeerServiceImpl: {}", id.toString());

    return Optional.of(this.beerMap.get(id));
  }

  @Override
  public BeerDTO saveNewBeer(BeerDTO beerDTO) {
    BeerDTO savedBeerDTO =
        BeerDTO.builder()
            .id(UUID.randomUUID())
            .beerName(beerDTO.getBeerName())
            .version(beerDTO.getVersion())
            .price(beerDTO.getPrice())
            .quantityOnHand(beerDTO.getQuantityOnHand())
            .upc(beerDTO.getUpc())
            .createdDate(LocalDateTime.now())
            .updatedDate(LocalDateTime.now())
            .build();

    this.beerMap.put(savedBeerDTO.getId(), savedBeerDTO);

    return savedBeerDTO;
  }

  @Override
  public void updateBeerById(UUID id, BeerDTO beerDTO) {
    BeerDTO existingBeerDTO = this.beerMap.get(id);
    existingBeerDTO.setBeerName(beerDTO.getBeerName());
    existingBeerDTO.setVersion(beerDTO.getVersion());
    existingBeerDTO.setPrice(beerDTO.getPrice());
    existingBeerDTO.setQuantityOnHand(beerDTO.getQuantityOnHand());
    existingBeerDTO.setUpc(beerDTO.getUpc());
    existingBeerDTO.setUpdatedDate(LocalDateTime.now());

    this.beerMap.put(id, existingBeerDTO);
  }

  @Override
  public void deleteBeerById(UUID beerId) {
    this.beerMap.remove(beerId);
  }
}
