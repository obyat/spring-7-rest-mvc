package guru.springframework.spring7restmvc.bootstrap;

import guru.springframework.spring7restmvc.entities.Beer;
import guru.springframework.spring7restmvc.entities.Customer;
import guru.springframework.spring7restmvc.reporsitories.BeerRepository;
import guru.springframework.spring7restmvc.reporsitories.CustomerRepository;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
public class BootstrapData implements CommandLineRunner {
    
    private final BeerRepository beerRepository;
    private final CustomerRepository customerRepository;
    
    /**
     * Callback used to run the bean.
     *
     * @param args incoming main method arguments
     * @throws Exception on error
     */
    @Override
    public void run(String... args) throws Exception {
        populateBeerDb();
        populateCustomerDb();
    }

    private void populateBeerDb() {
        if (beerRepository.count() > 0) {
            return;
        }

        Beer beer1 =
                Beer.builder()
                        .beerName("Midnight Hops")
                        .upc("98237465")
                        .price(new BigDecimal("14.99"))
                        .quantityOnHand(88)
                        .createdDate(LocalDateTime.now())
                        .updatedDate(LocalDateTime.now())
                        .build();

        Beer beer2 =
                Beer.builder()
                        .beerName("Sunset Lager")
                        .upc("44556677")
                        .price(new BigDecimal("10.49"))
                        .quantityOnHand(210)
                        .createdDate(LocalDateTime.now())
                        .updatedDate(LocalDateTime.now())
                        .build();

        Beer beer3 =
                Beer.builder()
                        .beerName("Velvet Stout")
                        .upc("77889900")
                        .price(new BigDecimal("16.75"))
                        .quantityOnHand(64)
                        .createdDate(LocalDateTime.now())
                        .updatedDate(LocalDateTime.now())
                        .build();

        Beer beer4 =
                Beer.builder()
                        .beerName("Citrus Blaze IPA")
                        .upc("11235813")
                        .price(new BigDecimal("13.25"))
                        .quantityOnHand(145)
                        .createdDate(LocalDateTime.now())
                        .updatedDate(LocalDateTime.now())
                        .build();

        Beer beer5 =
                Beer.builder()
                        .beerName("Arctic Pilsner")
                        .upc("99887766")
                        .price(new BigDecimal("11.99"))
                        .quantityOnHand(97)
                        .createdDate(LocalDateTime.now())
                        .updatedDate(LocalDateTime.now())
                        .build();
        
        beerRepository.saveAll(List.of(beer1, beer2, beer3, beer4, beer5));;
    }

    private void populateCustomerDb() {
        if (customerRepository.count() > 0) {
            return;
        }

        Customer Customer1 =
                Customer.builder().customerName("Customer 1").version(1).build();

        Customer Customer2 =
                Customer.builder().customerName("Customer 2").version(2).build();

        Customer Customer3 =
                Customer.builder().customerName("Customer 3").version(3).build();

        customerRepository.saveAll(List.of(Customer1, Customer2, Customer3));
    }
}
