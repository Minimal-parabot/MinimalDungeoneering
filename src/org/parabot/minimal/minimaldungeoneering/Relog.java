package org.parabot.minimal.minimaldungeoneering;

import org.parabot.environment.api.utils.Time;
import org.parabot.environment.input.Keyboard;
import org.parabot.environment.scripts.framework.SleepCondition;
import org.parabot.environment.scripts.framework.Strategy;
import org.rev317.min.Loader;

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
        return !Loader.getClient().isLoggedIn();
    }

    @Override
    public void execute()
    {

        MinimalDungeoneering.status = "Possible dc";
        Time.sleep(new SleepCondition()
        {
            @Override
            public boolean isValid()
            {
                return Loader.getClient().isLoggedIn();
            }
        }, 3000);

        if (!Loader.getClient().isLoggedIn())
        {
            MinimalDungeoneering.status = "Relogging";

            Keyboard.getInstance().clickKey(KeyEvent.VK_ENTER);

            Time.sleep(new SleepCondition()
            {
                @Override
                public boolean isValid()
                {
                    return Loader.getClient().isLoggedIn();
                }
            }, 5000);

            if (Loader.getClient().isLoggedIn())
            {
                MinimalDungeoneering.status += "..";

                Time.sleep(4000);
            }
        }
    }
}