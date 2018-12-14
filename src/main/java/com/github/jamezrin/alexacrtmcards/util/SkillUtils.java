package com.github.jamezrin.alexacrtmcards.util;

import com.amazon.ask.dispatcher.request.handler.HandlerInput;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.function.Predicate;

import static com.amazon.ask.request.Predicates.intentName;

public class SkillUtils {
    // https://docs.oracle.com/javase/8/docs/api/java/time/format/DateTimeFormatter.html
    public static String formatSpeechDate(LocalDate date) {
        return date.format(DateTimeFormatter.ofPattern(
                "EEEE dd 'de' MMMM",
                Locale.forLanguageTag("es-ES")
        ));
    }

    public static Predicate<HandlerInput> hasPersistentAttribute(String attributeName) {
        return handlerInput -> handlerInput.getAttributesManager().getPersistentAttributes()
                .containsKey(attributeName);
    }

    public static Predicate<HandlerInput> hasProvidedCard() {
        return hasPersistentAttribute("ttp_prefix")
                .and(hasPersistentAttribute("ttp_number"));
    }

    public static Predicate<HandlerInput> requiresCard() {
        return intentName("OverviewSkillIntent")
                .or(intentName("RemainingDaysIntent"))
                .or(intentName("ExpirationDateIntent"));
    }

    public static Predicate<HandlerInput> cardSetupNeeded() {
        return requiresCard().and(
                hasProvidedCard().negate());
    }
}
