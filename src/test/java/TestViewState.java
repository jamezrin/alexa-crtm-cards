import com.jamezrin.alexaskills.crtm_cards.scraper.EndpointConnector;
import com.jamezrin.alexaskills.crtm_cards.scraper.ResponseParser;
import com.jamezrin.alexaskills.crtm_cards.scraper.ViewStateGenerator;
import com.jamezrin.alexaskills.crtm_cards.scraper.exceptions.ScraperException;
import com.jamezrin.alexaskills.crtm_cards.scraper.types.Card;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.junit.jupiter.api.Test;

import javax.xml.ws.Response;
import java.io.IOException;
import java.io.InputStream;
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
        ViewStateGenerator viewStateGenerator = new ViewStateGenerator(zonedDateTime);

        assertEquals(expected, viewStateGenerator.generate());
    }

    @Test
    public void testOkCard1() throws Exception {
        ViewStateGenerator viewStateGenerator = new ViewStateGenerator();
        String viewState = viewStateGenerator.generate();
        EndpointConnector connector = new EndpointConnector(viewState, "001", "0040440120"); // Jaime
        InputStream is = connector.connect();
        ResponseParser parser = new ResponseParser(is);

        tryParseFailProof(parser);
    }

    @Test
    public void testOkCard2() throws Exception {
        LocalDateTime localDateTime = LocalDateTime.of(2018, 11, 10, 14, 31);
        ZonedDateTime zonedDateTime = localDateTime.atZone(MADRID_ZONE);
        ViewStateGenerator viewStateGenerator = new ViewStateGenerator(zonedDateTime);

        String viewState = viewStateGenerator.generate();
        EndpointConnector connector = new EndpointConnector(viewState, "001", "0040440120"); // Jaime
        InputStream is = connector.connect();
        ResponseParser parser = new ResponseParser(is);

        tryParseFailProof(parser);
    }

    private void tryParseFailProof(ResponseParser parser) throws Exception {
        try {
            Card card = parser.parse();
            System.out.println(ReflectionToStringBuilder.toString(card, ToStringStyle.MULTI_LINE_STYLE));
        } catch (ScraperException e) {
            throw e;
        } catch (Exception e) {
            System.out.println(parser.getDocument().html());
            throw e;
        }
    }
}
