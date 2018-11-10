package com.jamezrin.alexaskills.crtm_cards.scraper;

import com.amazonaws.util.Base64;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class PlainViewStateGenerator {
    public static final Locale SPAIN_LOCALE = Locale.forLanguageTag("es-ES");
    public static final ZoneId MADRID_ZONE = ZoneId.of("Europe/Madrid");
    public static final DateTimeFormatter LONG_DATE_FORMATTER = makeLongDateFormatter();
    public static final DateTimeFormatter SHORT_DATE_FORMATTER = makeShortDateFormatter();
    public static final DateTimeFormatter SHORT_TIME_FORMATTER = makeShortTimeFormatter();

    private final ZonedDateTime zonedDateTime;

    public PlainViewStateGenerator() {
        this.zonedDateTime = ZonedDateTime.now();
    }

    public PlainViewStateGenerator(ZonedDateTime zonedDateTime) {
        this.zonedDateTime = zonedDateTime;
    }

    public ZonedDateTime getZonedDateTime() {
        return zonedDateTime;
    }

    public String generate() throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        baos.write(0xff); baos.write(0x01); // Preamble
        baos.write(0x0f); // Pair
        baos.write(0x0f); // Pair
        baos.write(0x05); // String of 0x09 chars
        baos.write(0x09); // Length of string (9)
        baos.write("792785032".getBytes(StandardCharsets.UTF_8));
        //baos.write(new byte[] { 0x37, 0x39, 0x32, 0x37, 0x38, 0x35, 0x30, 0x33, 0x32 }); // String "792785032"

        /*
        baos.write(0x0f); // Pair
        baos.write(0x64); // Empty node
        baos.write(0x16); // Array list
        baos.write(0x02); // Length of array list (2)
        baos.write(0x66); // Number zero
        baos.write(0x0f); // Pair
        baos.write(0x64); // Empty node
        baos.write(0x16); // Array list
        baos.write(0x04); // Length of array list (4)
        baos.write(0x02); // Unsigned integer for next byte
        baos.write(0x03); // Number 3 (unsigned)
        baos.write(0x0f); // Pair
        baos.write(0x0f); // Pair
        baos.write(0x16); // Array list
        baos.write(0x02); // Length of array list (2)
        baos.write(0x1e); // String (same as 0x05)
        baos.write(0x04); // Length of string (4)
        baos.write(0x54); // Character T
        baos.write(0x65); // Character e
        baos.write(0x78); // Character x
        baos.write(0x74); // Character t
        baos.write(0x05); // String
        baos.write(0x20); // Length of string (32)

        String longDate = capitalizeFirst(LONG_DATE_FORMATTER.format(zonedDateTime));
        baos.write(longDate.getBytes(StandardCharsets.UTF_8));
        */
        return Base64.encodeAsString(baos.toByteArray());
    }

    public static String capitalizeFirst(String string) {
        StringBuilder builder = new StringBuilder();

        if (string.length() >= 1) {
            builder.append(Character.toUpperCase(string.charAt(0)));
        }

        if (string.length() >= 2) {
            builder.append(string.substring(1));
        }

        return builder.toString();
    }

    private static DateTimeFormatter makeLongDateFormatter() {
        return DateTimeFormatter
                .ofPattern("EEEE, dd 'de' MMMM 'de' YYYY", SPAIN_LOCALE)
                .withZone(MADRID_ZONE);
    }

    private static DateTimeFormatter makeShortDateFormatter() {
        return DateTimeFormatter
                .ofPattern("dd/MM/YYYY", SPAIN_LOCALE)
                .withZone(MADRID_ZONE);
    }

    private static DateTimeFormatter makeShortTimeFormatter() {
        return DateTimeFormatter
                .ofPattern("HH:MM", SPAIN_LOCALE)
                .withZone(MADRID_ZONE);
    }
}
