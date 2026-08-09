package guru.springframework.spring7restmvc.reporsitories;

import guru.springframework.spring7restmvc.bootstrap.BootstrapData;
import guru.springframework.spring7restmvc.entities.Beer;
import guru.springframework.spring7restmvc.model.BeerStyle;
import guru.springframework.spring7restmvc.service.BeerCsvServiceImpl;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.context.annotation.Import;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.jupiter.api.Assertions.assertThrows;


@DataJpaTest
@Import({BootstrapData.class, BeerCsvServiceImpl.class})
class BeerRepositoryTest {

    @Autowired
    BeerRepository beerRepository;


    @Test
    void testSaveBeer() {
        Beer beer = beerRepository.save(Beer.builder()
                .beerName("Test Beer")
                .beerStyle(guru.springframework.spring7restmvc.model.BeerStyle.ALE)
                .price(new java.math.BigDecimal("9.99"))
                .upc("123456789012")
                .build());

        beerRepository.flush();

        assertThat(beer, notNullValue());
        assertThat(beer.getId(), notNullValue());
        assertThat(beer.getBeerName(), equalTo("Test Beer"));
    }


    @Test
    void testSaveBeerTooLong() {
        assertThrows(ConstraintViolationException.class, () -> {
            Beer beer = beerRepository.save(Beer.builder()
                    .beerName("Test Beer with a very long name that is over fifty characters")
                    .beerStyle(guru.springframework.spring7restmvc.model.BeerStyle.ALE)
                    .price(new java.math.BigDecimal("9.99"))
                    .upc("123456789012")
                    .build());

            beerRepository.flush();
        });
    }


    @Test
    void testGetBeerListByName() {
        List<Beer> beers = beerRepository.findAllByBeerNameIsLikeIgnoreCase("%IPA%", null).getContent();
        assertThat(beers, hasSize(336));
    }


    @Test
    void testGetBeerListByStyle() {
        List<Beer> beers = beerRepository.findAllByBeerStyle(BeerStyle.ALE, null).getContent();
        assertThat(beers, hasSize(1554));
    }
}