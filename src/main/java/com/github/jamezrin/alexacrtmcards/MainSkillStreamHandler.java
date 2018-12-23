package com.github.jamezrin.alexacrtmcards;

import com.amazon.ask.Skill;
import com.amazon.ask.SkillStreamHandler;
import com.amazon.ask.Skills;
import com.amazon.ask.attributes.persistence.impl.DynamoDbPersistenceAdapter;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.github.jamezrin.alexacrtmcards.handlers.exceptions.*;
import com.github.jamezrin.alexacrtmcards.handlers.intents.CancelAndStopIntentHandler;
import com.github.jamezrin.alexacrtmcards.handlers.intents.HelpIntentHandler;
import com.github.jamezrin.alexacrtmcards.handlers.intents.LaunchRequestHandler;
import com.github.jamezrin.alexacrtmcards.handlers.intents.SessionEndedRequestHandler;
import com.github.jamezrin.alexacrtmcards.handlers.intents.status.ExpirationDateIntentHandler;
import com.github.jamezrin.alexacrtmcards.handlers.intents.status.ProvideCardDetailsIntentHandler;
import com.github.jamezrin.alexacrtmcards.handlers.intents.status.RemainingDaysIntentHandler;
import com.github.jamezrin.crtmcards.EndpointClient;

// com.github.jamezrin.alexacrtmcards.MainSkillStreamHandler

public class MainSkillStreamHandler extends SkillStreamHandler {
    private static Skill getSkill() {
        EndpointClient endpointClient = new EndpointClient();

        String skillId = System.getenv("SKILL_ID");
        String tableName = System.getenv("TABLE_NAME");

        return Skills.standard()
                .addRequestHandlers(
                        new CancelAndStopIntentHandler(),
                        new ProvideCardDetailsIntentHandler(),
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
                .withPersistenceAdapter(DynamoDbPersistenceAdapter.builder()
                        .withDynamoDbClient(AmazonDynamoDBClientBuilder.standard()
                                .build())
                        .build())
                .withTableName(tableName)
                .withSkillId(skillId)
                .build();
    }

    public MainSkillStreamHandler() {
        super(getSkill());
    }
}
