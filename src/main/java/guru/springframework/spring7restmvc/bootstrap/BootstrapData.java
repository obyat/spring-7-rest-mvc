package guru.springframework.spring7restmvc.bootstrap;

import guru.springframework.spring7restmvc.entities.Beer;
import guru.springframework.spring7restmvc.entities.Customer;
import guru.springframework.spring7restmvc.model.BeerStyle;
import guru.springframework.spring7restmvc.reporsitories.BeerRepository;
import guru.springframework.spring7restmvc.reporsitories.CustomerRepository;
import guru.springframework.spring7restmvc.service.BeerCsvService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;


@Component
@RequiredArgsConstructor
public class BootstrapData implements CommandLineRunner {

  private final BeerRepository beerRepository;
  private final CustomerRepository customerRepository;
  private final BeerCsvService beerCsvService;


  private static BeerStyle getBeerStyleEnum(String beerStyle) {
    return switch (beerStyle.toLowerCase()) {
      case String s when s.contains("lager") -> BeerStyle.LAGER;
      case String s when s.contains("ipa") -> BeerStyle.IPA;
      case String s when s.contains("stout") -> BeerStyle.STOUT;
      case String s when s.contains("pilsner") -> BeerStyle.PILSNER;
      default -> BeerStyle.ALE;
    };
  }


  /**
   * Callback used to run the bean.
   *
   * @param args incoming main method arguments
   * @throws Exception on error
   */
  @Override
  public void run(String... args) throws Exception {
    populateCSVBeerDb();
    populateBeerDb();
    populateCustomerDb();
  }


  private void populateCSVBeerDb() throws FileNotFoundException {
    if (beerRepository.count() < 100) {
      File file = ResourceUtils.getFile("classpath:csvdata/beers.csv");
      List<Beer> beers =
              beerCsvService.convertCSV(file).stream()
                      .map(
                              beerCSVRecord -> {
                                return Beer.builder()
                                        .beerName(StringUtils.abbreviate(beerCSVRecord.getBeer(), 50))
                                        .beerStyle(getBeerStyleEnum(beerCSVRecord.getStyle()))
                                        .upc(beerCSVRecord.getId().toString())
                                        .price(new BigDecimal("12.99"))
                                        .quantityOnHand(beerCSVRecord.getCount())
                                        .createdDate(LocalDateTime.now())
                                        .updatedDate(LocalDateTime.now())
                                        .build();
                              })
                      .toList();
      beerRepository.saveAll(beers);
    }
  }


  private void populateBeerDb() {
    if (beerRepository.count() > 0) {
      return;
    }

    Beer beer1 =
            Beer.builder()
                    .beerName("Midnight Hops")
                    .beerStyle(BeerStyle.IPA)
                    .upc("98237465")
                    .price(new BigDecimal("14.99"))
                    .quantityOnHand(88)
                    .createdDate(LocalDateTime.now())
                    .updatedDate(LocalDateTime.now())
                    .build();

    Beer beer2 =
            Beer.builder()
                    .beerName("Sunset Lager")
                    .beerStyle(BeerStyle.LAGER)
                    .upc("44556677")
                    .price(new BigDecimal("10.49"))
                    .quantityOnHand(210)
                    .createdDate(LocalDateTime.now())
                    .updatedDate(LocalDateTime.now())
                    .build();

    Beer beer3 =
            Beer.builder()
                    .beerName("Velvet Stout")
                    .beerStyle(BeerStyle.STOUT)
                    .upc("77889900")
                    .price(new BigDecimal("16.75"))
                    .quantityOnHand(64)
                    .createdDate(LocalDateTime.now())
                    .updatedDate(LocalDateTime.now())
                    .build();

    Beer beer4 =
            Beer.builder()
                    .beerName("Citrus Blaze IPA")
                    .beerStyle(BeerStyle.IPA)
                    .upc("11235813")
                    .price(new BigDecimal("13.25"))
                    .quantityOnHand(145)
                    .createdDate(LocalDateTime.now())
                    .updatedDate(LocalDateTime.now())
                    .build();

    Beer beer5 =
            Beer.builder()
                    .beerName("Arctic Pilsner")
                    .beerStyle(BeerStyle.PILSNER)
                    .upc("99887766")
                    .price(new BigDecimal("11.99"))
                    .quantityOnHand(97)
                    .createdDate(LocalDateTime.now())
                    .updatedDate(LocalDateTime.now())
                    .build();

    beerRepository.saveAll(List.of(beer1, beer2, beer3, beer4, beer5));
  }


  private void populateCustomerDb() {
    if (customerRepository.count() > 0) {
      return;
    }

    Customer Customer1 =
            Customer.builder().customerName("Customer 1").email("customeEmail@gmail.com").build();

    Customer Customer2 = Customer.builder().customerName("Customer 2").build();

    Customer Customer3 = Customer.builder().customerName("Customer 3").build();

    customerRepository.saveAll(List.of(Customer1, Customer2, Customer3));
  }
}
