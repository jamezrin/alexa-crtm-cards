package com.github.jamezrin.alexacrtmcards.handlers.intents;

import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.dispatcher.request.handler.RequestHandler;
import com.amazon.ask.model.LaunchRequest;
import com.amazon.ask.model.Response;
import com.amazon.ask.response.ResponseBuilder;

import java.util.Optional;

import static com.amazon.ask.request.Predicates.requestType;
import static com.github.jamezrin.alexacrtmcards.util.SkillPredicates.hasProvidedCard;

public class LaunchRequestHandler implements RequestHandler {

    @Override
    public boolean canHandle(HandlerInput input) {
        return input.matches(requestType(LaunchRequest.class));
    }

    @Override
    public Optional<Response> handle(HandlerInput input) {
        ResponseBuilder builder = input.getResponseBuilder();


        if (input.matches(hasProvidedCard())) {
            String speechText =
                    "Bienvenido de nuevo. ¿Que quieres hacer? Recuerda que " +
                    "me puedes pedir ayuda diciendo 'ayuda'";
            builder.withSpeech(speechText);
            builder.withReprompt(speechText);
            builder.withSimpleCard("Bienvenido", speechText);
        } else {
            String speechText =
                    "Bienvenido a esta habilidad. Para usar esta habilidad tienes que darme " +
                    "unos dígitos de tu tarjeta de transporte. Para ello dí 'configurar'";
            builder.withSpeech(speechText);
            builder.withReprompt(speechText);
            builder.withSimpleCard("Bienvenido", speechText);
        }

        builder.withShouldEndSession(false);
        return builder.build();
    }

}
