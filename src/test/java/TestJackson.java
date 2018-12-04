import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.github.jamezrin.crtmcards.types.CardRenewal;
import com.github.jamezrin.crtmcards.types.CardType;
import com.github.jamezrin.crtmcards.types.CrtmCard;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.Map;

public class TestJackson {
    @Test
    public void test1() {
        CrtmCard card = new CrtmCard("023232", "some title", CardType.NORMAL, new CardRenewal[] {
                new CardRenewal(LocalDate.now(), LocalDate.now(), LocalDate.now(), LocalDate.now()),
                new CardRenewal(LocalDate.now(), LocalDate.now(), LocalDate.now(), LocalDate.now())
        }, LocalDate.now(), null);

        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        Map<String, Object> result = mapper.convertValue(card, Map.class);
        result.forEach((k, v) -> System.out.println(k + ": " + v));

        CrtmCard card2 = mapper.convertValue(result, CrtmCard.class);
        System.out.println(card2);
    }
}
