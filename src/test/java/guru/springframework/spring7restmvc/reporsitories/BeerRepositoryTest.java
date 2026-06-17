package guru.springframework.spring7restmvc.reporsitories;

import guru.springframework.spring7restmvc.entities.Beer;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;


@DataJpaTest
class BeerRepositoryTest {

    @Autowired
    BeerRepository beerRepository;

    @Test
    void testSaveBeer(){
        Beer beer = beerRepository.save( Beer.builder()
                .beerName("Test Beer")
                .build());

        assertThat(beer, notNullValue());
        assertThat(beer.getId(), notNullValue());
        assertThat(beer.getBeerName(), equalTo("Test Beer"));
    }
}