package guru.springframework.spring7restmvc.service;

import guru.springframework.spring7restmvc.mappers.BeerMapper;
import guru.springframework.spring7restmvc.model.BeerDTO;
import guru.springframework.spring7restmvc.reporsitories.BeerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;


@Service
@Primary
@RequiredArgsConstructor
public class BeerServiceJPA implements BeerService {

    private final BeerRepository beerRepository;
    private final BeerMapper beerMapper;


    @Override
    public List<BeerDTO> listBeers() {
        return beerRepository.findAll()
                .stream()
                .map(beerMapper::beerToBeerDto)
                .collect(Collectors.toList());
    }


    @Override
    public Optional<BeerDTO> getBeerById(UUID id) {
        return Optional.ofNullable(
                beerMapper.beerToBeerDto(
                        beerRepository
                                .findById(id)
                                .orElse(null)));
    }


    @Override
    public BeerDTO saveNewBeer(BeerDTO beerDTO) {
        return beerMapper.beerToBeerDto(
                beerRepository.save(
                        beerMapper.beerDtoToBeer(beerDTO)
                )
        );
    }


    @Override
    public Optional<BeerDTO> updateBeerById(UUID id, BeerDTO beerDTO) {
        AtomicReference<Optional<BeerDTO>> updatedBeer = new AtomicReference<>();

        beerRepository.findById(id).ifPresentOrElse(beer -> {
            beer.setBeerName(beerDTO.getBeerName());
            beer.setBeerStyle(beerDTO.getBeerStyle());
            beer.setPrice(beerDTO.getPrice());
            beer.setUpc(beerDTO.getUpc());

            beerRepository.save(beer);
            updatedBeer.set(Optional.of(beerMapper.beerToBeerDto(beer)));
        }, () -> updatedBeer.set(Optional.empty()));

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
