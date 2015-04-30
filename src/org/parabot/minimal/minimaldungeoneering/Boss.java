package org.parabot.minimal.minimaldungeoneering;

import org.parabot.core.ui.Logger;
import org.parabot.environment.api.utils.Time;
import org.parabot.environment.scripts.framework.SleepCondition;
import org.parabot.environment.scripts.framework.Strategy;
import org.rev317.min.api.methods.Npcs;
import org.rev317.min.api.wrappers.Npc;

public class Boss implements Strategy
{
    private final int[] bossIds;

    public Boss(int[] bossIds)
    {
        this.bossIds = bossIds;
    }

    private Npc boss;

    @Override
    public boolean activate()
    {
        for (Npc n : Npcs.getNearest(bossIds))
        {
            if (n != null && n.getLocation().isReachable())
            {
                boss = n;

                return true;
            }
        }

        return false;
    }

    public void execute()
    {
        if (!boss.isInCombat())
        {
            Logger.addMessage("Attacking boss");

            boss.interact(Npcs.Option.ATTACK);

            Time.sleep(new SleepCondition()
            {
                @Override
                public boolean isValid()
                {
                    return boss.isInCombat();
                }
            }, 5000);
        }

        if (boss.isInCombat())
        {
            Logger.addMessage("Waiting for boss to die");

            Time.sleep(new SleepCondition()
            {
                @Override
                public boolean isValid()
                {
                    return Npcs.getNearest(bossIds).length == 0;
                }
            }, 1000);

            if (Npcs.getNearest(bossIds).length == 0)
            {
                Time.sleep(1000);
            }
        }
    }
}