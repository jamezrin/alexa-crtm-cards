package com.github.jamezrin.alexacrtmcards.handlers.intents.status;

import com.amazon.ask.attributes.AttributesManager;
import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.dispatcher.request.handler.RequestHandler;
import com.amazon.ask.model.*;
import com.amazon.ask.response.ResponseBuilder;

import java.util.Map;
import java.util.Optional;

import static com.amazon.ask.request.Predicates.intentName;
import static com.github.jamezrin.alexacrtmcards.util.SkillUtils.hasProvidedCard;

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
        Intent intent = intentRequest.getIntent();
        Map<String, Slot> slots = intent.getSlots();

        Slot prefixSlot = slots.get("prefix");
        Slot numberSlot = slots.get("number");
        //if (intentRequest.getDialogState() == DialogState.COMPLETED) {
        if (prefixSlot.getValue() != null && numberSlot.getValue() != null) {
            AttributesManager attributesManager = input.getAttributesManager();
            Map<String, Object> attributes = attributesManager.getPersistentAttributes();


            String prefixSlotValue = prefixSlot.getValue();
            attributes.put("ttp_prefix", prefixSlotValue);

            String numberSlotValue = numberSlot.getValue();
            attributes.put("ttp_number", numberSlotValue);

            attributesManager.setPersistentAttributes(attributes);
            attributesManager.savePersistentAttributes();

            String speechText = "<p>¡Ya está todo! Ya no tendrás que hacer este paso nunca mas. </p>" +
                    "<p>Dime algo como Cuando caduca mi tarjeta de transporte</p>";
            builder.withSpeech(speechText);
            builder.withReprompt(speechText);
            builder.withSimpleCard("¡Ya está!", speechText);

            builder.withShouldEndSession(false);
        } else {
            builder.addDelegateDirective(intentRequest.getIntent());
        }

        return builder.build();
    }
}
