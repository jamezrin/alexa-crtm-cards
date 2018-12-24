package com.github.jamezrin.alexacrtmcards;

import com.amazon.ask.Skill;
import com.amazon.ask.SkillStreamHandler;
import com.amazon.ask.Skills;
import com.github.jamezrin.alexacrtmcards.handlers.exceptions.*;
import com.github.jamezrin.alexacrtmcards.handlers.intents.*;
import com.github.jamezrin.crtmcards.EndpointClient;

// com.github.jamezrin.alexacrtmcards.MainSkillStreamHandler

public class MainSkillStreamHandler extends SkillStreamHandler {
    private static Skill getSkill() {
        //String attrKey = System.getenv("ATTR_KEY");
        String skillId = System.getenv("SKILL_ID");
        String tableName = System.getenv("TABLE_NAME");

        EndpointClient endpointClient = new EndpointClient();
        //AttributeHandler attributeHandler = new AttributeHandler(attrKey);

        return Skills.standard()
                .addRequestHandlers(
                        new CancelAndStopIntentHandler(),
                        new RequiredCardDetailsIntentHandler(),
                        new ProvideCardDetailsIntentHandler(endpointClient),
                        //new OverviewSkillIntentHandler(endpointClient),
                        new RemainingDaysIntentHandler(endpointClient),
                        new ExpirationDateIntentHandler(endpointClient),
                        new HelpIntentHandler(),
                        new LaunchRequestHandler(),
                        new SessionEndedRequestHandler())
                .addExceptionHandlers(
                        new InvalidCardNumberExceptionHandler(),
                        new InactiveCardNumberExceptionHandler(),
                        new CurrentlyUnavailableExceptionHandler(),
                        new NotExistentCardNumberExceptionHandler(),
                        new UnsuccessfulRequestExceptionHandler(),
                        new ScraperExceptionHandler())
                .withTableName(tableName)
                .withSkillId(skillId)
                .build();
    }

    public MainSkillStreamHandler() {
        super(getSkill());
    }
}
