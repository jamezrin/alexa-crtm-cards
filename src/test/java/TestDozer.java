
import com.github.dozermapper.core.DozerBeanMapperBuilder;
import com.github.dozermapper.core.Mapper;
import com.github.jamezrin.crtmcards.types.CardRenewal;
import com.github.jamezrin.crtmcards.types.CardType;
import com.github.jamezrin.crtmcards.types.CrtmCard;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.Map;

public class TestDozer {
    @Test
    public void test1() {
        // https://www.baeldung.com/dozer
        CrtmCard card = new CrtmCard("023232", "some title", CardType.NORMAL, new CardRenewal[] {
                new CardRenewal(LocalDate.now(), LocalDate.now(), LocalDate.now(), LocalDate.now()),
                new CardRenewal(LocalDate.now(), LocalDate.now(), LocalDate.now(), LocalDate.now())
        }, LocalDate.now(), null);

        Mapper mapper = DozerBeanMapperBuilder.buildDefault();
        Map<String, Object> result = mapper.map(card, Map.class);
        result.forEach((k, v) -> System.out.println(k + ": " + v));

        CrtmCard card2 = mapper.map(result, CrtmCard.class);
        System.out.println(card2);
    }
}
