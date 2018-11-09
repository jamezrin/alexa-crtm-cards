import com.jamezrin.alexaskills.crtm_cards.scraper.EndpointConnector;
import com.jamezrin.alexaskills.crtm_cards.scraper.ResponseParser;
import com.jamezrin.alexaskills.crtm_cards.scraper.exceptions.ScraperException;
import com.jamezrin.alexaskills.crtm_cards.scraper.types.Card;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Pattern;

import static org.junit.Assert.*;

public class TestScrapCrtm {
    @Test
    public void testAna() throws Exception {
        EndpointConnector connector = new EndpointConnector("001", "0040440120");
        InputStream is = connector.connect();
        ResponseParser parser = new ResponseParser(is);
        Card card = parser.parse();
        System.out.println(card);
    }

    @Test
    public void testJaime() throws Exception {
        EndpointConnector connector = new EndpointConnector("001", "0000884263");
        InputStream is = connector.connect();
        ResponseParser parser = new ResponseParser(is);
        Card card = parser.parse();
        System.out.println(card);
    }
}
