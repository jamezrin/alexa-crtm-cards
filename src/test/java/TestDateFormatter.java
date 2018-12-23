import com.github.jamezrin.alexacrtmcards.util.HumanDateFormatter;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestDateFormatter {
    @Test
    // https://stackoverflow.com/a/27287307
    public void testFormat1() {
        LocalDate date = LocalDate.of(2018, 12, 21);
        LocalDate now = LocalDate.of(2018, 12, 20);
        assertEquals("mañana", HumanDateFormatter.formatPrettyDiff(date, now));
    }

    @Test
    public void testFormat2() {
        LocalDate date = LocalDate.of(2018, 12, 21);
        LocalDate now = LocalDate.of(2018, 12, 21);
        assertEquals("hoy", HumanDateFormatter.formatPrettyDiff(date, now));
    }

    @Test
    public void testFormat3() {
        LocalDate date = LocalDate.of(2018, 12, 21);
        LocalDate now = LocalDate.of(2018, 12, 22);
        assertEquals("ayer", HumanDateFormatter.formatPrettyDiff(date, now));
    }

    @Test
    public void testFormat4() {
        LocalDate date = LocalDate.of(2018, 12, 10);
        LocalDate now = LocalDate.of(2018, 11, 22);
        assertEquals("lunes 10 de diciembre", HumanDateFormatter.formatPrettyDiff(date, now));
    }
}
