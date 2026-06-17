package guru.springframework.spring7restmvc.mappers;
import guru.springframework.spring7restmvc.entities.Beer;
import guru.springframework.spring7restmvc.model.BeerDTO;
import org.mapstruct.Mapper;

@Mapper
public class BeerMapper {

    Beer beerDtoToBeer(BeerDTO beerDTO);

    BeerDTO beerToBeerDto(Beer beer);
}
