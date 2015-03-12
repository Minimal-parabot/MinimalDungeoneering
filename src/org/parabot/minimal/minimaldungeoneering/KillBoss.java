package org.parabot.minimal.minimaldungeoneering;

import org.parabot.environment.api.utils.Time;
import org.parabot.environment.scripts.framework.SleepCondition;
import org.parabot.environment.scripts.framework.Strategy;
import org.rev317.min.api.methods.Npcs;
import org.rev317.min.api.wrappers.Npc;

/**
 * This class is in charge of attacking the boss if he isn't already in combat
 */
public class KillBoss implements Strategy
{
    private final int[] BOSS_IDS;

    public KillBoss(int[] BOSS_IDS)
    {
        this.BOSS_IDS = BOSS_IDS;
    }

    private Npc boss;

    @Override
    public boolean activate()
    {
        for (Npc n : Npcs.getNearest(BOSS_IDS))
        {
            if (n != null
                    && !n.isInCombat())
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

        MinimalDungeoneering.status = "Attacking";

        boss.interact(1);

        Time.sleep(new SleepCondition()
        {
            @Override
            public boolean isValid()
            {
                return boss.isInCombat();
            }
        }, 5000);
    }
}