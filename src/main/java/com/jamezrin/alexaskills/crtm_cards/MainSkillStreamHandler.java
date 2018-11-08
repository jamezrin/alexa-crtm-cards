package com.jamezrin.alexaskills.crtm_cards;

import com.amazon.ask.Skill;
import com.amazon.ask.SkillStreamHandler;
import com.amazon.ask.Skills;
import com.jamezrin.alexaskills.crtm_cards.handlers.*;

public class MainSkillStreamHandler extends SkillStreamHandler {
    private static Skill getSkill() {
        return Skills.standard()
                .addRequestHandlers(
                        new CancelAndStopIntentHandler(),
                        new HelloWorldIntentHandler(),
                        new HelpIntentHandler(),
                        new LaunchRequestHandler(),
                        new SessionEndedRequestHandler(),
                        new FallbackIntentHandler())
                // Add your skill id below
                //.withSkillId("")
                .build();
    }

    public MainSkillStreamHandler() {
        super(getSkill());
    }
}
