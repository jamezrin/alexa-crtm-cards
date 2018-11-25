/*
     Copyright 2018 Amazon.com, Inc. or its affiliates. All Rights Reserved.

     Licensed under the Apache License, Version 2.0 (the "License"). You may not use this file
     except in compliance with the License. A copy of the License is located at

         http://aws.amazon.com/apache2.0/

     or in the "license" file accompanying this file. This file is distributed on an "AS IS" BASIS,
     WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for
     the specific language governing permissions and limitations under the License.
*/

package com.jamezrin.alexaskills.crtmcards.skill.handlers;

import com.amazon.ask.attributes.AttributesManager;
import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.dispatcher.request.handler.RequestHandler;
import com.amazon.ask.model.*;
import com.amazon.ask.response.ResponseBuilder;
import com.jamezrin.alexaskills.crtmcards.AppUtils;
import com.jamezrin.alexaskills.crtmcards.scraper.EndpointConnector;
import com.jamezrin.alexaskills.crtmcards.scraper.ResponseParser;
import com.jamezrin.alexaskills.crtmcards.scraper.exceptions.ScraperException;
import com.jamezrin.alexaskills.crtmcards.scraper.types.Card;
import com.jamezrin.alexaskills.crtmcards.scraper.types.CardRenewal;
import org.apache.http.HttpResponse;
import org.apache.http.impl.client.CloseableHttpClient;

import java.io.IOException;
import java.time.LocalDate;
import java.time.chrono.ChronoLocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.time.temporal.TemporalAccessor;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;

import static com.amazon.ask.request.Predicates.intentName;
import static com.jamezrin.alexaskills.crtmcards.AppConsts.VIEW_STATE;
import static com.jamezrin.alexaskills.crtmcards.scraper.ScraperUtils.makeHttpClient;

public class ExpirationDateIntentHandler implements RequestHandler {
    private static final CloseableHttpClient httpClient = makeHttpClient(20000);

    @Override
    public boolean canHandle(HandlerInput input) {
        return input.matches(intentName("ExpirationDateIntent"));
    }

    @Override
    public Optional<Response> handle(HandlerInput input) {
        ResponseBuilder builder = input.getResponseBuilder();
        AttributesManager attributesManager = input.getAttributesManager();
        Map<String, Object> attributes = attributesManager.getPersistentAttributes();
        Request request = input.getRequestEnvelope().getRequest();
        IntentRequest intentRequest = (IntentRequest) request;
        Intent intent = intentRequest.getIntent();

        Map<String, Slot> slots = intent.getSlots();

        Slot prefixSlot = slots.get("prefix");
        String prefixSlotValue = prefixSlot.getValue();
        Slot numberSlot = slots.get("number");
        String numberSlotValue = numberSlot.getValue();

        if (prefixSlotValue != null) {
            if (prefixSlotValue.length() == 3) {
                attributes.put("ttp_prefix", prefixSlotValue);
            } else {
                builder.withSpeech("No parece correcto. Necesito los tres últimos dígitos de la primera linea");
                builder.withShouldEndSession(false);
                builder.addElicitSlotDirective("prefix", intent);
                return builder.build();
            }
        }

        if (numberSlotValue != null) {
            if (numberSlotValue.length() == 10) {
                attributes.put("ttp_number", numberSlotValue);
            } else {
                builder.withSpeech("No parece correcto. Necesito los diez dígitos de la segunda linea");
                builder.withShouldEndSession(false);
                builder.addElicitSlotDirective("prefix", intent);
                return builder.build();
            }
        }

        if (prefixSlotValue != null || numberSlotValue != null) {
            attributesManager.setPersistentAttributes(attributes);
            attributesManager.savePersistentAttributes();
        }

        String ttpPrefix = (String) attributes.get("ttp_prefix");
        String ttpNumber = (String) attributes.get("ttp_number");

        StringBuilder speech = new StringBuilder();
        if (ttpPrefix == null && ttpNumber == null) {
            speech.append("<p>Antes de poder ayudarte necesito que me des unos números de tu tarjeta. ");
            speech.append("Los puedes encontrar al lado de tu foto en tu tarjeta de transportes. </p>");
        }

        if (ttpPrefix == null) {
            speech.append("Dame los tres últimos dígitos de la primera linea");
            builder.withSpeech(speech.toString());
            builder.withShouldEndSession(false);
            builder.addElicitSlotDirective("prefix", intent);
            return builder.build();
        }

        if (ttpNumber == null) {
            speech.append("Dame los diez dígitos de la segunda linea");
            builder.withSpeech(speech.toString());
            builder.withShouldEndSession(false);
            builder.addElicitSlotDirective("number", intent);
            return builder.build();
        }

        EndpointConnector endpointConnector = new EndpointConnector(
                VIEW_STATE,
                ttpPrefix,
                ttpNumber
        );

        try {
            HttpResponse response = endpointConnector.connect(httpClient);
            ResponseParser responseParser = new ResponseParser(response);
            Card card = responseParser.parse();
            CardRenewal lastRenewal = card.getRenewals() [card.getRenewals().length - 1];
            if (lastRenewal != null) {
                LocalDate expDate = lastRenewal.getExpirationDate();
                if (expDate != null && expDate.isAfter(LocalDate.now())) { // no ha caducado
                    builder.withSpeech("Tu tarjeta caduca el " + AppUtils.formatSpeechDate(expDate));
                } else {
                    builder.withSpeech("Tu tarjet ya ha caducado");
                }

                return builder.build();
            }
        } catch (IOException e) {
            speech.append("No se ha podido contactar con el servidor. Intentalo mas tarde");
            builder.withSpeech(speech.toString());
            return builder.build();
        } catch (ScraperException e) {
            speech.append("No se ha podido extraer la información. Intentalo mas tarde");
            builder.withSpeech(speech.toString());
            return builder.build();
        }


        return builder.build();
    }
}
