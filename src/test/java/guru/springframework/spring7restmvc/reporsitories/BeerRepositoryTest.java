package guru.springframework.spring7restmvc.reporsitories;

import guru.springframework.spring7restmvc.entities.Beer;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.jupiter.api.Assertions.assertThrows;


@DataJpaTest
class BeerRepositoryTest {

    @Autowired
    BeerRepository beerRepository;

    @Test
    void testSaveBeer(){
        Beer beer = beerRepository.save( Beer.builder()
                .beerName("Test Beer")
                .price(new java.math.BigDecimal("9.99"))
                .upc("123456789012")
                .build());

        beerRepository.flush();

        assertThat(beer, notNullValue());
        assertThat(beer.getId(), notNullValue());
        assertThat(beer.getBeerName(), equalTo("Test Beer"));
    }


    @Test
    void testSaveBeerTooLong(){
        assertThrows(ConstraintViolationException.class, () -> {
            Beer beer = beerRepository.save( Beer.builder()
                    .beerName("Test Beer with a very long name that is over fifty characters")
                    .price(new java.math.BigDecimal("9.99"))
                    .upc("123456789012")
                    .build());

            beerRepository.flush();
        });
    }
}