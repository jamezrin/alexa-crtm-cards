package com.jamezrin.alexaskills.crtmcards.skill;

import com.amazon.ask.Skill;
import com.amazon.ask.SkillStreamHandler;
import com.amazon.ask.Skills;
import com.jamezrin.alexaskills.crtmcards.skill.handlers.HelloWorldIntentHandler;
import com.jamezrin.alexaskills.crtmcards.skill.handlers.defaults.CancelAndStopIntentHandler;
import com.jamezrin.alexaskills.crtmcards.skill.handlers.defaults.HelpIntentHandler;
import com.jamezrin.alexaskills.crtmcards.skill.handlers.defaults.LaunchRequestHandler;
import com.jamezrin.alexaskills.crtmcards.skill.handlers.defaults.SessionEndedRequestHandler;

import static com.jamezrin.alexaskills.crtmcards.AppConsts.SKILL_ID;

// com.jamezrin.alexaskills.crtmcards.skill.MainSkillStreamHandler
public class MainSkillStreamHandler extends SkillStreamHandler {
    private static Skill getSkill() {
        return Skills.standard()
                .addRequestHandlers(
                        new CancelAndStopIntentHandler(),
                        new HelloWorldIntentHandler(),
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
