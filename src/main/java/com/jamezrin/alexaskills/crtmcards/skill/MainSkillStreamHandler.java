package com.jamezrin.alexaskills.crtmcards.skill;

import com.amazon.ask.Skill;
import com.amazon.ask.SkillStreamHandler;
import com.amazon.ask.Skills;
import com.amazon.ask.attributes.persistence.PersistenceAdapter;
import com.amazon.ask.attributes.persistence.impl.DynamoDbPersistenceAdapter;
import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.services.dynamodbv2.*;
import com.amazonaws.services.dynamodbv2.xspec.S;
import com.jamezrin.alexaskills.crtmcards.scraper.ScraperUtils;
import com.jamezrin.alexaskills.crtmcards.skill.handlers.ExpirationDateIntentHandler;
import com.jamezrin.alexaskills.crtmcards.skill.handlers.OverviewSkillIntentHandler;
import com.jamezrin.alexaskills.crtmcards.skill.handlers.ProvideCardInfoIntentHandler;
import com.jamezrin.alexaskills.crtmcards.skill.handlers.RemainingDaysIntentHandler;
import com.jamezrin.alexaskills.crtmcards.skill.handlers.defaults.CancelAndStopIntentHandler;
import com.jamezrin.alexaskills.crtmcards.skill.handlers.defaults.HelpIntentHandler;
import com.jamezrin.alexaskills.crtmcards.skill.handlers.LaunchRequestHandler;
import com.jamezrin.alexaskills.crtmcards.skill.handlers.defaults.SessionEndedRequestHandler;
import org.apache.http.impl.client.CloseableHttpClient;

import static com.jamezrin.alexaskills.crtmcards.AppConsts.SKILL_ID;
import static com.jamezrin.alexaskills.crtmcards.scraper.ScraperUtils.makeHttpClient;

// com.jamezrin.alexaskills.crtmcards.skill.MainSkillStreamHandler

public class MainSkillStreamHandler extends SkillStreamHandler {
    private static Skill getSkill() {
        return Skills.standard()
                .addRequestHandlers(
                        new CancelAndStopIntentHandler(),
                        new OverviewSkillIntentHandler(),
                        new ProvideCardInfoIntentHandler(),
                        new RemainingDaysIntentHandler(),
                        new ExpirationDateIntentHandler(),
                        new HelpIntentHandler(),
                        new LaunchRequestHandler(),
                        new SessionEndedRequestHandler())
                //.withHttpClient(makeHttpClient(20000))
                .withTableName("crtm-cards")
                .withSkillId(SKILL_ID)
                .build();
    }

    public MainSkillStreamHandler() {
        super(getSkill());
    }
}
