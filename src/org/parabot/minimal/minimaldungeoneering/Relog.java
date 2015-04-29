package org.parabot.minimal.minimaldungeoneering;

import org.parabot.core.ui.Logger;
import org.parabot.environment.api.utils.Time;
import org.parabot.environment.input.Keyboard;
import org.parabot.environment.scripts.framework.SleepCondition;
import org.parabot.environment.scripts.framework.Strategy;
import org.rev317.min.api.methods.Game;

import java.awt.event.KeyEvent;

/**
 * This class handles the relogging
 * No password is necessary since Ikov doesn't remove it after log-in
 * The first sleep condition is there just to ensure we aren't dcing
 */
public class Relog implements Strategy
{
    @Override
    public boolean activate()
    {
        return !Game.isLoggedIn();
    }

    @Override
    public void execute()
    {
        Logger.addMessage("Relogging");

        Keyboard.getInstance().clickKey(KeyEvent.VK_ENTER);

        Time.sleep(new SleepCondition()
        {
            @Override
            public boolean isValid()
            {
                return Game.isLoggedIn();
            }
        }, 5000);

        if (Game.isLoggedIn())
        {
            Logger.addMessage("Waiting after relog..");

            Time.sleep(4000);
        }
    }
}