import com.jamezrin.alexaskills.crtm_cards.scraper.ResponseParser;
import org.junit.Test;

import java.time.LocalDate;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class TestDateParsing {
    @Test
    public void testOkDate1() {
        String string = "Caduca: 05-05-2025";
        LocalDate date = ResponseParser.extractDate(string);
        assertEquals(5, date.getDayOfMonth());
        assertEquals(5, date.getMonthValue());
        assertEquals(2025, date.getYear());
    }

    @Test
    public void testOkDate2() {
        String string = "Caduca: 05-10-2025";
        LocalDate date = ResponseParser.extractDate(string);
        assertEquals(5, date.getDayOfMonth());
        assertEquals(10, date.getMonthValue());
        assertEquals(2025, date.getYear());
    }

    @Test
    public void testEmptyDate() {
        String string = "Caduca:";
        LocalDate date = ResponseParser.extractDate(string);
        assertNull(date);
    }

    @Test
    public void testNoDate() {
        String string = "";
        LocalDate date = ResponseParser.extractDate(string);
        assertNull(date);
    }

    @Test
    public void testInvalidDate() {
        String string = "Caduca: 2010/10/10";
        LocalDate date = ResponseParser.extractDate(string);
        assertNull(date);
    }
}
