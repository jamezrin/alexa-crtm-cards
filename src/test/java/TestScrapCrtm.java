import com.jamezrin.alexaskills.crtmcards.scraper.EndpointConnector;
import com.jamezrin.alexaskills.crtmcards.scraper.ResponseParser;
import com.jamezrin.alexaskills.crtmcards.scraper.exceptions.InactiveCardNumberException;
import com.jamezrin.alexaskills.crtmcards.scraper.exceptions.InvalidCardNumberException;
import com.jamezrin.alexaskills.crtmcards.scraper.exceptions.NotAvailableException;
import com.jamezrin.alexaskills.crtmcards.scraper.types.Card;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.junit.jupiter.api.Test;

import java.io.InputStream;
import java.net.SocketTimeoutException;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class TestScrapCrtm {
    @Test
    public void testOkCard1() throws Exception {
        try {
            EndpointConnector connector = new EndpointConnector("001", "0040440120"); // Jaime
            InputStream is = connector.connect();
            ResponseParser parser = new ResponseParser(is);
            Card card = parser.parse();
            System.out.println(ReflectionToStringBuilder.toString(card, ToStringStyle.MULTI_LINE_STYLE));
        } catch (NotAvailableException | SocketTimeoutException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testOkCard2() throws Exception {
        try {
            EndpointConnector connector = new EndpointConnector("001", "0000884263"); // Ana
            InputStream is = connector.connect();
            ResponseParser parser = new ResponseParser(is);
            Card card = parser.parse();
            System.out.println(ReflectionToStringBuilder.toString(card, ToStringStyle.MULTI_LINE_STYLE));
        } catch (NotAvailableException | SocketTimeoutException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testInactiveCard() throws Exception {
        try {
            EndpointConnector connector = new EndpointConnector("001", "0000023768");
            InputStream is = connector.connect();
            assertThrows(InactiveCardNumberException.class,
                    () -> {
                        try {
                            new ResponseParser(is).parse();
                        } catch (NotAvailableException e) {
                            e.printStackTrace();
                        }
                    }
            );
        } catch (NotAvailableException | SocketTimeoutException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testInvalidCard1() throws Exception {
        try {
            EndpointConnector connector = new EndpointConnector("251", "4342343");
            InputStream is = connector.connect();
            assertThrows(InvalidCardNumberException.class,
                    () -> new ResponseParser(is).parse()
            );
        } catch (NotAvailableException | SocketTimeoutException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testInvalidCard2() throws Exception {
        try {
            EndpointConnector connector = new EndpointConnector("1337", "8374628231");
            InputStream is = connector.connect();
            assertThrows(InvalidCardNumberException.class,
                    () -> new ResponseParser(is).parse()
            );
        } catch (NotAvailableException | SocketTimeoutException e) {
            e.printStackTrace();
        }
    }
}
