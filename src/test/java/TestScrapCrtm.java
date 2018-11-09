import com.jamezrin.alexaskills.crtm_cards.scraper.EndpointConnector;
import com.jamezrin.alexaskills.crtm_cards.scraper.ResponseParser;
import com.jamezrin.alexaskills.crtm_cards.scraper.exceptions.ScraperException;
import org.junit.Test;

import java.io.IOException;

public class TestScrapCrtm {
    @Test
    public void testApp() {
        try {
            new ResponseParser(new EndpointConnector("001", "0040440120").connect()).parse();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ScraperException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            new ResponseParser(new EndpointConnector("001", "0000884263").connect()).parse();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ScraperException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
