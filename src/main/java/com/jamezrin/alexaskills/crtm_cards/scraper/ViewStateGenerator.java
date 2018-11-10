package com.jamezrin.alexaskills.crtm_cards.scraper;

import com.amazonaws.util.Base64;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class ViewStateGenerator {
    public static final Locale SPAIN_LOCALE = Locale.forLanguageTag("es-ES");
    public static final ZoneId MADRID_ZONE = ZoneId.of("Europe/Madrid");
    public static final DateTimeFormatter LONG_DATE_FORMATTER = makeLongDateFormatter();
    public static final DateTimeFormatter SHORT_DATE_FORMATTER = makeShortDateFormatter();
    public static final DateTimeFormatter SHORT_TIME_FORMATTER = makeShortTimeFormatter();

    private final ZonedDateTime zonedDateTime;

    public ViewStateGenerator() {
        this.zonedDateTime = ZonedDateTime.now();
    }

    public ViewStateGenerator(ZonedDateTime zonedDateTime) {
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
        baos.write(0x05); // String container
        baos.write(0x09); // Length of string (9)
        baos.write("792785032".getBytes(StandardCharsets.UTF_8));
        baos.write(0x0f); // Pair
        baos.write(0x64); // Empty node
        baos.write(0x16); // Array list
        baos.write(0x02); // Length of array list (2)
        baos.write(0x66); // Number zero
        baos.write(0x0f); // Pair
        baos.write(0x64); // Empty node
        baos.write(0x16); // Array list
        baos.write(0x02); // Length of array list (2)
        baos.write(0x02); // Unsigned integer for next byte
        baos.write(0x03); // Number 3 (unsigned)
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
        baos.write(0x1e); // String container (same as 0x05)
        baos.write(0x04); // Length of string (4)
        baos.write(0x54); // Character T
        baos.write(0x65); // Character e
        baos.write(0x78); // Character x
        baos.write(0x74); // Character t
        baos.write(0x05); // String container

        String longDate = capitalizeFirst(LONG_DATE_FORMATTER.format(zonedDateTime));
        baos.write(longDate.length() + 1); // Length of string
        baos.write(longDate.getBytes(StandardCharsets.UTF_8)); // Long Date

        baos.write(0x64); // Empty node
        baos.write(0x64); // Empty node
        baos.write(0x02); // Unsigned integer for next byte
        baos.write(0x05); // Number 5 (unsigned)
        baos.write(0x0f); // Pair
        baos.write(0x64); // Empty node
        baos.write(0x16); // Array list
        baos.write(0x06); // Length of array list (6)
        baos.write(0x02); // Unsigned integer for next byte
        baos.write(0x01); // Number 1 (unsigned)
        baos.write(0x0f); // Pair
        baos.write(0x0f); // Pair
        baos.write(0x16); // Array list
        baos.write(0x02); // Length of array list (2)
        baos.write(0x1f); // String reference for next byte
        baos.write(0x00); // String reference value (0)
        baos.write(0x05); // String container

        String shortDate = SHORT_DATE_FORMATTER.format(zonedDateTime);
        baos.write(shortDate.length()); // Length of string
        baos.write(shortDate.getBytes(StandardCharsets.UTF_8)); // Simple Date

        baos.write(0x64); // Empty node
        baos.write(0x64); // Empty node
        baos.write(0x02); // Unsigned integer for next byte
        baos.write(0x03); // Number 3 (unsigned)
        baos.write(0x0f); // Pair
        baos.write(0x0f); // Pair
        baos.write(0x16); // Array list
        baos.write(0x02); // Length of array list (2)
        baos.write(0x1f); // String reference for next byte
        baos.write(0x00); // String reference value (0)
        baos.write(0x05); // String container

        String shortTime = SHORT_TIME_FORMATTER.format(zonedDateTime);
        baos.write(shortTime.length()); // Length of string
        baos.write(shortTime.getBytes(StandardCharsets.UTF_8)); // Simple Time

        baos.write(0x64); // Empty node
        baos.write(0x64); // Empty node
        baos.write(0x02); // Unsigned integer for next byte
        baos.write(0x05); // Number 5 (unsigned)

        // Automatically generated, couldn't care less
        baos.write(0x0f); // Pair
        baos.write(0x64); // Empty node
        baos.write(0x16); // Array list
        baos.write(0x02); // Unsigned integer for next byte
        baos.write(0x02); // Unsigned integer for next byte
        baos.write(0x03); // Unknown
        baos.write(0x0f); // Pair
        baos.write(0x64); // Empty node
        baos.write(0x16); // Array list
        baos.write(0x02); // Unsigned integer for next byte
        baos.write(0x66); // Unknown
        baos.write(0x0f); // Pair
        baos.write(0x64); // Empty node
        baos.write(0x16); // Array list
        baos.write(0x02); // Unsigned integer for next byte
        baos.write(0x02); // Unsigned integer for next byte
        baos.write(0x01); // Unknown
        baos.write(0x0f); // Pair
        baos.write(0x10); // Unknown
        baos.write(0x64); // Empty node
        baos.write(0x0f); // Pair
        baos.write(0x16); // Array list
        baos.write(0x06); // Unknown
        baos.write(0x66); // Unknown
        baos.write(0x02); // Unsigned integer for next byte
        baos.write(0x01); // Unknown
        baos.write(0x02); // Unsigned integer for next byte
        baos.write(0x02); // Unsigned integer for next byte
        baos.write(0x02); // Unsigned integer for next byte
        baos.write(0x03); // Unknown
        baos.write(0x02); // Unsigned integer for next byte
        baos.write(0x04); // Unknown
        baos.write(0x02); // Unsigned integer for next byte
        baos.write(0x05); // String container
        baos.write(0x16); // Array list
        baos.write(0x06); // Unknown
        baos.write(0x10); // Unknown
        baos.write(0x05); // String container
        baos.write(0x03); // Unknown
        baos.write(0x2d); // Unknown
        baos.write(0x2d); // Unknown
        baos.write(0x2d); // Unknown
        baos.write(0x05); // String container
        baos.write(0x03); // Unknown
        baos.write(0x2d); // Unknown
        baos.write(0x2d); // Unknown
        baos.write(0x2d); // Unknown
        baos.write(0x67); // Unknown
        baos.write(0x10); // Unknown
        baos.write(0x05); // String container
        baos.write(0x03); // Unknown
        baos.write(0x30); // Unknown
        baos.write(0x30); // Unknown
        baos.write(0x31); // Unknown
        baos.write(0x05); // String container
        baos.write(0x03); // Unknown
        baos.write(0x30); // Unknown
        baos.write(0x30); // Unknown
        baos.write(0x31); // Unknown
        baos.write(0x67); // Unknown
        baos.write(0x10); // Unknown
        baos.write(0x05); // String container
        baos.write(0x03); // Unknown
        baos.write(0x30); // Unknown
        baos.write(0x30); // Unknown
        baos.write(0x32); // Unknown
        baos.write(0x05); // String container
        baos.write(0x03); // Unknown
        baos.write(0x30); // Unknown
        baos.write(0x30); // Unknown
        baos.write(0x32); // Unknown
        baos.write(0x67); // Unknown
        baos.write(0x10); // Unknown
        baos.write(0x05); // String container
        baos.write(0x03); // Unknown
        baos.write(0x30); // Unknown
        baos.write(0x30); // Unknown
        baos.write(0x33); // Unknown
        baos.write(0x05); // String container
        baos.write(0x03); // Unknown
        baos.write(0x30); // Unknown
        baos.write(0x30); // Unknown
        baos.write(0x33); // Unknown
        baos.write(0x67); // Unknown
        baos.write(0x10); // Unknown
        baos.write(0x05); // String container
        baos.write(0x03); // Unknown
        baos.write(0x31); // Unknown
        baos.write(0x37); // Unknown
        baos.write(0x35); // Unknown
        baos.write(0x05); // String container
        baos.write(0x03); // Unknown
        baos.write(0x31); // Unknown
        baos.write(0x37); // Unknown
        baos.write(0x35); // Unknown
        baos.write(0x67); // Unknown
        baos.write(0x10); // Unknown
        baos.write(0x05); // String container
        baos.write(0x03); // Unknown
        baos.write(0x32); // Unknown
        baos.write(0x35); // Unknown
        baos.write(0x31); // Unknown
        baos.write(0x05); // String container
        baos.write(0x03); // Unknown
        baos.write(0x32); // Unknown
        baos.write(0x35); // Unknown
        baos.write(0x31); // Unknown
        baos.write(0x67); // Unknown
        baos.write(0x64); // Empty node
        baos.write(0x64); // Empty node
        baos.write(0x64); // Empty node
        baos.write(0x01); // Unknown
        baos.write(0x44); // Unknown
        baos.write(0x9f); // Unknown
        baos.write(0xea); // Unknown
        baos.write(0x6b); // Unknown
        baos.write(0x6b); // Unknown
        baos.write(0xf4); // Unknown
        baos.write(0x38); // Unknown
        baos.write(0x03); // Unknown
        baos.write(0x0c); // Unknown
        baos.write(0x79); // Unknown
        baos.write(0x17); // Unknown
        baos.write(0x64); // Empty node
        baos.write(0xa5); // Unknown
        baos.write(0xdd); // Unknown
        baos.write(0x97); // Unknown
        baos.write(0x16); // Array list
        baos.write(0x20); // Unknown
        baos.write(0xa5); // Unknown
        baos.write(0xb4); // Unknown
        baos.write(0xa8); // Unknown
        baos.write(0x89); // Unknown
        baos.write(0x8f); // Unknown
        baos.write(0x25); // Unknown
        baos.write(0xd2); // Unknown
        baos.write(0xe9); // Unknown
        baos.write(0xda); // Unknown
        baos.write(0x05); // String container
        baos.write(0xff); // Unknown
        baos.write(0x0d); // Unknown
        baos.write(0x04); // Unknown
        baos.write(0x42); // Unknown

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
                .ofPattern("HH:mm", SPAIN_LOCALE)
                .withZone(MADRID_ZONE);
    }
}
