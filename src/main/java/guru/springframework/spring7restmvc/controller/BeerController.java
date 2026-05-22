package guru.springframework.spring7restmvc.controller;

import guru.springframework.spring7restmvc.model.Beer;
import guru.springframework.spring7restmvc.service.BeerService;
import jdk.jfr.Category;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@Slf4j
@AllArgsConstructor
@RestController
public class BeerController {
    private final BeerService beerService;

    @RequestMapping("/api/v1/beer")
    public List<Beer> getAllBeers() {
        return this.beerService.listBeers();
    }

    @RequestMapping("/api/v1/beer/{id}")
    public Beer getBeerById(UUID id){
        log.debug("Getting beer by id in controller: {}", id);

        return beerService.getBeerById(id);
    }
}