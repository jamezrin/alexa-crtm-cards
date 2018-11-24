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
import com.amazon.ask.model.canfulfill.CanFulfillIntent;
import com.amazon.ask.model.canfulfill.CanFulfillSlot;
import com.amazon.ask.model.dialog.DelegateDirective;
import com.amazon.ask.response.ResponseBuilder;
import com.jamezrin.alexaskills.crtmcards.AppConsts;
import com.jamezrin.alexaskills.crtmcards.skill.CardInfo;

import java.util.Map;
import java.util.Optional;

import static com.amazon.ask.request.Predicates.intentName;
import static com.jamezrin.alexaskills.crtmcards.AppConsts.SKILL_CARD_TITLE;

public class ExpirationDateIntentHandler implements RequestHandler {
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

        String prefix = (String) attributes.get("ttp_prefix");
        String number = (String) attributes.get("ttp_number");

        StringBuilder speech = new StringBuilder();
        if (prefix == null || number == null) {
            //speech.append("Antes de poder ayudarte necesito que me des unos numeros de tu tarjeta. ");
            //speech.append("Los puedes encontrar al lado de tu foto en tu tarjeta de transportes.");
        }

        if (prefix == null) {
            speech.append("Dime los tres ultimos digitos de la primera linea");
            builder.withSpeech(speech.toString());
            builder.addElicitSlotDirective("prefix", intent);
            return builder.build();
        }

        if (number == null) {
            speech.append("Dime todos los digitos de la segunda linea");
            builder.withSpeech(speech.toString());
            builder.addElicitSlotDirective("number", intent);
            return builder.build();
        }

        Map<String, Slot> slots = intent.getSlots();

        Slot prefixSlot = slots.get("prefix");
        Slot numberSlot = slots.get("number");

        if (prefixSlot.getValue() != null || numberSlot.getValue() != null) {
            if (prefixSlot.getValue() != null) {
                speech.append("Tienes un prefijo asignado: ");
                speech.append(prefixSlot.getValue());
                attributes.put("ttp_prefix", prefixSlot.getValue());
            }

            if (numberSlot.getValue() != null) {
                speech.append("Tienes un numero asignado: ");
                speech.append(numberSlot.getValue());
                attributes.put("ttp_number", numberSlot.getValue());
            }

            input.getAttributesManager().setPersistentAttributes(attributes);
            input.getAttributesManager().savePersistentAttributes();
        }




        builder.withSpeech(speech.toString());
        return builder.build();
    }
}
