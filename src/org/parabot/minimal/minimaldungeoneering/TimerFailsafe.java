package org.parabot.minimal.minimaldungeoneering;

import org.parabot.core.ui.Logger;
import org.parabot.environment.api.utils.Time;
import org.parabot.environment.scripts.framework.Strategy;

public class TimerFailsafe implements Strategy
{
    @Override
    public boolean activate()
    {
        return MinimalDungeoneering.secondaryTimer.getRemaining() <= 0;
    }

    @Override
    public void execute()
    {
        Logger.addMessage("Secondary timer has ran out - the dungeon may have been bugged.");

        MinimalDungeoneering.secondaryTimer.restart();

        MinimalDungeoneering.forceLogout();

        Time.sleep(5000);
    }
}