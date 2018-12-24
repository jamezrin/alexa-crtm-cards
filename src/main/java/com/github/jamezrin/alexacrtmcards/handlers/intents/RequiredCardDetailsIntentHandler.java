package com.github.jamezrin.alexacrtmcards.handlers.intents;

import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.dispatcher.request.handler.RequestHandler;
import com.amazon.ask.model.Response;
import com.amazon.ask.response.ResponseBuilder;

import java.util.Optional;

import static com.github.jamezrin.alexacrtmcards.util.SkillPredicates.cardSetupNeeded;

public class RequiredCardDetailsIntentHandler implements RequestHandler {
    @Override
    public boolean canHandle(HandlerInput handlerInput) {
        return handlerInput.matches(cardSetupNeeded());
    }

    @Override
    public Optional<Response> handle(HandlerInput input) {
        ResponseBuilder builder = input.getResponseBuilder();
        String speechText =
                "Antes de poder hacer esto necesito detalles de tu tarjeta de transportes. Para hacerlo tienes que " +
                "decir 'configurar'. Si necesitas mas ayuda di 'ayuda' y te daré mas detalles.";
        builder.withSpeech(speechText);
        builder.withReprompt(speechText);
        builder.withSimpleCard("Configurame", speechText);
        builder.withShouldEndSession(false);
        return builder.build();
    }
}
