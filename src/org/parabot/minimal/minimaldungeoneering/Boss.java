package org.parabot.minimal.minimaldungeoneering;

import org.parabot.core.ui.Logger;
import org.parabot.environment.api.utils.Time;
import org.parabot.environment.scripts.framework.SleepCondition;
import org.parabot.environment.scripts.framework.Strategy;
import org.rev317.min.api.methods.Npcs;
import org.rev317.min.api.wrappers.Npc;

public class Boss implements Strategy
{
    /**
     * 9916 - Luminescent icefiend
     * 9934 - Plane-freezer Lakhrahnaz
     * 9989 - Asta Frost Web
     * 10044 - Icy Bones
     * 10064 - Hobgoblin Geomancer
     * 10110 - Bulkwark Beast
     * 10116 - Unholy Cursebearer
     */
    private static final int[] BOSS_IDS = { 9916, 9934, 9989, 10044, 10064, 10110, 10116 };

    private Npc boss;

    @Override
    public boolean activate()
    {
        for (Npc n : Npcs.getNearest(BOSS_IDS))
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
        MinimalDungeoneering.monsterVisible = true;

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
                    return Npcs.getNearest(BOSS_IDS).length == 0;
                }
            }, 1000);

            if (Npcs.getNearest(BOSS_IDS).length == 0)
            {
                Time.sleep(1000);
            }
        }
    }
}