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
import com.amazon.ask.model.Response;
import com.amazon.ask.response.ResponseBuilder;
import com.jamezrin.alexaskills.crtmcards.AppConsts;
import com.jamezrin.alexaskills.crtmcards.skill.CardInfo;

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

        String cardInfo = (String) input.getAttributesManager().getPersistentAttributes().get("CARD_INFO");

        if (cardInfo != null) {
            String speech = "I have your data";
            builder.withSpeech(speech);
            builder.withReprompt(speech);
            builder.withSimpleCard(SKILL_CARD_TITLE, speech);
        } else {
            String speech = "I don't have your data";
            builder.withSpeech(speech);
            builder.withReprompt(speech);
            builder.withSimpleCard(SKILL_CARD_TITLE, speech);
        }

        return builder.build();
    }
}
