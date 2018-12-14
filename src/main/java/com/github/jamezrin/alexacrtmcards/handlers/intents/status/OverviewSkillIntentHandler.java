package com.github.jamezrin.alexacrtmcards.handlers.intents.status;

import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.dispatcher.request.handler.RequestHandler;
import com.amazon.ask.model.Response;
import com.amazon.ask.response.ResponseBuilder;

import java.util.Optional;

import static com.amazon.ask.request.Predicates.intentName;
import static com.github.jamezrin.alexacrtmcards.util.SkillUtils.hasProvidedCard;

public class OverviewSkillIntentHandler implements RequestHandler {
    @Override
    public boolean canHandle(HandlerInput input) {
        return input.matches(intentName("OverviewSkillIntent")
                .and(hasProvidedCard()));
    }

    @Override
    public Optional<Response> handle(HandlerInput input) {
        ResponseBuilder builder = input.getResponseBuilder();
        builder.withSpeech("Está funcionando, bien!");
        return builder.build();
    }
}
