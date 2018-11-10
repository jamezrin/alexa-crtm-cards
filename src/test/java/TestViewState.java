import com.amazonaws.util.Base64;
import com.jamezrin.alexaskills.crtm_cards.scraper.EndpointConnector;
import com.jamezrin.alexaskills.crtm_cards.scraper.PlainViewStateGenerator;
import org.apache.http.client.HttpClient;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.time.temporal.TemporalAccessor;

import static com.jamezrin.alexaskills.crtm_cards.scraper.PlainViewStateGenerator.MADRID_ZONE;

public class TestViewState {
    private static final HttpClient httpClient = EndpointConnector.makeHttpClient();

    @Test
    public void testViewState() throws IOException {
        printViewState("/wEPDwUJNzkyNzg1MDMyD2QWAmYPZBYCAgMPZBYEAgMPDxYCHgRUZXh0BSBTw6FiYWRvLCAxMCBkZSBub3ZpZW1icmUgZGUgMjAxOGRkAgUPZBYGAgEPDxYCHwAFCjEwLzExLzIwMThkZAIDDw8WAh8ABQUwMToyM2RkAgUPZBYCAgMPZBYCZg9kFgICAQ8QZA8WBmYCAQICAgMCBAIFFgYQBQMtLS0FAy0tLWcQBQMwMDEFAzAwMWcQBQMwMDIFAzAwMmcQBQMwMDMFAzAwM2cQBQMxNzUFAzE3NWcQBQMyNTEFAzI1MWdkZGQ2PRthWZh/3QZyzrNBRulTrszEjMQ/S24+ed45cwqTOw==");
        LocalDateTime localDateTime = LocalDateTime.of(2018, 11, 9, 1, 28);
        ZonedDateTime zonedDateTime = localDateTime.atZone(MADRID_ZONE);

        PlainViewStateGenerator generator = new PlainViewStateGenerator(zonedDateTime);
        printViewState(generator.generate());
    }

    private void printStringBytes(String string) {
        System.out.printf("Content of \"%s\": ", string);

        for (byte x : string.getBytes(StandardCharsets.UTF_8)) {
            System.out.printf("0x%x ", x);
        }

        System.out.println();
    }

    private void printViewState(String viewState) {
        byte[] arr = Base64.decode(viewState);

        for (byte x : arr) {
            System.out.printf("0x%02x ", x);
        }

        System.out.println();
    }
}
