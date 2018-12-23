import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.github.jamezrin.crtmcards.types.CardRenewal;
import com.github.jamezrin.crtmcards.types.CardType;
import com.github.jamezrin.crtmcards.types.CrtmCard;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.platform.commons.logging.Logger;
import org.junit.platform.commons.logging.LoggerFactory;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class TestJackson {
    private static ObjectMapper objectMapper;
    private static CrtmCard sampleCard;

    private static final Logger logger =
            LoggerFactory.getLogger(TestJackson.class);

    @BeforeAll
    public static void setUp() {
        long startMillis = System.currentTimeMillis();

        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());

        CardRenewal card1 = new CardRenewal(
                LocalDate.of(2018, 9, 5),
                LocalDate.of(2018, 9, 10),
                LocalDate.of(2018, 10, 15),
                LocalDate.of(2018, 10, 20)
        );

        CardRenewal card2 = new CardRenewal(
                LocalDate.of(2018, 8, 10),
                LocalDate.of(2018, 8, 15),
                LocalDate.of(2018, 9, 20),
                LocalDate.of(2018, 9, 25)
        );

        EnumMap<CardType, LocalDate> sampleProfiles = new EnumMap<>(CardType.class);
        sampleProfiles.put(CardType.BLUE, LocalDate.of(2018, 10, 20));

        sampleCard = new CrtmCard(
                "0123456789",
                "Titulo",
                CardType.NORMAL,
                new CardRenewal[] { card1, card2 },
                LocalDate.of(2018, 10, 20),
                sampleProfiles
        );

        long endMillis = System.currentTimeMillis() - startMillis;
        logger.info(() -> String.format("took %dms", endMillis));
    }

    @Test
    public void testSerialize1() {
        Map<String, Object> map = objectMapper.convertValue(sampleCard, Map.class);
        assertNotNull(map);
        assertEquals("0123456789", map.get("fullNum"));
        assertEquals("Titulo", map.get("title"));
        assertEquals("NORMAL", map.get("type"));
        assertEquals(Arrays.asList(2018, 10, 20), map.get("expirationDate"));
    }

    @Test
    public void testSerialize2() {
        Map<String, Object> map = objectMapper.convertValue(sampleCard, Map.class);
        assertNotNull(map);
        assertEquals("0123456789", map.get("fullNum"));
        assertEquals("Titulo", map.get("title"));
        assertEquals("NORMAL", map.get("type"));
        assertEquals(Arrays.asList(2018, 10, 20), map.get("expirationDate"));
    }

    @Test
    public void testDeserialize1() {
        Map<String, Object> map = new HashMap<>();
        map.put("fullNum", "0123456789");
        map.put("title", "Titulo");
        map.put("type", "NORMAL");
        map.put("expirationDate", Arrays.asList(2018, 10, 20));

        CrtmCard card = objectMapper.convertValue(map, CrtmCard.class);
        assertNotNull(card);
        assertEquals("0123456789", card.getFullNum());
        assertEquals("Titulo", card.getTitle());
        assertEquals(CardType.NORMAL, card.getType());
        assertEquals(LocalDate.of(2018, 10, 20), card.getExpirationDate());
    }

    @Test
    public void testDeserialize2() {
        Map<String, Object> map = new HashMap<>();
        map.put("fullNum", "0123456789");
        map.put("title", "Titulo");
        map.put("type", "NORMAL");
        map.put("expirationDate", Arrays.asList(2018, 10, 20));

        CrtmCard card = objectMapper.convertValue(map, CrtmCard.class);
        assertNotNull(card);
        assertEquals("0123456789", card.getFullNum());
        assertEquals("Titulo", card.getTitle());
        assertEquals(CardType.NORMAL, card.getType());
        assertEquals(LocalDate.of(2018, 10, 20), card.getExpirationDate());
    }
}
