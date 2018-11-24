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

import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.dispatcher.request.handler.RequestHandler;
import com.amazon.ask.model.*;
import com.amazon.ask.response.ResponseBuilder;
import com.jamezrin.alexaskills.crtmcards.skill.CardInfo;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static com.amazon.ask.request.Predicates.intentName;

public class ProvideCardInfoIntentHandler implements RequestHandler {

    @Override
    public boolean canHandle(HandlerInput input) {
        return input.matches(intentName("ProvideCardInfoIntent"));
    }

    @Override
    public Optional<Response> handle(HandlerInput input) {
        Request request = input.getRequestEnvelope().getRequest();
        IntentRequest intentRequest = (IntentRequest) request;
        Intent intent = intentRequest.getIntent();
        Map<String, Slot> slots = intent.getSlots();

        Slot prefix = slots.get("prefix");
        Slot number = slots.get("number");

        Map<String, Object> attributes = input.getAttributesManager().getPersistentAttributes();

        attributes.put("prefix", prefix.getValue());
        attributes.put("number", number.getValue());

        input.getAttributesManager().setPersistentAttributes(attributes);
        input.getAttributesManager().savePersistentAttributes();

        ResponseBuilder builder = input.getResponseBuilder();
        // input.getRequestEnvelope().getSession().getUser().getUserId()
        builder.withSpeech("Comprueba la base de datos");

        return builder.build();
    }
}
