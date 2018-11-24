import com.jamezrin.alexaskills.crtmcards.scraper.EndpointConnector;
import com.jamezrin.alexaskills.crtmcards.scraper.ResponseParser;
import com.jamezrin.alexaskills.crtmcards.scraper.ScraperUtils;
import com.jamezrin.alexaskills.crtmcards.scraper.exceptions.InactiveCardNumberException;
import com.jamezrin.alexaskills.crtmcards.scraper.exceptions.InvalidCardNumberException;
import com.jamezrin.alexaskills.crtmcards.scraper.exceptions.NotAvailableException;
import com.jamezrin.alexaskills.crtmcards.scraper.types.Card;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.junit.jupiter.api.Test;

import java.net.SocketTimeoutException;

import static com.jamezrin.alexaskills.crtmcards.scraper.ScraperUtils.makeHttpClient;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class TestScrapCrtm {
    private final String viewState = makeDefaultViewState();
    private final HttpClient httpClient = makeHttpClient(20000);

    @Test
    public void testOkCard1() throws Exception {
        try {
            EndpointConnector connector = new EndpointConnector(viewState, "001", "0040440120"); // Jaime
            HttpResponse response = connector.connect(httpClient);
            ResponseParser parser = new ResponseParser(response);
            Card card = parser.parse();
            System.out.println(ReflectionToStringBuilder.toString(card, ToStringStyle.MULTI_LINE_STYLE));
        } catch (NotAvailableException | SocketTimeoutException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testOkCard2() throws Exception {
        try {
            EndpointConnector connector = new EndpointConnector(viewState, "001", "0000884263"); // Ana
            HttpResponse response = connector.connect(httpClient);
            ResponseParser parser = new ResponseParser(response);
            Card card = parser.parse();
            System.out.println(ReflectionToStringBuilder.toString(card, ToStringStyle.MULTI_LINE_STYLE));
        } catch (NotAvailableException | SocketTimeoutException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testInactiveCard() throws Exception {
        try {
            EndpointConnector connector = new EndpointConnector(viewState, "001", "0000023768");
            HttpResponse response = connector.connect(httpClient);
            assertThrows(InactiveCardNumberException.class,
                    () -> {
                        try {
                            new ResponseParser(response).parse();
                        } catch (NotAvailableException e) {
                            e.printStackTrace();
                        }
                    }
            );
        } catch (SocketTimeoutException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testInvalidCard1() throws Exception {
        try {
            EndpointConnector connector = new EndpointConnector(viewState, "251", "4342343");
            HttpResponse response = connector.connect(httpClient);
            assertThrows(InvalidCardNumberException.class,
                    () -> new ResponseParser(response).parse()
            );
        } catch (SocketTimeoutException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testInvalidCard2() throws Exception {
        try {
            EndpointConnector connector = new EndpointConnector(viewState, "1337", "8374628231");
            HttpResponse response = connector.connect(httpClient);
            assertThrows(InvalidCardNumberException.class,
                    () -> new ResponseParser(response).parse()
            );
        } catch (SocketTimeoutException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testFetchViewState() throws Exception {
        String viewState = ScraperUtils.fetchViewState();
        assertNotNull(viewState);
    }

    @Test
    public void testConnectInactiveCard() throws Exception {
        EndpointConnector connector = new EndpointConnector(viewState, "001", "0000023768");
        HttpResponse response = connector.connect(httpClient);
        assertNotNull(response);
    }

    @Test
    public void testParseInactiveCard() throws Exception {
        EndpointConnector connector = new EndpointConnector(viewState, "001", "0000023768");
        HttpResponse response = connector.connect(httpClient);
        ResponseParser parser = new ResponseParser(response);
        assertNotNull(parser.getDocument());
    }

    public static String makeDefaultViewState() {
        return "/wEPDwUJNzkyNzg1MDMyD2QWAmYPZBYCAgMPZBYEAgMPDxYCHgRUZXh0BSBTw6FiYWRvLCAxMCBkZSBub3ZpZW1icm" +
                "UgZGUgMjAxOGRkAgUPZBYGAgEPDxYCHwAFCjEwLzExLzIwMThkZAIDDw8WAh8ABQUxNDozMWRkAgUPZBYCAgMPZBYC" +
                "Zg9kFgICAQ8QZA8WBmYCAQICAgMCBAIFFgYQBQMtLS0FAy0tLWcQBQMwMDEFAzAwMWcQBQMwMDIFAzAwMmcQBQMwMD" +
                "MFAzAwM2cQBQMxNzUFAzE3NWcQBQMyNTEFAzI1MWdkZGQBRJ/qa2v0OAMMeRdkpd2XFiCltKiJjyXS6doF/w0EQg==";
    }
}
