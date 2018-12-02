package com.github.jamezrin.alexacrtmcards;

import com.amazon.ask.Skill;
import com.amazon.ask.SkillStreamHandler;
import com.amazon.ask.Skills;
import com.github.jamezrin.alexacrtmcards.handlers.intents.CancelAndStopIntentHandler;
import com.github.jamezrin.alexacrtmcards.handlers.intents.LaunchRequestHandler;
import com.github.jamezrin.alexacrtmcards.handlers.intents.SessionEndedRequestHandler;
import com.github.jamezrin.alexacrtmcards.handlers.intents.status.ExpirationDateIntentHandler;
import com.github.jamezrin.alexacrtmcards.handlers.intents.status.ProvideCardDetailsIntentHandler;
import com.github.jamezrin.crtmcards.EndpointClient;

// com.github.jamezrin.alexacrtmcards.MainSkillStreamHandler

public class MainSkillStreamHandler extends SkillStreamHandler {
    public static final String SKILL_ID = "amzn1.ask.skill.def1ad6d-f8e8-49f0-8d9f-6558599af1eb";

    private static Skill getSkill() {
        EndpointClient endpointClient = new EndpointClient();

        return Skills.standard()
                .addRequestHandlers(
                        new CancelAndStopIntentHandler(),
                        new ProvideCardDetailsIntentHandler(),
                        //new OverviewSkillIntentHandler(endpointClient),
                        //new RemainingDaysIntentHandler(endpointClient),
                        new ExpirationDateIntentHandler(endpointClient),
                        //new HelpIntentHandler(),
                        new LaunchRequestHandler(),
                        new SessionEndedRequestHandler())
                .withTableName("crtm-status")
                .withSkillId(SKILL_ID)
                .build();
    }

    public MainSkillStreamHandler() {
        super(getSkill());
    }
}
