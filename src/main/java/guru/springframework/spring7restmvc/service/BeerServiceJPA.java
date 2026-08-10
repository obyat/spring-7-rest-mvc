package guru.springframework.spring7restmvc.service;

import guru.springframework.spring7restmvc.entities.Beer;
import guru.springframework.spring7restmvc.mappers.BeerMapper;
import guru.springframework.spring7restmvc.model.BeerDTO;
import guru.springframework.spring7restmvc.model.BeerStyle;
import guru.springframework.spring7restmvc.reporsitories.BeerRepository;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.annotation.Primary;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;


@Service
@Primary
@RequiredArgsConstructor
public class BeerServiceJPA implements BeerService {

  private static final Integer DEFAULT_PAGE_NUMBER = 0;
  private static final Integer DEFAULT_PAGE_SIZE = 25;
  private final BeerRepository beerRepository;
  private final BeerMapper beerMapper;


  /**
   * Builds a {@link PageRequest} using the provided page number and page size.
   *
   * <p>The page number is expected to be 1-based and is converted to the 0-based index required by
   * Spring Data. If the page number is null or less than or equal to zero, the default page number
   * is used.
   *
   * <p>If the page size is null, the default page size is used. The page size is also limited to a
   * maximum of 1000.
   *
   * @param pageNumber the requested page number, starting at 1
   * @param pageSize   the requested number of items per page
   * @return a {@link PageRequest} configured with the validated page number and page size
   */
  private PageRequest buildPageRequest(Integer pageNumber, Integer pageSize) {
    pageNumber = pageNumber != null && pageNumber > 0 ? pageNumber - 1 : DEFAULT_PAGE_NUMBER;

    pageSize = pageSize == null ? DEFAULT_PAGE_SIZE : Math.min(pageSize, 1000);

    Sort sort = Sort.by(Sort.Order.asc("beerName"));

    return PageRequest.of(pageNumber, pageSize, sort);
  }


  @Override
  public Page<BeerDTO> listBeers(
          String beerName,
          BeerStyle beerStyle,
          boolean showInventory,
          Integer PageNumber,
          Integer pageSize) {
    Page<Beer> beerPage;
    PageRequest pageRequest = buildPageRequest(PageNumber, pageSize);

    if (!StringUtils.isBlank(beerName) && beerStyle == null) {
      beerPage = this.listBeersByName(beerName, pageRequest);
    } else if (StringUtils.isBlank(beerName)
            && beerStyle != null
            && !StringUtils.isBlank(beerStyle.toString())) {
      beerPage = this.listBeersByStyle(beerStyle, pageRequest);
    } else if (!StringUtils.isBlank(beerName)
            && beerStyle != null
            && !StringUtils.isBlank(beerStyle.toString())) {
      beerPage = this.listBeersByNameAndStyle(beerName, beerStyle, pageRequest);
    } else {
      beerPage = beerRepository.findAll(pageRequest);
    }

    if (!showInventory) {
      beerPage.forEach(beer -> beer.setQuantityOnHand(null));
    }
    return beerPage.map(beerMapper::beerToBeerDto);
  }


  private Page<Beer> listBeersByNameAndStyle(
          String beerName, BeerStyle beerStyle, Pageable pageable) {
    return beerRepository.findAllByBeerNameIsLikeIgnoreCaseAndBeerStyle(
            "%" + beerName + "%", beerStyle, pageable);
  }


  private Page<Beer> listBeersByStyle(BeerStyle beerStyle, Pageable pageable) {
    return beerRepository.findAllByBeerStyle(beerStyle, pageable);
  }


  private Page<Beer> listBeersByName(String beerName, Pageable pageable) {
    return beerRepository.findAllByBeerNameIsLikeIgnoreCase("%" + beerName + "%", pageable);
  }


  @Override
  public Optional<BeerDTO> getBeerById(UUID id) {
    return Optional.ofNullable(beerMapper.beerToBeerDto(beerRepository.findById(id).orElse(null)));
  }


  @Override
  public BeerDTO saveNewBeer(BeerDTO beerDTO) {
    return beerMapper.beerToBeerDto(beerRepository.save(beerMapper.beerDtoToBeer(beerDTO)));
  }


  @Override
  public Optional<BeerDTO> updateBeerById(UUID id, BeerDTO beerDTO) {
    AtomicReference<Optional<BeerDTO>> updatedBeer = new AtomicReference<>();

    beerRepository
            .findById(id)
            .ifPresentOrElse(
                    beer -> {
                      beer.setBeerName(beerDTO.getBeerName());
                      beer.setBeerStyle(beerDTO.getBeerStyle());
                      beer.setPrice(beerDTO.getPrice());
                      beer.setUpc(beerDTO.getUpc());

                      beerRepository.save(beer);
                      updatedBeer.set(Optional.of(beerMapper.beerToBeerDto(beer)));
                    },
                    () -> updatedBeer.set(Optional.empty()));

    return updatedBeer.get();
  }


  @Override
  public Boolean deleteBeerById(UUID beerId) {
    if (!beerRepository.existsById(beerId)) {
      return false;
    }
    beerRepository.deleteById(beerId);
    return true;
  }
}
