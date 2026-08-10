package guru.springframework.spring7restmvc.service;

import guru.springframework.spring7restmvc.model.BeerCSVRecord;
import org.junit.jupiter.api.Test;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.greaterThan;


class BeerCsvServiceImplTest {

  BeerCsvService beerCsvService = new BeerCsvServiceImpl();


  @Test
  void testConvertCSV() throws FileNotFoundException {
    File file = ResourceUtils.getFile("classpath:csvdata/beers.csv");

    List<BeerCSVRecord> beerCSVRecords = beerCsvService.convertCSV(file);
    System.out.println(beerCSVRecords.getFirst().getBeer());
    System.out.println(beerCSVRecords.getFirst().getStyle());
    System.out.println(beerCSVRecords.size());

    assertThat(beerCSVRecords.size(), greaterThan(0));
  }
}
