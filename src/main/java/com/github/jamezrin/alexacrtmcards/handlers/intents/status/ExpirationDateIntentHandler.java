/*
     Copyright 2018 Amazon.com, Inc. or its affiliates. All Rights Reserved.

     Licensed under the Apache License, Version 2.0 (the "License"). You may not use this file
     except in compliance with the License. A copy of the License is located at

         http://aws.amazon.com/apache2.0/

     or in the "license" file accompanying this file. This file is distributed on an "AS IS" BASIS,
     WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for
     the specific language governing permissions and limitations under the License.
*/

package com.github.jamezrin.alexacrtmcards.handlers.intents.status;

import com.amazon.ask.attributes.AttributesManager;
import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.dispatcher.request.handler.RequestHandler;
import com.amazon.ask.model.Response;
import com.amazon.ask.response.ResponseBuilder;
import com.github.jamezrin.alexacrtmcards.SkillUtils;
import com.github.jamezrin.crtmcards.EndpointClient;
import com.github.jamezrin.crtmcards.ResponseParser;
import com.github.jamezrin.crtmcards.exceptions.ScraperException;
import com.github.jamezrin.crtmcards.exceptions.UnsuccessfulRequestException;
import com.github.jamezrin.crtmcards.types.Card;
import com.github.jamezrin.crtmcards.types.CardRenewal;
import org.apache.http.HttpResponse;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Map;
import java.util.Optional;

import static com.amazon.ask.request.Predicates.intentName;

public class ExpirationDateIntentHandler implements RequestHandler {
    private final EndpointClient endpointClient;

    public ExpirationDateIntentHandler(EndpointClient endpointClient) {
        this.endpointClient = endpointClient;
    }

    @Override
    public boolean canHandle(HandlerInput input) {
        return input.matches(intentName("ExpirationDateIntent")
                .and(SkillUtils::hasProvidedCard));
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
            Card card = responseParser.parse();

            try {
                int renewalsLen = card.getRenewals().length;
                CardRenewal lastRenewal = card.getRenewals() [renewalsLen - 1];

                if (lastRenewal != null) {
                    LocalDate expDate = lastRenewal.getExpirationDate();
                    if (expDate != null) {
                        if (expDate.isAfter(LocalDate.now())) {
                            String speechText = String.format("Tu tarjeta caduca el dia %s",
                                    SkillUtils.formatSpeechDate(expDate));
                            builder.withSpeech(speechText);
                            builder.withReprompt(speechText);
                            builder.withSimpleCard("Tarjeta", speechText);
                        } else {
                            String speechText = String.format("Tu tarjeta caducó el dia %s",
                                    SkillUtils.formatSpeechDate(expDate));
                            builder.withSpeech(speechText);
                            builder.withReprompt(speechText);
                            builder.withSimpleCard("Caducada", speechText);
                        }
                    } else {
                        String speechText = "Tu tarjeta ya ha caducado";
                        builder.withSpeech(speechText);
                        builder.withReprompt(speechText);
                        builder.withSimpleCard("Caducada", speechText);
                    }
                }
            } catch (ArrayIndexOutOfBoundsException e) {
                String speechText = "Esta tarjeta no se ha llegado a recargar nunca";
                builder.withSpeech(speechText);
                builder.withReprompt(speechText);
                builder.withSimpleCard("Error", speechText);
            }
        } catch (IOException | UnsuccessfulRequestException e) {
            String speechText = "No se ha podido contactar con el servidor. Inténtalo mas tarde";
            builder.withSpeech(speechText);
            builder.withReprompt(speechText);
            builder.withSimpleCard("Error", speechText);
        } catch (ScraperException e) {
            String speechText = "No se ha podido extraer la información. Inténtalo mas tarde";
            builder.withSpeech(speechText);
            builder.withReprompt(speechText);
            builder.withSimpleCard("Error", speechText);
        }

        return builder.build();
    }
}
