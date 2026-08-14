package guru.springframework.spring7restmvc.reporsitories;

import guru.springframework.spring7restmvc.entities.Beer;
import guru.springframework.spring7restmvc.entities.Category;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;


@SpringBootTest
class CategoryRepositoryTest {

    @Autowired
    CategoryRepository categoryRepository;

    @Autowired
    BeerRepository beerRepository;

    Beer testBeer;


    @BeforeEach
    void setUp() {
        testBeer = beerRepository.findAll().get(0);
    }


    @Transactional
    @Test
    void testAddCategory() {
        Category savedCategory = categoryRepository.save(Category.builder()
                .description("Test Description")
                .build());

        testBeer.addCategory(savedCategory);
        Beer savedBeer = beerRepository.save(testBeer);

        System.out.println("Saved Beer: " + savedBeer.getBeerName());
        System.out.println("Saved Beer Categories: " + savedBeer.getCategories().toString());
    }
}