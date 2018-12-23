package com.github.jamezrin.alexacrtmcards.util;

import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class HumanDateFormatter {
    public static String formatPrettyDate(LocalDate date) {
        return date.format(DateTimeFormatter.ofPattern(
                "EEEE dd 'de' MMMM",
                Locale.forLanguageTag("es-ES")
        ));
    }

    public static String formatPrettyDiff(LocalDate date, LocalDate now) {
        Period period = now.until(date);

        if (period.getMonths() == 0 &&
                period.getYears() == 0) {
            switch (period.getDays()) {
                case 1: return "mañana";
                case 0: return "hoy";
                case -1: return "ayer";
            }
        }

        return formatPrettyDate(date);
    }

    public static String formatPrettyDiff(LocalDate date) {
        return formatPrettyDiff(date, LocalDate.now());
    }
}
