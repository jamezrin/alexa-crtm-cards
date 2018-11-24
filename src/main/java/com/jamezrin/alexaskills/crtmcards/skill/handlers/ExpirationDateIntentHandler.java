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

import java.util.Map;
import java.util.Optional;

import static com.amazon.ask.request.Predicates.intentName;

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

        Map<String, Slot> slots = intent.getSlots();

        Slot prefixSlot = slots.get("prefix");
        if (prefixSlot.getValue() != null) {
            attributes.put("ttp_prefix", prefixSlot.getValue());
        }

        Slot numberSlot = slots.get("number");
        if (numberSlot.getValue() != null) {
            attributes.put("ttp_number", numberSlot.getValue());
        }

        if (prefixSlot.getValue() != null || numberSlot.getValue() != null) {
            attributesManager.setPersistentAttributes(attributes);
            attributesManager.savePersistentAttributes();
        }

        String prefix = (String) attributes.get("ttp_prefix");
        if (prefix == null) {
            builder.withSpeech("Dame tu prefijo");
            builder.addElicitSlotDirective("prefix", intent);
            return builder.build();
        }

        String number = (String) attributes.get("ttp_number");
        if (number == null) {
            builder.withSpeech("Dame tu numero");
            builder.addElicitSlotDirective("number", intent);
            return builder.build();
        }

        builder.withSpeech("Tu prefijo es " + prefix + " y tu numero es " + number);
        return builder.build();
    }
}
