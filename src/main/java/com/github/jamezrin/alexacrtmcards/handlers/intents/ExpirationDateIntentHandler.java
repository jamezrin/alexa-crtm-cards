package com.github.jamezrin.alexacrtmcards.handlers.intents;

import com.amazon.ask.attributes.AttributesManager;
import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.dispatcher.request.handler.RequestHandler;
import com.amazon.ask.model.Response;
import com.amazon.ask.response.ResponseBuilder;
import com.github.jamezrin.alexacrtmcards.util.HumanDateFormatter;
import com.github.jamezrin.crtmcards.EndpointClient;
import com.github.jamezrin.crtmcards.ResponseParser;
import com.github.jamezrin.crtmcards.exceptions.ScraperException;
import com.github.jamezrin.crtmcards.exceptions.UnsuccessfulRequestException;
import com.github.jamezrin.crtmcards.types.CardRenewal;
import com.github.jamezrin.crtmcards.types.CrtmCard;
import org.apache.http.HttpResponse;
import org.slf4j.Logger;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.Optional;

import static com.amazon.ask.request.Predicates.intentName;
import static com.github.jamezrin.alexacrtmcards.util.SkillPredicates.hasProvidedCard;
import static org.slf4j.LoggerFactory.getLogger;

public class ExpirationDateIntentHandler implements RequestHandler {
    private final EndpointClient endpointClient;

    private static Logger LOG = getLogger(ExpirationDateIntentHandler.class);

    public ExpirationDateIntentHandler(EndpointClient endpointClient) {
        this.endpointClient = endpointClient;
    }

    @Override
    public boolean canHandle(HandlerInput input) {
        return input.matches(intentName("ExpirationDateIntent")
                .and(hasProvidedCard()));
    }

    @Override
    public Optional<Response> handle(HandlerInput input) {
        ResponseBuilder builder = input.getResponseBuilder();
        AttributesManager attributesManager = input.getAttributesManager();
        Map<String, Object> attributes = attributesManager.getPersistentAttributes();

        String ttpPrefix = (String) attributes.get("ttp_prefix");
        String ttpNumber = (String) attributes.get("ttp_number");

        try {
            HttpResponse response = endpointClient.connect(ttpPrefix, ttpNumber);
            ResponseParser responseParser = new ResponseParser(response);
            CrtmCard card = responseParser.parse();

            try {
                if (!card.getRenewals().isEmpty()) {
                    CardRenewal lastRenewal = card.getRenewals().get(
                            card.getRenewals().size() - 1);
                    LocalDate expDate = lastRenewal.getExpirationDate();
                    if (expDate != null) {
                        if (expDate.isAfter(LocalDate.now())) {
                            // todo make similar thing in remaining days intent handler
                            String speechText = String.format(
                                    "Tu tarjeta caducará el dia <say-as interpret-as=\"date\">%s</say-as>",
                                    expDate.format(DateTimeFormatter.ofPattern("yyyyMMdd")));

                            builder.withSpeech(speechText);
                            builder.withReprompt(speechText);

                            builder.withSimpleCard("En vigor",
                                    String.format("Tu tarjeta caducará el dia %s",
                                    HumanDateFormatter.formatPrettyDate(expDate)));
                        } else {
                            String speechText = String.format(
                                    "Tu tarjeta caducó el dia <say-as interpret-as=\"date\">%s</say-as>",
                                    expDate.format(DateTimeFormatter.ofPattern("yyyyMMdd")));

                            builder.withSpeech(speechText);
                            builder.withReprompt(speechText);

                            builder.withSimpleCard("Caducada",
                                    String.format("Tu tarjeta caducó el dia %s",
                                    HumanDateFormatter.formatPrettyDate(expDate)));
                        }
                    } else {
                        String speechText = "Tu tarjeta ya ha caducado";
                        builder.withSpeech(speechText);
                        builder.withReprompt(speechText);
                        builder.withSimpleCard("Caducada", speechText);
                    }
                } else {
                    String speechText = "Tu tarjeta no se ha recargado nunca";
                    builder.withSpeech(speechText);
                    builder.withReprompt(speechText);
                    builder.withSimpleCard("Caducada", speechText);
                }
            } catch (ArrayIndexOutOfBoundsException e) {
                LOG.error(e.getMessage(), e);
                String speechText = "Esta tarjeta no se ha llegado a recargar nunca";
                builder.withSpeech(speechText);
                builder.withReprompt(speechText);
                builder.withSimpleCard("Error", speechText);
            }
        } catch (IOException | UnsuccessfulRequestException e) {
            LOG.error(e.getMessage(), e);
            String speechText = "No se ha podido contactar con el servidor. Inténtalo mas tarde";
            builder.withSpeech(speechText);
            builder.withReprompt(speechText);
            builder.withSimpleCard("Error", speechText);
        } catch (ScraperException e) {
            LOG.error(e.getMessage(), e);
            String speechText = "No se ha podido extraer la información. Inténtalo mas tarde";
            builder.withSpeech(speechText);
            builder.withReprompt(speechText);
            builder.withSimpleCard("Error", speechText);
        }

        builder.withShouldEndSession(true);
        return builder.build();
    }
}
