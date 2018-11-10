import com.amazonaws.util.Base64;
import com.jamezrin.alexaskills.crtm_cards.AppConsts;
import com.jamezrin.alexaskills.crtm_cards.scraper.EndpointConnector;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.ByteBuffer;

import static com.jamezrin.alexaskills.crtm_cards.AppConsts.CRTM_BASE_URI;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestViewState {
    private static HttpClient httpClient = EndpointConnector.makeHttpClient();

    @Test
    public void testViewState() throws IOException {
        printViewState("/wEPDwUJNzkyNzg1MDMyD2QWAmYPZBYCAgMPZBYEAgMPDxYCHgRUZXh0BSBWaWVybmVzLCAwOSBkZSBub3ZpZW1icmUgZGUgMjAxOGRkAgUPZBYGAgEPDxYCHwAFCjA5LzExLzIwMThkZAIDDw8WAh8ABQUwMToyOGRkAgUPZBYCAgMPZBYCZg9kFgICAQ8QZA8WBmYCAQICAgMCBAIFFgYQBQMtLS0FAy0tLWcQBQMwMDEFAzAwMWcQBQMwMDIFAzAwMmcQBQMwMDMFAzAwM2cQBQMxNzUFAzE3NWcQBQMyNTEFAzI1MWdkZGToxCNvBRzJegbOeKpmT39Wqme8cu0qpIFR9XJrvmmEcw==");
        printViewState("/wEPDwUJNzkyNzg1MDMyD2QWAmYPZBYCAgMPZBYEAgMPDxYCHgRUZXh0BSBTw6FiYWRvLCAxMCBkZSBub3ZpZW1icmUgZGUgMjAxOGRkAgUPZBYGAgEPDxYCHwAFCjEwLzExLzIwMThkZAIDDw8WAh8ABQUwMToyMWRkAgUPZBYCAgMPZBYCZg9kFgICAQ8QZA8WBmYCAQICAgMCBAIFFgYQBQMtLS0FAy0tLWcQBQMwMDEFAzAwMWcQBQMwMDIFAzAwMmcQBQMwMDMFAzAwM2cQBQMxNzUFAzE3NWcQBQMyNTEFAzI1MWdkZGRN1GrxRIDYnfmaafDY9bjtu41GoaKDZWhv+HgLhFH+kw==");
        printViewState("/wEPDwUJNzkyNzg1MDMyD2QWAmYPZBYCAgMPZBYEAgMPDxYCHgRUZXh0BSBTw6FiYWRvLCAxMCBkZSBub3ZpZW1icmUgZGUgMjAxOGRkAgUPZBYGAgEPDxYCHwAFCjEwLzExLzIwMThkZAIDDw8WAh8ABQUwMToyM2RkAgUPZBYCAgMPZBYCZg9kFgICAQ8QZA8WBmYCAQICAgMCBAIFFgYQBQMtLS0FAy0tLWcQBQMwMDEFAzAwMWcQBQMwMDIFAzAwMmcQBQMwMDMFAzAwM2cQBQMxNzUFAzE3NWcQBQMyNTEFAzI1MWdkZGQ2PRthWZh/3QZyzrNBRulTrszEjMQ/S24+ed45cwqTOw==");
        System.out.println(fetchViewState());



        System.out.println(ByteBuffer.wrap(new byte[]{0x7f}).get());
    }

    @Test
    public void testByteBufferConv() {
        System.out.println(0xff);
        assertEquals(127, ByteBuffer.wrap(new byte[]{0x7f}).get());
        //assertEquals(129, ByteBuffer.wrap(new byte[]{0xf, 0x10, 0x01}).getShort());
    }

    private void printViewState(String viewState) {
        byte[] arr = Base64.decode(viewState);

        for (byte x : arr) {
            System.out.printf("0x%02x ", x);
        }

        System.out.println();
    }

    public static String fetchViewState() throws IOException {
        HttpGet viewRequest = new HttpGet(AppConsts.CRTM_QUERY_URI);
        HttpResponse viewResponse = httpClient.execute(viewRequest);
        HttpEntity viewResponseEntity = viewResponse.getEntity();

        Document viewStateDocument = Jsoup.parse(viewResponseEntity.getContent(), "UTF-8", CRTM_BASE_URI);
        return viewStateDocument.getElementById("__VIEWSTATE").attr("value");
    }
}
