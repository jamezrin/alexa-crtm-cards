package com.jamezrin.alexaskills.crtmcards.skill;

import com.amazon.ask.Skill;
import com.amazon.ask.SkillStreamHandler;
import com.amazon.ask.Skills;
import com.jamezrin.alexaskills.crtmcards.skill.handlers.*;

import static com.jamezrin.alexaskills.crtmcards.AppConsts.SKILL_ID;

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
                .withSkillId(SKILL_ID)
                .build();
    }

    public MainSkillStreamHandler() {
        super(getSkill());
    }
}
