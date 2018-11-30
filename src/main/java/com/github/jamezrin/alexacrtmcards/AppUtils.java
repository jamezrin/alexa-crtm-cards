package com.github.jamezrin.alexacrtmcards;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class AppUtils {
    // https://docs.oracle.com/javase/8/docs/api/java/time/format/DateTimeFormatter.html
    public static String formatSpeechDate(LocalDate date) {
        return date.format(DateTimeFormatter.ofPattern(
                "EEEE dd 'de' MMMM",
                Locale.forLanguageTag("es-ES")
        ));
    }
}
