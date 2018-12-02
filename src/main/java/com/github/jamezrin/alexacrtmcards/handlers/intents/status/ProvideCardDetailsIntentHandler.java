package com.github.jamezrin.alexacrtmcards.handlers.intents.status;

import com.amazon.ask.attributes.AttributesManager;
import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.dispatcher.request.handler.RequestHandler;
import com.amazon.ask.model.*;
import com.amazon.ask.response.ResponseBuilder;

import java.util.Map;
import java.util.Optional;

import static com.amazon.ask.request.Predicates.intentName;
import static com.github.jamezrin.alexacrtmcards.SkillUtils.hasProvidedCard;

public class ProvideCardDetailsIntentHandler implements RequestHandler {
    @Override
    public boolean canHandle(HandlerInput input) {
        return input.matches(intentName("ProvideCardDetailsIntent")
                .or((handlerInput) -> !hasProvidedCard(handlerInput)));
    }

    @Override
    public Optional<Response> handle(HandlerInput input) {
        ResponseBuilder builder = input.getResponseBuilder();
        Request request = input.getRequestEnvelope().getRequest();
        IntentRequest intentRequest = (IntentRequest) request;

        if (intentRequest.getDialogState() == DialogState.COMPLETED) {
            Intent intent = intentRequest.getIntent();
            Map<String, Slot> slots = intent.getSlots();

            AttributesManager attributesManager = input.getAttributesManager();
            Map<String, Object> attributes = attributesManager.getPersistentAttributes();

            Slot prefixSlot = slots.get("prefix");
            String prefixSlotValue = prefixSlot.getValue();
            attributes.put("ttp_prefix", prefixSlotValue);

            Slot numberSlot = slots.get("number");
            String numberSlotValue = numberSlot.getValue();
            attributes.put("ttp_number", numberSlotValue);

            attributesManager.setPersistentAttributes(attributes);
            attributesManager.savePersistentAttributes();

            String speechText = "<p>¡Ya está todo! Ya no tendrás que hacer este paso nunca mas</p>" +
                    "<p>Dime algo como <emphasis level=\"moderate\">Cuando caduca mi tarjeta de transporte</emphasis>";

            builder.withSpeech(speechText);
            builder.withReprompt(speechText);
            builder.withSimpleCard("Hecho", speechText);

            builder.withShouldEndSession(false);
        } else {
            builder.addDelegateDirective(intentRequest.getIntent());
        }

        return builder.build();
    }
}
