package guru.springframework.spring7restmvc.controller;

import guru.springframework.spring7restmvc.entities.Beer;
import guru.springframework.spring7restmvc.model.BeerDTO;
import guru.springframework.spring7restmvc.reporsitories.BeerRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;


@SpringBootTest // complete sp context not just splice
class BeerControllerIT {

    @Autowired
    BeerController beerController;

    @Autowired
    BeerRepository beerRepository;

    @Test
    void testListBeers(){
        List<BeerDTO> dtos = beerController.getAllBeers();

        assertThat(dtos.size()).isEqualTo(5);
    }


    @Test
    void testGetBeerById() {
        Beer beer = beerRepository.findAll().get(0);

        BeerDTO dto = beerController.getBeerById(beer.getId());

        assertThat(dto.getId()).isEqualTo(beer.getId());
    }


    @Test
    void testBeerIdNotFound() {
        assertThrows(NotFoundException.class, () -> {
            beerController.getBeerById(UUID.randomUUID());
        });
    }


    @Transactional // rolls back db to original undeleted state
    @Test
    void testEmptyBeers() {
        beerRepository.deleteAll();
        List<BeerDTO> dtos = beerController.getAllBeers();

        assertThat(dtos.size()).isEqualTo(0);
    }
}