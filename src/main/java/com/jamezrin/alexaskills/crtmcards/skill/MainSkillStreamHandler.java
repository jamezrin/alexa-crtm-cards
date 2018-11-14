package com.jamezrin.alexaskills.crtmcards.skill;

import com.amazon.ask.Skill;
import com.amazon.ask.SkillStreamHandler;
import com.amazon.ask.Skills;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBAsyncClient;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBAsyncClientBuilder;
import com.jamezrin.alexaskills.crtmcards.scraper.ScraperUtils;
import com.jamezrin.alexaskills.crtmcards.skill.handlers.ExpirationDateIntentHandler;
import com.jamezrin.alexaskills.crtmcards.skill.handlers.OverviewSkillIntentHandler;
import com.jamezrin.alexaskills.crtmcards.skill.handlers.ProvideCardInfoIntentHandler;
import com.jamezrin.alexaskills.crtmcards.skill.handlers.RemainingDaysIntentHandler;
import com.jamezrin.alexaskills.crtmcards.skill.handlers.defaults.CancelAndStopIntentHandler;
import com.jamezrin.alexaskills.crtmcards.skill.handlers.defaults.HelpIntentHandler;
import com.jamezrin.alexaskills.crtmcards.skill.handlers.LaunchRequestHandler;
import com.jamezrin.alexaskills.crtmcards.skill.handlers.defaults.SessionEndedRequestHandler;

import static com.jamezrin.alexaskills.crtmcards.AppConsts.SKILL_ID;

// com.jamezrin.alexaskills.crtmcards.skill.MainSkillStreamHandler

public class MainSkillStreamHandler extends SkillStreamHandler {
    private static Skill getSkill() {
        return Skills.standard()
                .withDynamoDbClient(AmazonDynamoDBAsyncClientBuilder.standard().build())
                .addRequestHandlers(
                        new CancelAndStopIntentHandler(),
                        new OverviewSkillIntentHandler(),
                        new ProvideCardInfoIntentHandler(),
                        new RemainingDaysIntentHandler(),
                        new ExpirationDateIntentHandler(),
                        new HelpIntentHandler(),
                        new LaunchRequestHandler(),
                        new SessionEndedRequestHandler())
                .withSkillId(SKILL_ID)
                .build();
    }

    public MainSkillStreamHandler() {
        super(getSkill());
    }
}
