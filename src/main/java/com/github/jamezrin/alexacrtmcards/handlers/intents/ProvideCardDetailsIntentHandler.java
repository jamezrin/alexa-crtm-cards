package com.github.jamezrin.alexacrtmcards.handlers.intents;

import com.amazon.ask.attributes.AttributesManager;
import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.dispatcher.request.handler.RequestHandler;
import com.amazon.ask.model.*;
import com.amazon.ask.response.ResponseBuilder;
import com.github.jamezrin.crtmcards.EndpointClient;
import com.github.jamezrin.crtmcards.ResponseParser;
import com.github.jamezrin.crtmcards.exceptions.InvalidCardNumberException;
import com.github.jamezrin.crtmcards.types.CrtmCard;
import org.apache.http.HttpResponse;
import org.jsoup.helper.Validate;
import org.slf4j.Logger;

import java.util.Map;
import java.util.Optional;
import java.util.regex.Pattern;

import static com.amazon.ask.request.Predicates.intentName;
import static org.slf4j.LoggerFactory.getLogger;

public class ProvideCardDetailsIntentHandler implements RequestHandler {
    private static final Pattern PREFIX_PATTERN = Pattern.compile("^[0-9]{3}$");
    private static final Pattern NUMBER_PATTERN = Pattern.compile("^[0-9]{10}$");

    private final EndpointClient endpointClient;

    private static Logger LOG = getLogger(ProvideCardDetailsIntentHandler.class);

    public ProvideCardDetailsIntentHandler(EndpointClient endpointClient) {
        this.endpointClient = endpointClient;
    }

    @Override
    public boolean canHandle(HandlerInput input) {
        return input.matches(intentName("ProvideCardDetailsIntent"));
    }

    @Override
    public Optional<Response> handle(HandlerInput input) {
        ResponseBuilder builder = input.getResponseBuilder();
        Request request = input.getRequestEnvelope().getRequest();
        IntentRequest intentRequest = (IntentRequest) request;
        Intent intent = intentRequest.getIntent();
        Map<String, Slot> slots = intent.getSlots();

        Slot prefixSlot = slots.get("prefix");
        String prefixSlotValue = prefixSlot.getValue();
        if (prefixSlotValue != null) {
            if (!PREFIX_PATTERN
                    .matcher(prefixSlotValue)
                    .matches()) {
                builder.withSpeech(
                        "Dame los tres últimos dígitos de la primera linea situados en " +
                        "la parte frontal de tu tarjeta, al lado de tu foto");
                builder.addElicitSlotDirective("prefix", intent);
                return builder.build();
            }
        }

        Slot numberSlot = slots.get("number");
        String numberSlotValue = numberSlot.getValue();
        if (numberSlotValue != null) {
            if (!NUMBER_PATTERN
                    .matcher(numberSlotValue)
                    .matches()) {
                builder.withSpeech(
                        "Dame los diez dígitos de la segunda linea situados en la parte " +
                        "frontal de tu tarjeta, al lado de tu foto");
                builder.addElicitSlotDirective("number", intent);
                return builder.build();
            }
        }

        // For some reason the dialog state never gets to be `COMPLETED`
        if (prefixSlotValue != null && numberSlotValue != null) {
            try {
                HttpResponse response = endpointClient.connect(
                        prefixSlotValue, numberSlotValue);
                ResponseParser responseParser = new ResponseParser(response);
                CrtmCard card = responseParser.parse();
                Validate.notNull(card);

                AttributesManager attributesManager = input.getAttributesManager();
                Map<String, Object> attributes = attributesManager.getPersistentAttributes();

                attributes.put("ttp_prefix", prefixSlotValue);
                attributes.put("ttp_number", numberSlotValue);

                attributesManager.setPersistentAttributes(attributes);
                attributesManager.savePersistentAttributes();

                String speechText = "<p>¡Ya está todo! Ya no tendrás que hacer este paso nunca mas. </p>" +
                        "<p>Pregúntame algo como 'Cuando caduca mi tarjeta de transporte'</p>";
                builder.withSpeech(speechText);
                builder.withReprompt(speechText);
                builder.withSimpleCard("¡Ya está!", speechText);

                builder.withShouldEndSession(false);
            } catch (InvalidCardNumberException e) {
                LOG.error(e.getMessage(), e);
                String speechText = "<p>La tarjeta que has introducido no parece ser valida</p>" +
                        "<p>Vuelve a intentarlo</p>";
                builder.withSpeech(speechText);
                builder.withReprompt(speechText);
                builder.withSimpleCard("Tarjeta invalida", speechText);

                builder.withShouldEndSession(true);
            } catch (Exception e) {
                LOG.error(e.getMessage(), e);
                String speechText = "<p>Ha ocurrido un error al contactar con el consorcio de transportes.</p>" +
                        "<p>Vuelve a intentarlo mas tarde</p>";
                builder.withSpeech(speechText);
                builder.withReprompt(speechText);
                builder.withSimpleCard("Error remoto", speechText);

                builder.withShouldEndSession(true);
            }
        } else {
            builder.addDelegateDirective(intentRequest.getIntent());
        }

        return builder.build();
    }
}
