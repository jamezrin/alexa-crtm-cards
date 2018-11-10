import com.jamezrin.alexaskills.crtm_cards.scraper.ViewStateGenerator;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;

import static com.jamezrin.alexaskills.crtm_cards.scraper.ViewStateGenerator.MADRID_ZONE;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestViewState {
    @Test
    public void testViewStateGenerator() throws IOException {
        String expected =
                "/wEPDwUJNzkyNzg1MDMyD2QWAmYPZBYCAgMPZBYEAgMPDxYCHgRUZXh0BSBTw6FiYWRvLCAxMCBkZSBub3ZpZW1icm" +
                "UgZGUgMjAxOGRkAgUPZBYGAgEPDxYCHwAFCjEwLzExLzIwMThkZAIDDw8WAh8ABQUxNDozMWRkAgUPZBYCAgMPZBYC" +
                "Zg9kFgICAQ8QZA8WBmYCAQICAgMCBAIFFgYQBQMtLS0FAy0tLWcQBQMwMDEFAzAwMWcQBQMwMDIFAzAwMmcQBQMwMD" +
                "MFAzAwM2cQBQMxNzUFAzE3NWcQBQMyNTEFAzI1MWdkZGQBRJ/qa2v0OAMMeRdkpd2XFiCltKiJjyXS6doF/w0EQg==";
        LocalDateTime localDateTime = LocalDateTime.of(2018, 11, 10, 14, 31);
        ZonedDateTime zonedDateTime = localDateTime.atZone(MADRID_ZONE);
        ViewStateGenerator generator = new ViewStateGenerator(zonedDateTime);

        assertEquals(expected, generator.generate());
    }
}
