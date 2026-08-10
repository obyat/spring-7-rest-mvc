package guru.springframework.spring7restmvc.reporsitories;

import guru.springframework.spring7restmvc.entities.Beer;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.mysql.MySQLContainer;

import javax.sql.DataSource;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.greaterThan;


@Testcontainers
@SpringBootTest
@ActiveProfiles("localmysql")
public class MySqlIT {

  @Container
  static MySQLContainer mySQLContainer = new MySQLContainer("mysql:9");
  @Autowired
  DataSource dataSource;
  @Autowired
  BeerRepository beerRepository;

  @DynamicPropertySource
  static void mySqlProperties(DynamicPropertyRegistry registry) {
    registry.add("spring.datasource.username", mySQLContainer::getUsername);
    registry.add("spring.datasource.password", mySQLContainer::getPassword);
    registry.add("spring.datasource.url", mySQLContainer::getJdbcUrl);
  }

  @Test
  void testListBeers() {
    System.out.println("I'm here");
    System.out.println("DataSource: " + dataSource);
    System.out.println("BeerRepository: " + beerRepository);

    List<Beer> beers = beerRepository.findAll();

    assertThat(beers.size(), greaterThan(0));
  }
}
