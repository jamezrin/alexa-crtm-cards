package com.github.jamezrin.alexacrtmcards.util;

import com.amazon.ask.attributes.AttributesManager;
import com.amazon.ask.dispatcher.request.handler.HandlerInput;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.Map;

public class SkillUtils {
    // https://docs.oracle.com/javase/8/docs/api/java/time/format/DateTimeFormatter.html
    public static String formatSpeechDate(LocalDate date) {
        return date.format(DateTimeFormatter.ofPattern(
                "EEEE dd 'de' MMMM",
                Locale.forLanguageTag("es-ES")
        ));
    }

    public static boolean hasProvidedCard(HandlerInput input) {
        AttributesManager attributesManager = input.getAttributesManager();
        Map<String, Object> attributes = attributesManager.getPersistentAttributes();

        return attributes.containsKey("ttp_prefix") &&
                attributes.containsKey("ttp_number");
    }
}
