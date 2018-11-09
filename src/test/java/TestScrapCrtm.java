import com.jamezrin.alexaskills.crtm_cards.scraper.EndpointConnector;
import com.jamezrin.alexaskills.crtm_cards.scraper.ResponseParser;
import com.jamezrin.alexaskills.crtm_cards.scraper.exceptions.InactiveCardNumberException;
import com.jamezrin.alexaskills.crtm_cards.scraper.exceptions.InvalidCardNumberException;
import com.jamezrin.alexaskills.crtm_cards.scraper.types.Card;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.InputStream;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class TestScrapCrtm {
    @Test
    @DisplayName("Card Holder: Jaime")
    public void testOkCard1() throws Exception {
        EndpointConnector connector = new EndpointConnector("001", "0040440120");
        InputStream is = connector.connect();
        ResponseParser parser = new ResponseParser(is);
        Card card = parser.parse();
        System.out.println(ReflectionToStringBuilder.toString(card, ToStringStyle.MULTI_LINE_STYLE));
    }

    @Test
    @DisplayName("Card Holder: Ana")
    public void testOkCard2() throws Exception {
        EndpointConnector connector = new EndpointConnector("001", "0000884263");
        InputStream is = connector.connect();
        ResponseParser parser = new ResponseParser(is);
        Card card = parser.parse();
        System.out.println(ReflectionToStringBuilder.toString(card, ToStringStyle.MULTI_LINE_STYLE));
    }

    @Test
    public void testInactiveCard() throws Exception {
        EndpointConnector connector = new EndpointConnector("251", "0011652831");
        InputStream is = connector.connect();
        assertThrows(InactiveCardNumberException.class,
            () -> new ResponseParser(is).parse()
        );
    }

    @Test
    public void testInvalidCard1() throws Exception {
        EndpointConnector connector = new EndpointConnector("251", "4342343");
        InputStream is = connector.connect();
        assertThrows(InvalidCardNumberException.class,
            () -> new ResponseParser(is).parse()
        );
    }

    @Test
    public void testInvalidCard2() throws Exception {
        EndpointConnector connector = new EndpointConnector("1337", "8374628231");
        InputStream is = connector.connect();
        assertThrows(InvalidCardNumberException.class,
                () -> new ResponseParser(is).parse()
        );
    }
}
