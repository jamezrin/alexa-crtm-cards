package com.github.jamezrin.alexacrtmcards.handlers.intents;

import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.dispatcher.request.handler.RequestHandler;
import com.amazon.ask.model.Response;
import com.amazon.ask.response.ResponseBuilder;

import java.util.Optional;

import static com.amazon.ask.request.Predicates.intentName;
import static com.github.jamezrin.alexacrtmcards.util.SkillPredicates.hasProvidedCard;

public class HelpIntentHandler implements RequestHandler {

    @Override
    public boolean canHandle(HandlerInput input) {
        return input.matches(intentName("AMAZON.HelpIntent"));
    }

    @Override
    public Optional<Response> handle(HandlerInput input) {
        ResponseBuilder builder = input.getResponseBuilder();

        if (hasProvidedCard().test(input)) {
            String speechText =
                    "Me puedes pedir que te diga cuando caduca tu tarjeta diciendo 'cuando caduca mi tarjeta' " +
                    "o si lo prefieres dí 'cuantos dias le quedan a mi tarjeta'. Recuerda que las demás veces que " +
                    "me preguntes, lo tienes que hacerlo preguntando a la habilidad, por ejemplo 'Alexa, pregunta a " +
                    "Consorcio de Transportes cuando caduca mi tarjeta'";
            builder.withSpeech(speechText);
            builder.withReprompt(speechText);
            builder.withSimpleCard("Ayuda", speechText);
        } else {
            String speechText =
                    "Antes de poder utilizar esta habilidad, tienes que darme unos numeros de tu tarjeta diciendo 'configurar'. " +
                    "Los puedes encontrar en la parte frontal de tu tarjeta, justo al lado de tu foto en el carnet. " +
                    "El primer numero está en la primera linea y es de 3 dígitos, y el segundo numero está justo debajo y " +
                    "es de 10 dígitos. Una vez hecho eso podrás pedirme ayuda otra vez y te dire que puedes hacer.";
            builder.withSpeech(speechText);
            builder.withReprompt(speechText);
            builder.withSimpleCard("Ayuda", speechText);
        }

        builder.withShouldEndSession(false);
        return builder.build();
    }
}
